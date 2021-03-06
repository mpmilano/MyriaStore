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
	final SimpleNameManager snm = SimpleNameManager.inst;
	final IncrementFactory incrfact = IncrementFactory.inst;
	final CrossStore<SimpleCausal.SimpleRemoteObject<?>, SafeInteger, SafeInteger, SimpleCausal, FSStore.FSObject, String, java.net.InetAddress, FSStore> cross
		= 
		(new CrossStore<>
		 (new SimpleCausal(), SimpleNameManager.inst,
		  FSStore.inst, FSStore.inst));

	final Handle<SimpleCounter,consistency.Causal,access.ReadWrite,?,?> h;

	public LinkedList<Runnable> statements = new LinkedList<>();
	final private int clientID;
	final private SafeInteger orig_clientID;
	
	public ClientSimulator(final Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,CrossStore<SimpleCausal.SimpleRemoteObject<?>, SafeInteger, SafeInteger, SimpleCausal, FSStore.FSObject, String, java.net.InetAddress, FSStore>> h1,
						   final Handle<SimpleCounter,consistency.Lin,access.ReadWrite,consistency.Lin,FSStore> linhandle){
		this.orig_clientID = cross.this_replica();
		this.clientID = this.orig_clientID.toInt();
		ContextSwitcher.setContext(cross.this_replica());
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
		statements.add(thunk(System.out.print("Context " + clientID + ": client at " + h.ro.name().toString() + ": " );
							 (new PrintFactory<consistency.Causal>()).build(Handle.readOnly(h)).execute()));
		statements.add(thunk(assert(clientID == cross.this_replica().toInt())));
		statements.add(thunk(incrfact.build(linhandle).execute()));
		
		/*statements.add(new Runnable(){
				@Override
				public void run(){
				}
				}); */
	}
	
	public void tick(){
		ContextSwitcher.setContext(orig_clientID);
		System.out.print(clientID + ": ");
		statements.remove().run();
		System.out.println("");
	}

	public void sync(){
		cross.tick();
	}

	public boolean done(){
		return statements.isEmpty();
	}
	
}
