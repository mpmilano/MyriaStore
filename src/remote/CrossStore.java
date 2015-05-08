package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;

#define CausalStore Store<Causal, CausalObj, CausalType, CReplicaID, CausalP>
#define LinStore Store<Lin, LinObj, LinType, LinReplica, LinP>
#define CrossStoreT CrossStore<CausalObj, CausalType, CReplicaID, CausalP, LinObj, LinType, LinReplica, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType, CReplicaID extends Serializable, CausalP, LinObj extends RemoteObject, LinType, LinReplica, LinP>
	extends Store<Causal, CrossStore.CrossObject, CausalType, Void, CrossStore> {
	//private CausalStore causal;
	//private LinStore lin;

	final CausalStore this_store;
	
		{
			#define ReplicaID CReplicaID
			#define Nonce String
				#define MetaData FourTuple<TreeSet<Pair<ReplicaID,Nonce>>, \
				Nonce, ReplicaID, Ends>
				#define MD_readfrom(x) x.a
				#define MD_n(x) x.b
				#define MD_natural_replica(x) x.c
				#define MD_ends(x) x.d
		}

	private final CausalType tombstone_word;
	private CausalType tombstone_name(String s){
		return this_store.concat(tombstone_word,
								 this_store.ofString(s));
	}
	
	private class Tombstone implements Serializable {
		public final Nonce n;
		public final CausalType name;

		public Tombstone(Nonce n){
			this.n = n;
			this.name = tombstone_name(n);
		}

	}

	private class Ends extends HashMap<CReplicaID, Timestamp> implements CausalSafe, Mergable<Ends>{
		public boolean prec(Ends e){

			Set<CReplicaID> crs = new TreeSet<>();
			crs.addAll(keySet());
			crs.addAll(e.keySet());
			
			for (CReplicaID cr : crs){
				Timestamp this_ts = get(cr);
				Timestamp e_ts = e.get(cr);
				if (!Timestamp.prec(this_ts,e_ts)) return false;
			}
			return true;
		}
		public Ends fast_forward(Ends future){
			for (CReplicaID cr : future.keySet()){
				put(cr,Timestamp.update(get(cr), future.get(cr)));
			}
			return this;
		}

		@Override
		public Ends merge(Ends e){
			return this.fast_forward(e);
		}
	}

	Object to_return = null;
		
	public <CS extends CausalStore &
					   HasClock &
					   AccessReplica
					   <Causal, CausalObj, CausalType, CReplicaID, CausalP>&
					   Synq<CReplicaID> >
		CrossStore(final CS c, final LinStore l){
		this_store = c;
		tombstone_word = this_store.ofString("tombstone-");
		//causal = c;
		//lin = l;

		final Ends ends = null;
		final Set<Pair<ReplicaID, Nonce>> readset = new TreeSet<>();
		final ReplicaID natural_replica = this_store.this_replica();
		final LinType metadata_suffix = l.ofString("metadata");

		{
			#define Arg Void
				#define Ret Ends
		}
		final Function<Arg,Ret> write_casual_meta= new Function<Arg,Ret>(){
				@Override
				public Ret apply(Arg arg){
					Ends tstamp = new Ends();
					for (Pair<ReplicaID, Nonce> rsp : readset){
						Timestamp t = ends.get(rsp.first);
						assert(t != null);
						tstamp.put(rsp.first,t);
					}
					return tstamp;
				}
			};

		l.registerOnWrite(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					Nonce n = NonceGenerator.get();
					{
						LinType meta_name =
							l.concat(name,metadata_suffix);
						TreeSet<Pair<ReplicaID, Nonce>> rscopy =
							new TreeSet<>();
						rscopy.addAll(readset);
						rscopy.add(new Pair<ReplicaID, Nonce>(natural_replica, n));
						//annotate with metadata
						l.newObject(new MetaData
									(rscopy, n, natural_replica, ends),
									meta_name, l);
					}
					//write tombstone
					try {
						Tombstone t = new Tombstone(n);
						newObject(t.name, t);
					} catch(Exception e){
						throw new RuntimeException(e);
					}
					
					return null;
				}
			});
		
		l.registerOnRead(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					LinType meta_name =
						l.concat(name,metadata_suffix);
					LinObj rmeta;
					try {rmeta = l.existingObject(meta_name);}
					catch (Exception e) {throw new RuntimeException(e);}
					@SuppressWarnings("unchecked")
					MetaData meta = (MetaData) rmeta.get();
					if (!MD_ends(meta).prec(ends)) {
						ends.fast_forward(MD_ends(meta));
						if (!contains_tombstone(MD_n(meta))){
							readset.addAll(MD_readfrom(meta));
							c.sync_req(MD_natural_replica(meta),
									   natural_replica);
						}
					}
					return null;
				}
			});

		c.registerOnRead(new Function<CausalType, Void>(){

				@SuppressWarnings("unchecked")
				private <T> T
					helper(Mergable<T> mp, CausalType name) {
					@SuppressWarnings("unchecked")
					T m = (T) mp;
					for (Pair<ReplicaID, Nonce> rsp : readset){
						try{
							T r = (T)
								c.access_replica(rsp.first)
								.existingObject(name).get();
							if (m == null) m = r;
							else m = (T) mp.merge(r);
						}
						catch(Exception e){
							throw new RuntimeException(e);
						}
					}
					return m;
				}
				
				@Override
				public Void apply(CausalType ct){
					Mergable<?> m = null;
					to_return = helper(m, ct);
					Ends e = null;
					ends.fast_forward(helper(e,c.concat
											 (c.ofString("metameta-"),ct)));
					return null;
				}
			});

		c.registerOnWrite(new Function<CausalType, Void>(){
				@Override
				public Void apply(CausalType ct){
					ends.put(natural_replica, c.currentTime());
					CausalType metaname =
						c.concat(c.ofString("metameta-"),ct);
					c.newObject(write_casual_meta.apply(null), metaname, c);
					return null;
				}
			});

		c.registerOnTick(new Runnable(){
				@Override
				public void run(){
					Object found = null;
					final List<Pair<ReplicaID, Nonce>> readset_new = new LinkedList<>();
					for (Pair<ReplicaID, Nonce> rsp : readset){
						if (!contains_tombstone(rsp.second))
							readset_new.add(rsp);
					}
					readset.clear();
					readset.addAll(readset_new);
				}
			});
	}

	private boolean contains_tombstone(Nonce n){
		Object found = null;
		try {
			found = this_store.existingObject(tombstone_name(n)).get();
			if (found != null) return true;
		}
		catch(Exception e) {}
		return false;
	}

	@Override
	protected <T extends Serializable> CrossObject newObject(CausalType arg, T init) throws Exception{
		//TODO - tracking?
		return new CrossObject<T>(this_store.newObject(arg,init));
	}

	@Override
	protected <T extends Serializable> CrossObject newObject(CausalType arg) throws Exception{
		//TODO - tracking?
		return new CrossObject<T>(this_store.newObject(arg));
	}

	

	public class CrossObject<T extends Serializable>
		implements RemoteObject<T>{

		final RemoteObject<T> real;

		public CrossObject(final CausalObj real){
			@SuppressWarnings("unchecked")
			RemoteObject<T> coerce = (RemoteObject<T>) real;
			this.real = coerce;
		}

		@Override
		public CrossStore getStore(){
			return CrossStore.this;
		}

		@Override
		public void put(T t){
			real.put(t);
		}

		@Override
		@SuppressWarnings("unchecked")
		public T get(){
			return (T) to_return;
		}
		
	}

	//Dummies and passthroughs

	@Override
	public Void this_replica() { return null; }

	@Override
	public CausalType concat(CausalType ct, CausalType ct2){
		return this_store.concat(ct,ct2);
	}

	@Override
	public CausalType ofString(String ct2){
		return this_store.ofString(ct2);
	}

	@Override
	public void registerOnRead(Function<CausalType, Void> f){
		//TODO - maybe black-hole? unimplemented?
		this_store.registerOnRead(f);
	}

	@Override
	public void registerOnWrite(Function<CausalType, Void> f){
		this_store.registerOnWrite(f);
	}

	@Override
	public CausalType genArg(){return this_store.genArg();}

	
		
}
