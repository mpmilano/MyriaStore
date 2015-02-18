package remote;

import java.io.Serializable;

public abstract class Store<Cons extends consistency.Top, RObj extends RemoteObject, SType, Store_p>
	implements HasConsistency<Cons>, HasAccess<access.ReadWrite>, StoreCons<Cons>
{

	protected abstract <T extends Serializable> RObj newObject(SType arg, T init) throws Exception;
	protected abstract SType genArg();

	public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p> newObject(T init, SType arg){
		try {
			@SuppressWarnings("unchecked")
				RemoteObject<T> newobj = (RemoteObject<T>) newObject(arg,init);
			return new Handle<>(newobj);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public <T extends Serializable> Handle<T, Cons, access.ReadWrite, Cons, Store_p> newObject(T init){
		return newObject(init,genArg());
	}


	public abstract class AltObjFact<T extends Serializable, A extends access.Unknown, OHBSObj extends RObj> {
		protected Handle<T, Cons, A, Cons, Store_p> buildHandle(OHBSObj oh){
			@SuppressWarnings("unchecked")
			RemoteObject<T> newobj = (RemoteObject<T>) oh;
			return new Handle<>(newobj);
		}
	}

	public /*ops-only*/ void beginTransaction(){
		//TODO - make this do something.
	}
		
	public /*ops-only*/ void endTransaction(){
		//TODO - make this do something.
	}
	
}
