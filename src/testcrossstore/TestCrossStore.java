#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

package testcrossstore;

import remote.*;
import fsstore.*;
import logstore.*;
import simplecausal.*;
import operations.*;
import java.util.*;
import util.*;
import transactions.*;
import consistency.*;
import java.io.*;

public class TestCrossStore {

	String othername = "/tmp/fsstore/lin-test-obj";
	final Handle<SimpleCounter,consistency.Lin,access.ReadWrite,consistency.Lin,FSStore> linhandle =
		FSStore.inst.newObject(new SimpleCounter(),othername,FSStore.inst);

	private static Object synchlock = "";

	SafeInteger name = SafeInteger.ofString(NonceGenerator.get());


	//the "manager" interface - none of the testing operations are done here,
	//but we can use it to observe the state outside of the simulation class.

	/*
	<CausalP, LinObj extends RemoteObject, LinType, LinReplica, LinP>
	
					<Causal,
					  SimpleCausal.SimpleRemoteObject<?>,
					  , , SimpleCausal>														*/
	final CrossStore<SimpleCausal.SimpleRemoteObject<?>, SafeInteger, SafeInteger, SimpleCausal, FSStore.FSObject, String, java.net.InetAddress, FSStore> cross
						  = 
		(new CrossStore<>
		 (new SimpleCausal(), SimpleNameManager.inst,
		  FSStore.inst, FSStore.inst));

	
		/*(new CrossStore<>
		 (new SimpleCausal(), (new SimpleNameManager()),
		 FSStore.inst, FSStore.inst));*/
	Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,CrossStore<SimpleCausal.SimpleRemoteObject<?>,SafeInteger,SafeInteger,SimpleCausal,FSStore.FSObject,String,java.net.InetAddress,FSStore>> hmaster =
		cross.newObject(new SimpleCounter(),
						name,
						cross);

	Random rand = new Random();

	private void waitForEnter(){
		Scanner scan = new Scanner(System.in);
		scan.nextLine();
	}
	
	public TestCrossStore() {
		//need this to initially seed the "master" replica
		cross.tick();
		ClientSimulator t1 = new ClientSimulator(hmaster, linhandle);
		ClientSimulator t2 = new ClientSimulator(hmaster, linhandle);
		
		//the observer is slaved to this one.
		//new ClientSimulator(hmaster, linhandle);

		t1.tick();
		t2.tick();

		waitForEnter();
		
		while (!(t1.done() && t2.done())){
			if (true/*rand.nextBoolean()*/){
				t1.tick();
				waitForEnter();
				t2.tick();
			}
			else {
				t2.tick();
				t1.tick();
			}
			waitForEnter();

			//if (rand.nextBoolean()) t1.sync();
			//if (rand.nextBoolean()) t2.sync();
		}

		System.out.println("threads done");
		ContextSwitcher.setContext(cross.this_replica());
		IncrementFactory.inst.build(linhandle).execute();
		System.out.print("Lin object: ");
		(new PrintFactory<consistency.Lin>().build(Handle.readOnly(linhandle))).execute();
		System.out.print("Observer (context "+ cross.this_replica().toString() +") at " + hmaster.ro.name().toString() + ": ");
		(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(hmaster)).execute();
		t1.sync();
		t2.sync();
		cross.tick();
		(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(hmaster)).execute();
	}
}
