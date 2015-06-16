package remote;

import java.io.Serializable;
import java.util.*;
import consistency.*;
import util.*;

#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

public abstract class Store<Cons extends consistency.Top,
										 RObj extends RemoteObject,
													  SType, RType, Store_p>
	implements HasConsistency<Cons>, HasAccess<access.ReadWrite>, StoreCons<Cons>
{

	protected abstract <T extends Serializable> RObj
		newObject(SType arg, T init) throws util.MyriaException;

	//for referencing existing objects by name.  Only for use within framework.
	synchronized <T extends Serializable>
		RObj existingObject(final SType arg) {
			cassert(arg != null, "attempt to reference existing object with null name");
			cassert(exists(arg),"attempt to build non-existing object! please check if exists first.");

			//TODO: propogate this pattern everywhere
			try{
				@SuppressWarnings("unchecked")
					RObj specific = newObject(arg);
				return specific;
			}
			catch(util.MyriaException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

	//for referencing existing objects by name.  Only for use within framework.
	synchronized 
		RObj existingObject2(final SType arg)  {
			cassert(arg != null, "attempt to reference existing object with null name");
			cassert(exists(arg),"attempt to build non-existing object! please check if exists first.");
			try{
				return newObject(arg);
			}
			catch(util.MyriaException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}

	
	protected abstract boolean exists(SType arg);
	

	public boolean objectExists(final SType arg) {
		cassert(arg != null, "attempt to query existing object with null name");
		return exists(arg);
	}
	
	protected abstract <T extends Serializable>
		RObj newObject(SType arg) throws util.MyriaException;
	
	protected abstract SType genArg();

	synchronized public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(final T init, final SType arg, Store<consistency.Lin,?,?,?,Store_p> s){
		assert(s == this);
		cassert(init != null, "attempt to construct lin object with null initial value");
		cassert(arg != null, "attempt to construct lin object with null name");
		try {
			@SuppressWarnings("unchecked")
				RemoteObject<T,SType> newobj = (RemoteObject<T,SType>) newObject(arg,init);
			return new Handle<>(newobj);
		}
		catch (util.MyriaException e){
			throw new RuntimeException(e);
		}
	}

	synchronized public <T extends CausalSafe<T>> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(final T init, final SType arg, Store<consistency.Causal,?,?,?,Store_p> s){
		assert(s == this);
		cassert(init != null, "attempt to construct causal object with null initial value");
		cassert(arg != null, "attempt to construct causal object with null name");
		try {
			@SuppressWarnings("unchecked")
				RemoteObject<T,SType> newobj = (RemoteObject<T,SType>) newObject(arg,init);
			return new Handle<>(newobj);
		}
		catch (util.MyriaException e){
			throw new RuntimeException(e);
		}
	}	

	synchronized public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(final T init, Store<consistency.Lin,?,?,?,Store_p> s){
		cassert(init != null, "attempt to initialize lin object with null!");
		return newObject(init,genArg(),s);
	}

	synchronized public <T extends CausalSafe<T>> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(final T init, Store<consistency.Causal,?,?,?,Store_p> s){
		cassert(init != null, "attempt to initialize causal object with null!");
		return newObject(init,genArg(),s);
	}



	public abstract class AltObjFact<T extends Serializable,
											   A extends access.Unknown,
														 OHBSObj extends RObj> {
		protected Handle<T, Cons, A, Cons, Store_p> buildHandle(OHBSObj oh){
			@SuppressWarnings("unchecked")
			RemoteObject<T,SType> newobj = (RemoteObject<T,SType>) oh;
			return new Handle<>(newobj);
		}
	}

	//happens AFTER write
	public abstract void registerOnWrite(util.Function<SType,Void> r);

	//happens BEFORE read
	public abstract void registerOnRead(util.Function<SType,Void> r);

	private Random rand = new Random();
	private List<Runnable> rl = new LinkedList<>();
	public void registerOnTick(Runnable r){
		rl.add(r);
	}

	public void tick(){
		for (Runnable r : rl) r.run();
	}

	public 	synchronized /*ops-only*/ void beginTransaction(){
		//TODO - make this do something.
	}
		
	public 	synchronized /*ops-only*/ void endTransaction(){
		//TODO - make this do something.
	}

	public abstract RType this_replica();

}
