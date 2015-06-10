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

class ClientSimulator {
	SimpleNameManager snm = new SimpleNameManager();
	IncrementFactory incrfact = new IncrementFactory();

	
	
	public void run(){
		final IndirectStore<Causal, SafeInteger, SafeInteger> cross
			= new IndirectStore<>
			(new SimpleCausal());
		/*(new CrossStore<>
		  (new SimpleCausal(), snm,
		  FSStore.inst, FSStore.inst));*/
		cross.tick();
		Handle<SimpleCounter,consistency.Causal,access.ReadWrite,?,?> h;
		cassert(cross.objectExists((SafeInteger)hmaster.ro.name()),"failure: cross tick did not receive master object by name.");
		try{
			synchronized(hmaster){
				h = hmaster.getCopy(cross);
			}
		}
		catch(MyriaException e){
			h = null;
			System.err.println("failure: cross tick did not receive master object by name.");
			throw new RuntimeException(e);
		}
		
		assert((cross.newObject(new SimpleCounter(),
								SafeInteger.ofString(NonceGenerator.get()),
								cross)).ro.get() != null);
		
		incrfact.build(h).execute();
		incrfact.build(h).execute();
		(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(h)).execute();
		cross.tick();
	}
	
}
