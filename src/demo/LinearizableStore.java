package demo;

import consistency.Linearizable;
import operations.Get;
import operations.Put;
import remote.BackingStore;
import remote.RemoteObject;

public class LinearizableStore extends BackingStore<Linearizable> {

	public LinearizableStore(){
		super(Linearizable.model());
	}
	
	private class RObject<T> extends RemoteObject<T, Linearizable, BackingStore<Linearizable>> {
		
		public T t;
		
		public RObject(T t){
			super(LinearizableStore.this);
			this.t = t;
		}
	}
	
	@Override
	public <T> RemoteObject<T, Linearizable, BackingStore<Linearizable>> newObject(T t) {
		return new RObject<T>(t);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, M /*compat*/ extends consistency.BaseModel> T runOp(Get<T,M> op) {
		return ((RObject<T>) op.h.ro).t;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, M /*compat*/ extends consistency.BaseModel> Void runOp(Put<T, M> op) {
		((RObject<T>) op.h.ro).t = op.t;
		return null;
	}

}
