package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

#define cassert(x,s) assert((new Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));
#define CausalStoreParams Causal, CausalObj, CausalType, CReplicaID, CausalP
#define CausalStore Store<CausalStoreParams>
#define LinStore Store<Lin, LinObj, LinType, LinReplica, LinP>
#define CrossStoreT CrossStore<CausalObj, CausalType, CReplicaID, CausalP, LinObj, LinType, LinReplica, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType extends Serializable & Comparable<CausalType> & Mergable<CausalType> & RCloneable<CausalType>, CReplicaID extends CausalSafe<CReplicaID> & Comparable<CReplicaID>, CausalP, LinObj extends RemoteObject, LinType, LinReplica, LinP>
	extends Store<Causal, CrossStore.CrossObject, CausalType, CReplicaID, CrossStoreT> {
	//private CausalStore causal;
	//private LinStore lin;


	#define ReplicaID CReplicaID
	#define Nonce String

	#include "Metadata.h"		

	private final CausalType tombstone_word;
	private final CausalType tombstone_name(String s){
		return cnm.concat(tombstone_word,
						  cnm.ofString(s));
	}

	#define Tombstone CausalSafeStringPair

	//cross-store tracking
	private final Set<Pair<ReplicaID, Nonce>> readset = new ConcurrentSkipListSet<>();
	private final Ends<CReplicaID> ends = new Ends<CReplicaID>();

	//where to find metadata
	private final CausalType meta_name;
	private final LinType metadata_suffix;

	//Store functions 
	private final CausalStore this_store;
	private final ReplicaID natural_replica;
	private final AccessReplica<CausalStoreParams> replica_map;
	private final HasClock timer;
	private final NameManager<CausalType> cnm;
	private final NameManager<LinType> lnm;

		
	public <CS extends CausalStore &
					   HasClock &
					   AccessReplica
					   <Causal, CausalObj, CausalType, CReplicaID, CausalP>&
					   Synq<CReplicaID> >
		CrossStore(final CS c, final NameManager<CausalType> cnm,
				   final LinStore l, final NameManager<LinType> lnm){
		this_store = c;
		replica_map = c;
		timer = c;
		this.cnm = cnm;
		this.lnm = lnm;
		meta_name = cnm.ofString("metameta-");
		natural_replica = this_store.this_replica();
		metadata_suffix = lnm.ofString("-metadata");

		l.registerOnWrite(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					if (ContextSwitcher.testContext(this_replica())){
					if (!is_metadata(name)){
						Nonce n = NonceGenerator.get();
						{
							LinType meta_name =
								lnm.concat(name,metadata_suffix);
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
						final Tombstone<CausalType> t = new Tombstone<>(n, tombstone_name(n));
						try {
							CrossStore.this.newObject(t.second, t);
						} catch(final MyriaCompatException e){
							cassert(e.check(t),"Tombstone<CausalType> is not compatible! What?!");
							throw new RuntimeException("compat error thrown, but class is compatible!");
						}
					}
					}
					return null;
				}
			});
		
		l.registerOnRead(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					if (ContextSwitcher.testContext(this_replica())){
						if (!is_metadata(name)){
							LinType meta_name =
								lnm.concat(name,metadata_suffix);
							@SuppressWarnings("unchecked")
								MetaData meta = (l.objectExists(meta_name) ?
												 (MetaData) l.<MetaData>existingObject(meta_name).get() :
												 null);
							
							if(meta != null)
								if (!MD_ends(meta).prec(ends)) {
									ends.fast_forward(MD_ends(meta));
									if (!contains_tombstone(MD_n(meta))){
										readset.addAll(MD_readfrom(meta));
										c.sync_req(MD_natural_replica(meta),
												   natural_replica);
									}
								}
						}
					}
					return null;
				}
			});


		c.registerOnTick(new Runnable(){
				
				@Override
				public void run(){
					final List<Pair<ReplicaID, Nonce>> readset_new = new LinkedList<>();
					for (Pair<ReplicaID, Nonce> rsp : readset){
						if (!contains_tombstone(rsp.second))
							readset_new.add(rsp);
					}
					readset.clear();
					readset.addAll(readset_new);
				}
			});
		tombstone_word = cnm.ofString("tombstone-");
	}

	private boolean is_metadata(LinType lt){
		return lnm.taggedWith(lt,metadata_suffix);
	}
	
	private boolean contains_tombstone(Nonce n){
		synchronized(this_store){
			return this_store.objectExists(tombstone_name(n));
		}
	}

	//Necessary overhead to make the above work - mostly filling in abstract methods with the obvious stuff.


	@SuppressWarnings("unchecked")
	private <T extends CausalSafe<T>> CrossObject newObject_impl(CausalType arg, T init) {
		CausalType metaname = cnm.concat(meta_name,arg);
		synchronized(this_store){
			this_store.newObject(generate_casual_meta(), metaname, this_store);
			return new CrossObject<T>((CausalObj)this_store.newObject(init,arg,this_store).ro);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> CrossObject newObject(CausalType arg, final T init) throws util.MyriaCompatException{
		if (! (init instanceof CausalSafe<?>))
			throw new MyriaCompatException("Attempt to construct a new object which is not causal safe!",CausalSafe.class);
		return newObject_impl(arg, (CausalSafe) init);
	}

	private <T extends CausalSafe<T>> CrossObject newObject_impl(CausalType arg){
		CausalType metaname = cnm.concat(meta_name,arg);
		synchronized(this_store){
			this_store.newObject(generate_casual_meta(), metaname, this_store);
			return new CrossObject<T>(this_store.existingObject2(arg));
		}
	}
	
	@Override
	protected <T extends Serializable> CrossObject newObject(CausalType arg){
		return newObject_impl(arg);
	}

	

	public class CrossObject<T extends CausalSafe<T> >
		extends RemoteObject<T, CausalType>{

		final RemoteObject<T, CausalType> real;

		public CrossObject(final CausalObj real){
			@SuppressWarnings("unchecked")
				RemoteObject<T,CausalType> coerce =
				(RemoteObject<T, CausalType>) real;
			this.real = coerce;
		}

		@Override
		public CrossStore getStore(){
			return CrossStore.this;
		}


		@Override
		public CausalType name(){
			return real.name();
		}		
		
		@Override
		public void put(final T t){
			synchronized(this_store){
				final CausalType ct = real.name();
				ends.put(natural_replica, timer.currentTime());
				CausalType metaname = cnm.concat(meta_name,ct);
				this_store.newObject(generate_casual_meta(), metaname, this_store);
				real.put(t);
			}
		}

		private <T extends Mergable<T> > T helper(T m, final CausalType name) {
			boolean once = false;
			assert(m != null);
			for (final Pair<ReplicaID, Nonce> rsp : readset){
				if (!replica_map.access_replica(rsp.first).objectExists(name))
					continue;
				
				@SuppressWarnings("unchecked")
					Class<? extends T> cls = (Class<? extends T>) m.getClass();
				T r = cls.
					cast(replica_map.access_replica(rsp.first)
						 .existingObject2(name).get());
				m = m.merge(r);
				once = true;
			}
			if (!once){
				synchronized(this_store){
					cassert(this_store.objectExists(name),cnm.toString(name) + " is not contained in the local replica!");
				}
				cassert(replica_map.access_replica(natural_replica).objectExists(name),cnm.toString(name) +
						" is not contained in the local replica when accessed via access_replica!");
			}
			{
				final T mt = m;
				cassert(mt != null,"result of merge is null!");
			}
			return m;
		}

		private int readset_reps(Set<Pair<ReplicaID, Nonce>> rs){
			TreeSet<ReplicaID> effective_size = new TreeSet<>();
			for (Pair<ReplicaID, Nonce> p : rs){
				effective_size.add(p.first);
			}
			return effective_size.size();
		}
				
		T doGet(CausalType ct){
			T m = real.get();
			T to_return = helper(m, ct);
			ends.fast_forward(helper(ends,cnm.concat(meta_name,ct)));
			System.out.println("Readset size: " + readset.size() + " # replicas hitting: " + readset_reps(readset));
				
			return to_return;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public T get(){
			return doGet(real.name());
		}
		
	}

	private Ends<CReplicaID> generate_casual_meta () {
		Ends<CReplicaID> tstamp = new Ends<CReplicaID>();
		for (Pair<ReplicaID, Nonce> rsp : readset){
			final Timestamp t = ends.get(rsp.first);
			if (t != null) tstamp.put(rsp.first,t);
		}
		final Timestamp t = ends.get(natural_replica);
		if (t != null) tstamp.put(natural_replica,t);
		return tstamp;
	}

	
	//Dummies and passthroughs

	@Override
	protected boolean exists(CausalType ct){
		synchronized(this_store){
			return this_store.objectExists(ct);
		}
	}

	@Override
	public CReplicaID this_replica() { return this_store.this_replica(); }

	@Override
	public void registerOnRead(Function<CausalType, Void> f){
		//TODO - maybe black-hole? unimplemented?
		synchronized(this_store){
			this_store.registerOnRead(f);
		}
	}

	@Override
	public void registerOnWrite(Function<CausalType, Void> f){
		synchronized(this_store){
			this_store.registerOnWrite(f);
		}
	}

	@Override
	public void registerOnTick(Runnable r){
		synchronized(this_store){
			this_store.registerOnTick(r);
		}
	}

	@Override
	public void tick(){
		synchronized(this_store){
			this_store.tick();
		}
	}

	@Override
	public CausalType genArg(){
		synchronized(this_store){
			return this_store.genArg();
		}
	}

	
		
}
