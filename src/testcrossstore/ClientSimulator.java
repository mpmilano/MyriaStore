#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

#define thunk(x) (new Runnable(){ @Override public void run(){ x; }})

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
	final SimpleNameManager snm = new SimpleNameManager();
	final IncrementFactory incrfact = new IncrementFactory();
	final IndirectStore<Causal, SafeInteger, SafeInteger> cross
		= new IndirectStore<>
		(new SimpleCausal());
	final Handle<SimpleCounter,consistency.Causal,access.ReadWrite,?,?> h;

	public LinkedList<Runnable> statements = new LinkedList<>();
	
	public ClientSimulator(final Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,IndirectStore<Causal, SafeInteger, SafeInteger>> h1){
		cross.tick();
		cassert(cross.objectExists((SafeInteger)h1.ro.name()),"failure: cross tick did not receive master object by name.");
		Handle<SimpleCounter,consistency.Causal,access.ReadWrite,?,?> htmp = null;
		try{
			htmp = h1.getCopy(cross);
		}
		catch(MyriaException e){
			System.err.println("failure: cross tick did not receive master object by name.");
			throw new RuntimeException(e);
		}
		finally {
			this.h = htmp;
		}

		statements.add(new Runnable(){
				@Override
				public void run(){
					cassert((cross.newObject
							 (new SimpleCounter(),
							  SafeInteger.ofString(NonceGenerator.get()),
							  cross)).ro.get() != null, "Cross fails to retrieve newly-created object");
				}
			});

		statements.add(thunk(incrfact.build(h).execute()));
		statements.add(thunk(incrfact.build(h).execute()));
		statements.add(thunk((new PrintFactory<consistency.Causal>()).build(Handle.readOnly(h)).execute()));
		statements.add(new Runnable(){
				@Override
				public void run(){
				}
			});
	}
	
	public void tick(){
		/*(new CrossStore<>
		  (new SimpleCausal(), snm,
		  FSStore.inst, FSStore.inst));*/
		cross.tick();
		statements.remove().run();
		cross.tick();
	}

	public boolean done(){
		return statements.isEmpty();
	}
	
}
