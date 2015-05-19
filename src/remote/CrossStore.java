package remote;
import consistency.*;
import util.*;
import java.io.*;
import java.util.*;

#define cassert(x,s) assert((new Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));
#define CausalStoreParams Causal, CausalObj, CausalType, CReplicaID, CausalP
#define CausalStore Store<CausalStoreParams>
#define LinStore Store<Lin, LinObj, LinType, LinReplica, LinP>
#define CrossStoreT CrossStore<CausalObj, CausalType, CReplicaID, CausalP, LinObj, LinType, LinReplica, LinP>
	
public class CrossStore<CausalObj extends RemoteObject, CausalType, CReplicaID extends CausalSafe<CReplicaID> & Comparable<CReplicaID>, CausalP, LinObj extends RemoteObject, LinType, LinReplica, LinP>
	extends Store<Causal, CrossStore.CrossObject, CausalType, Void, CrossStoreT> {
	//private CausalStore causal;
	//private LinStore lin;


	#define ReplicaID CReplicaID
	#define Nonce String

	#include "Metadata.h"
	#include "Tombstone.h"
	#include "Ends.h"

	private Object to_return = null;

	//cross-store tracking
	private final Set<Pair<ReplicaID, Nonce>> readset = new TreeSet<>();
	private final Ends ends = new Ends();

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
		tombstone_word = cnm.ofString("tombstone-");
		meta_name = cnm.ofString("metameta-");
		natural_replica = this_store.this_replica();
		metadata_suffix = lnm.ofString("metadata");

		l.registerOnWrite(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
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
					try {
						Tombstone t = new Tombstone(n);
						newObject(t.name, t);
					} catch(MyriaException e){
						throw new RuntimeException(e);
					}
					return null;
				}
			});
		
		l.registerOnRead(new Function<LinType,Void>(){
				@Override
				public Void apply(LinType name){
					LinType meta_name =
						lnm.concat(name,metadata_suffix);
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
		return this_store.objectExists(tombstone_name(n));
	}

	//Necessary overhead to make the above work - mostly filling in abstract methods with the obvious stuff.
	
	@Override
	protected <T extends Serializable> CrossObject newObject(CausalType arg, T init) throws util.MyriaException{
		//TODO - will assume metameta exists on get, so need to create it here!
		return new CrossObject<T>(this_store.newObject(arg,init));
	}

	@Override
	protected <T extends Serializable> CrossObject newObject(CausalType arg) throws util.MyriaException{
		//TODO - will assume metameta exists on get, so need to create it here!
		return new CrossObject<T>(this_store.newObject(arg));
	}

	

	public class CrossObject<T extends Serializable>
		implements RemoteObject<T, CausalType>{

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

		
		private Ends generate_casual_meta () {
			Ends tstamp = new Ends();
			for (Pair<ReplicaID, Nonce> rsp : readset){
				Timestamp t = ends.get(rsp.first);
				assert(t != null);
				tstamp.put(rsp.first,t);
			}
			return tstamp;
		}
		
		@Override
		public void put(final T t){
			final CausalType ct = real.name();
			ends.put(natural_replica, timer.currentTime());
			CausalType metaname = cnm.concat(meta_name,ct);
			this_store.newObject(generate_casual_meta(), metaname, this_store);
			real.put(t);
		}

		@SuppressWarnings("unchecked")
		private <T> T
			helper(Mergable<T> mp, final CausalType name) {
			@SuppressWarnings("unchecked")
				T m = (T) mp;
			boolean once = false;
			for (final Pair<ReplicaID, Nonce> rsp : readset){
				try{
					if (!replica_map.access_replica(rsp.first).objectExists(name))
						continue;
					T r = (T)
						replica_map.access_replica(rsp.first)
						.existingObject(name).get();
					if (m == null) m = r;
					else m = (T) mp.merge(r);
					once = true;
				}
				catch(MyriaException e){
					throw new RuntimeException(e);
				}
			}
			if (!once){
				cassert(this_store.objectExists(name),cnm.toString(name) + " is not contained in the local replica!");
				cassert(replica_map.access_replica(natural_replica).objectExists(name),cnm.toString(name) +
						" is not contained in the local replica when accessed via access_replica!");
			}
			{
				final boolean oncet = once;
				cassert(oncet,"There are no elements in readset which contain " + cnm.toString(name) + "!");
				final T mt = m;
				cassert(mt != null,"result of merge is null!");
			}
			return m;
		}
				
		public void doGet(CausalType ct){
			Mergable<?> m = null;
			to_return = helper(m, ct);
			Ends e = null;
			ends.fast_forward(helper(e,cnm.concat(meta_name,ct)));
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public T get(){
			doGet(real.name());
			assert(to_return != null);
			return (T) to_return;
		}
		
	}

	//Dummies and passthroughs

	@Override
	protected boolean exists(CausalType ct){
		return this_store.objectExists(ct);
	}

	@Override
	public Void this_replica() { return null; }

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
