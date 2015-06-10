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

	private static Object synchlock = "";

	SafeInteger name = SafeInteger.ofString(NonceGenerator.get());

	//the "manager" interface - none of the testing operations are done here,
	//but we can use it to observe the state outside of the simulation class.
	IndirectStore<Causal, SafeInteger, SafeInteger> cross
		= new IndirectStore<>
		(new SimpleCausal());
		/*(new CrossStore<>
		 (new SimpleCausal(), (new SimpleNameManager()),
		 FSStore.inst, FSStore.inst));*/
	Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,IndirectStore<Causal, SafeInteger, SafeInteger>> hmaster =
		cross.newObject(new SimpleCounter(),
						name,
						cross);

	Random rand = new Random();
	
	public TestCrossStore() {
		try{

			//need this to initially seed the "master" replica
			cross.tick();
			ClientSimulator t1 = new ClientSimulator(hmaster);
			t1.tick();
			ClientSimulator t2 = new ClientSimulator(hmaster);
			t2.tick();
			while (!(t1.done() && t2.done())){
				if (rand.nextBoolean()){
					t1.tick();
					t2.tick();
				}
				else {
					t2.tick();
					t1.tick();
				}
			}
			System.out.println("threads done");
			cross.tick();
			(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(hmaster)).execute();
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}