package vectorclockgc;

import remote.*;
import consistency.*;
import access.*;
import java.util.*;
import java.io.*;
import util.*;
import operations.*;


public class Client<StrongStore, CausalStore, CStoreType>{

	private static class Counter implements Serializable{
		private int val = 0;
		public int get(){return val;}
		public int increment() { return ++val; }
		public void reset(Counter c) {this.val = c.val;}
		public Counter(int val){this.val = val;}

		public static Counter max(Counter a, Counter b){
			if (a == null) return b;
			if (b == null) return a;
			return a.val > b.val ? a : b;
		}
		
		public static Counter min(Counter a, Counter b){
			if (a == null || b == null) return null;
			return a.val > b.val ? b : a;
		}
	
	}
	
	private static class VectorClock<CStoreType> implements Serializable{
		HashMap<CStoreType, Counter> clock = new HashMap<>();
		CStoreType preset_uid = null;
		Counter preset_counter = null;

		
		public VectorClock(CStoreType uid, Counter tracks){
			this.preset_uid = uid;
			this.preset_counter = tracks;
		}

		public VectorClock(){}

		public Set<CStoreType> keys(){
			Set<CStoreType> ret =  clock.keySet();
			if (preset_uid != null) ret.add(preset_uid);
			return ret;
			
		}


		public Counter get(CStoreType s){
			if (preset_uid != null && preset_uid.equals(s))
				return preset_counter;
			else
				return clock.get(s);
		}

		public void put(CStoreType s, Counter c){
			if (preset_uid != null && preset_uid.equals(s))
				preset_counter.reset(c);
			else clock.put(s,c);
		}

		public VectorClock<CStoreType> advanceTo(VectorClock<CStoreType> o){

			if (o.preset_uid != null){
				put(o.preset_uid, Counter.max(o.preset_counter, get(o.preset_uid)));
			}
			for (Map.Entry<CStoreType, Counter> entry : o.clock.entrySet() ){
				put(entry.getKey(),Counter.max(entry.getValue(), get(entry.getKey())));
			}
			return this;
		}
		
		public static <CStoreType> VectorClock<CStoreType> meetOf(Collection<VectorClock<CStoreType> > vcs){
			VectorClock<CStoreType> meet = new VectorClock<CStoreType>();
			for (VectorClock<CStoreType> e : vcs){
				for (CStoreType s : e.keys()) {
					Counter meetcur = meet.get(s);
					meet.put(s, Counter.min(e.get(s), meetcur == null ? e.get(s) : meetcur));
				}
			}
			return meet;		
		}
	}

	
	final Store<consistency.Lin, ?, ?, StrongStore> strongStore;
	final Store<consistency.Causal, ? ,CStoreType ,CausalStore> causalStore;
	public Client(final Store<consistency.Lin, ?, ?, StrongStore> strongStore, 
				  final Store<consistency.Causal, ? ,CStoreType ,CausalStore> causalStore){
		this.strongStore = strongStore;
		this.causalStore = causalStore;
		this.tstampName = causalStore.ofString("timestamp"); //TODO - unwritable for normal users
		client_suffix = causalStore.ofString("unique-string-here"); //TODO - obvious
		localClock = new VectorClock<CStoreType>(client_suffix, myTime);
		
	}

	//TODO - this design requires construction-time knowledge of the full list of gloal clocks.  This seems like it is the sort of thing
	//that we want to allow to change over time.  Not too hard to change, but maybe midnight isn't the right time for it.
	final ArrayList<Handle<VectorClock<CStoreType>, consistency.Lin, access.ReadWrite, consistency.Lin, StrongStore> > global_clocks = new ArrayList<>();
	final Handle<VectorClock<CStoreType>, consistency.Lin, access.ReadWrite, consistency.Lin, StrongStore> my_global_contribution;

	final Counter myTime = new Counter(0);
	final VectorClock<CStoreType> global_estimate = new VectorClock<CStoreType>();
	final CStoreType tstampName;
	final CStoreType client_suffix;
	final VectorClock<CStoreType> localClock;

	private Handle<VectorClock<CStoreType>,Causal,ReadWrite,Causal,CausalStore> lookup(CStoreType arg){
		return causalStore.newObject(null,arg);
	}

	private Handle<VectorClock<CStoreType>,Causal,ReadWrite,Causal,CausalStore> lookup(CStoreType arg1, CStoreType arg2){
		return lookup(causalStore.concat(arg1,arg2));
	}
	private Handle<VectorClock<CStoreType>,Causal,ReadWrite,Causal,CausalStore>
		lookup(CStoreType arg1, CStoreType arg2, CStoreType arg3){
		return lookup(causalStore.concat(arg1,arg2),arg3);
	}
	

	Function<CStoreType,Void> onWrite = new Function<CStoreType,Void>(){
			@Override
			public Void apply(CStoreType objToWrite){
				myTime.increment();
				(new PutOp<>(lookup(objToWrite,tstampName), localClock)).execute();
				return null;
			}
		};

	Function<CStoreType,Void> onRead = new Function<CStoreType,Void>(){
			@Override
			public Void apply(CStoreType toRead){
				localClock.advanceTo((new GetOp<>(lookup(toRead,tstampName))).execute());
				return null;
			}
		};

	Runnable onTick = new Runnable(){
			@Override
			public void run(){
				(new PutOp<>(my_global_contribution,localClock)).execute();
				ArrayList<VectorClock<CStoreType>> cache = new ArrayList<VectorClock<CStoreType>>();
				for (Handle<VectorClock<CStoreType>, Lin, ReadWrite, Lin, StrongStore> h : global_clocks){
					cache.add((new GetOp<>(h)).execute());
				}
				global_estimate.advanceTo(VectorClock.meetOf(cache));
			}
		};
	
}
