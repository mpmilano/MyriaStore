package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;

#define CausalStore Store<Causal, CausalObj, CausalType, CausalReplica, CausalP>
#define LinStore Store<Lin, LinObj, LinType, LinReplica, LinP>
#define CrossStoreT CrossStore<CausalObj, CausalType, CausalReplica, CausalP, LinObj, LinType, LinReplica, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType, CausalReplica extends HasClock, CausalP, LinObj extends RemoteObject, LinType, LinReplica, LinP>
	extends Store<Causal, CrossStore.CrossObject, CausalType, Void, CrossStore> {
	//private CausalStore causal;
	//private LinStore lin;

	private class ReadSetPair implements Serializable{
		{
			#define ReplicaID CausalReplica
			#define Nonce String
		}
		public final ReplicaID replica;
		public final Nonce operation_nonce;
		public ReadSetPair(final ReplicaID r, final Nonce s){
			replica = r;
			operation_nonce = s;
		}
	}

	private class MetaData implements Serializable{
		Set<ReadSetPair> readfrom;
		Nonce n;
		ReplicaID natural_replica;
		public final Ends ends;
		public MetaData(Set<ReadSetPair> readfrom, Nonce n, ReplicaID natural_replica, Ends ends){
			this.readfrom = readfrom; this.n = n; this.natural_replica = natural_replica; this.ends = ends;
		}
	}

	private class Tombstone implements Serializable {
		public final Nonce n;
		public final CausalType name;

		public Tombstone(Nonce n){
			this.n = n;
			this.name = this_store.concat(this_store.ofString("tombstone-"), this_store.ofString(n));
		}

	}

	private class Ends extends HashMap<CausalReplica, Timestamp> implements CausalSafe{
		public boolean prec(Ends e){

			Set<CausalReplica> crs = new TreeSet<>();
			crs.addAll(keySet());
			crs.addAll(e.keySet());
			
			for (CausalReplica cr : crs){
				Timestamp this_ts = get(cr);
				Timestamp e_ts = e.get(cr);
				if (!Timestamp.prec(this_ts,e_ts)) return false;
			}
			return true;
		}
		public Ends fast_forward(Ends future){
			for (CausalReplica cr : future.keySet()){
				put(cr,Timestamp.update(get(cr), future.get(cr)));
			}
			return this;
		}
	}

	final CausalStore this_store;

	Object to_return = null;
		
	public CrossStore(final CausalStore c, final LinStore l){
		this_store = c;
		//causal = c;
		//lin = l;

		final Ends ends = null;
		final Set<ReadSetPair> readset = new TreeSet<>();
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
					for (ReadSetPair rsp : readset){
						Timestamp t = ends.get(rsp.replica);
						assert(t != null);
						tstamp.put(rsp.replica,t);
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
						Set<ReadSetPair> rscopy = new TreeSet<>();
						rscopy.addAll(readset);
						rscopy.add(new ReadSetPair(natural_replica, n));
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
					if (!meta.ends.prec(ends)) {
						ends.fast_forward(meta.ends);
						if (!contains_tombstone(meta.n)){
							readset.addAll(meta.readfrom);
							c.sync_req(meta.natural_replica,
									   natural_replica);
						}
					}
					return null;
				}
			});

		c.registerOnRead(new Function<CausalType, Void>(){

				@SuppressWarnings("unchecked")
				private <T> Mergable<T>
					helper(Mergable<T> m, CausalType name) {
					for (ReadSetPair rsp : readset){
						try{
							Mergable<T> r = (Mergable<T>)
								c.access_replica(rsp.replica)
								.existingObject(name).get();
							if (m == null) m = r;
							else m = (Mergable<T>) m.merge(r);
						}
						catch(Exception e){
							throw new RuntimeException(e);
						}
					}
					return m;
				}
				
				@Override
				public Void apply(CausalType name){
					Mergable<?> m = null;
					to_return = helper(m, name);
					return null;
				}
			});

		c.registerOnWrite(new Function<CausalType, Void>(){
				@Override
				public Void apply(CausalType ct){
					ends.put(natural_replica, natural_replica.currentTime());
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
					final List<ReadSetPair> readset_new = new LinkedList<>();
					for (ReadSetPair rsp : readset){
						if (!contains_tombstone(rsp.operation_nonce))
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
			found = this_store.existingObject(new Tombstone(n).name).get();
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
