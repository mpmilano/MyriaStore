package remote;

import java.io.Serializable;
import java.util.*;
import consistency.*;

public abstract class Store<Cons extends consistency.Top, RObj extends RemoteObject, SType, Store_p>
	implements HasConsistency<Cons>, HasAccess<access.ReadWrite>, StoreCons<Cons>
{

	protected abstract <T extends Serializable> RObj newObject(SType arg, T init) throws Exception;
	protected abstract SType genArg();

	public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(T init, SType arg, Store<consistency.Lin,?,?,Store_p> s){
		assert(s == this);
		try {
			@SuppressWarnings("unchecked")
				RemoteObject<T> newobj = (RemoteObject<T>) newObject(arg,init);
			return new Handle<>(newobj);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public <T extends CausalSafe> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(T init, SType arg, Store<consistency.Causal,?,?,Store_p> s){
		assert(s == this);
		try {
			@SuppressWarnings("unchecked")
				RemoteObject<T> newobj = (RemoteObject<T>) newObject(arg,init);
			return new Handle<>(newobj);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}	

	public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(T init, Store<consistency.Lin,?,?,Store_p> s){
		return newObject(init,genArg(),s);
	}

	public <T extends CausalSafe> Handle<T, Cons, access.ReadWrite, Cons, Store_p>
		newObject(T init, Store<consistency.Causal,?,?,Store_p> s){
		return newObject(init,genArg(),s);
	}



	public abstract class AltObjFact<T extends Serializable, A extends access.Unknown, OHBSObj extends RObj> {
		protected Handle<T, Cons, A, Cons, Store_p> buildHandle(OHBSObj oh){
			@SuppressWarnings("unchecked")
			RemoteObject<T> newobj = (RemoteObject<T>) oh;
			return new Handle<>(newobj);
		}
	}

	//happens AFTER write
	public abstract void registerOnWrite(util.Function<SType,Void> r);

	//happens BEFORE read
	public abstract void registerOnRead(util.Function<SType,Void> r);

	private Random rand = new Random();
	public void registerOnTick(Runnable r){
		final Runnable rp = r;
		(new Thread(){
				@Override
				public void run(){
					try{
						Thread.sleep(rand.nextInt((10 - 0) + 1) + 0); 
					}
					catch(Exception e){}
					rp.run();
				}
			}).start();
	}


	//only constraint here is consistent mapping.
	//incorporating the string directly would be nice
	//for debugging though, if it's possible to do.
	public abstract SType ofString(String s);

	public abstract SType concat(SType a, SType b);

	public /*ops-only*/ void beginTransaction(){
		//TODO - make this do something.
	}
		
	public /*ops-only*/ void endTransaction(){
		//TODO - make this do something.
	}
	
}
