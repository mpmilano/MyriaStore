package vectorclockgc;

import remote.*;
import consistency.*;
import access.*;
import java.util.*;

private class Counter{
}

private class VectorClock{

}



public class Cliet<StrongStore, CausalStore, CStoreType>{
	final Store<consistency.Lin, ?, ?, StrongStore> strongStore;
	final Store<consistency.Causal, ? ,CStoreType ,CausalStore> causalStore;

	final ArrayList<Handle<VectorClock, consistency.Lin, access.ReadWrite, consistency.Lin, StrongStore> > global_clocks;
	final Handle<VectorClock, consistency.Lin, access.ReadWrite, consistency.Lin, StrongStore> my_global_contribution;

	final Counter myTime = new Counter(0);
	final VectorClock localClock = new VectorClock(myTime);
	final VectorClock global_estimate;
	final CStoreType tstampName = causalStore.ofString("timestamp"); //TODO - unwritable for normal users
	final CStoreType client_suffix = causalStore.ofString("unique-string-here"); //TODO - obvious

	private <T extends Serializable> Handle<T,Causal,ReadWrite,Causal,CausalStore> lookup(CStoreType arg){
		return causalStore.newObject(null,arg);
	}

	private <T extends Serializable> Handle<T,Causal,ReadWrite,Causal,CausalStore> lookup(CStoreType arg1, CStoreType arg2){
		return lookup(causalStore.concat(arg1,arg2));
	}
	private <T extends Serializable> Handle<T,Causal,ReadWrite,Causal,CausalStore> lookup(CStoreType arg1, CStoreType arg2, CStoreType arg3){
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
				ArrayList<VectorClock> cache = new ArrayList<VectorClock>();
				for (Handle<VectorClock, Lin, ReadWrite, Lin, StrongStore> h : global_clocks){
					cache.add((new GetOp<>(h)).execute());
				}
				global_estimate.advanceTo(VectorClock.meetOf(cache));
			}
		};
	
}
