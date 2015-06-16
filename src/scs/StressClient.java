package scs;

import remote.*;
import util.*;
import java.util.*;
import simplecausal.*;
import fsstore.*;
import operations.*;

public class StressClient{

	
	final CrossStore<SimpleCausal.SimpleRemoteObject<?>, SafeInteger, SafeInteger, SimpleCausal, FSStore.FSObject, String, java.net.InetAddress, FSStore> cross
		= 
		new CrossStore<>
		 (new SimpleCausal(), SimpleNameManager.inst,
		  FSStore.inst, FSStore.inst);

	final Handle<SimpleCounter, consistency.Lin, access.ReadWrite, ?,?> linObj;

	final Handle<SimpleCounter, consistency.Causal, access.ReadWrite, ?,?> causal1;
	final Handle<SimpleCounter, consistency.Causal, access.ReadWrite, ?,?> causal2;
	final Handle<SimpleCounter, consistency.Causal, access.ReadWrite, ?,?> causal3;

	final public int id;

	Random r = new Random();

	final private Function<Void,Void> syncAll;

	public StressClient(Handle<SimpleCounter, consistency.Lin, access.ReadWrite, ?,?> linObj,
						Function<Void,Void> syncAll){
		this.linObj = linObj;
		this.syncAll = syncAll;

		causal1 = cross.newObject(new SimpleCounter(), cross.genArg(), cross);
		causal2 = cross.newObject(new SimpleCounter(), cross.genArg(), cross);
		causal3 = cross.newObject(new SimpleCounter(), cross.genArg(), cross);
		id = cross.this_replica().toInt();
	}


	int tick_count = 0;
	public void tick(){
		ContextSwitcher.setContext(cross.this_replica());
		sync();
		syncAll.apply(null);
		System.out.print("switched to " +  id + "; time since sync: " + tick_count++ + ": ");
		int next = r.nextInt(100);
		if (next > 70){
			IncrementFactory.inst.build(causal1).execute();
		}
		else if (next > 40){
			IncrementFactory.inst.build(causal2).execute();
		}
		else if (next > 10){
			IncrementFactory.inst.build(causal3).execute();
		}
		else {
			IncrementFactory.inst.build(linObj).execute();
		}

		System.out.println();
	}

	public void sync(){
		cross.tick();
		tick_count = 0;
	}

}
