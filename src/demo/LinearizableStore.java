package demo;

import consistency.BaseModel;
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

		@Override
		public <M extends BaseModel> T runOp(Get<T, M> op) {
			return t;
		}

		@Override
		public <M extends BaseModel> void runOp(Put<T, M> op) {
			t = op.t;
		}

	}
	
	@Override
	public <T> RemoteObject<T, Linearizable, BackingStore<Linearizable>> newObject(T t) {
		return new RObject<T>(t);
	}

}
