package demo;

import operations.Get;
import operations.Put;
import consistency.BaseModel;
import consistency.Linearizable;
import remote.BackingStore;
import remote.RemoteObject;

public class LinearizableStore extends BackingStore<Linearizable, LinearizableStore> {

	public LinearizableStore(){
		super(Linearizable.model());
	}

	public class LinearizableStoreObject<T> extends
			RemoteObject<T, Linearizable, LinearizableStore> {
		
		public T t;
		
		public LinearizableStoreObject(T t, LinearizableStore ls){
			super(ls);
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
		
		public void runOp(CustomOp c){
			System.out.println("custom fever!");
		}

	}
	
	@Override
	public <T> RemoteObject<T, Linearizable, LinearizableStore> newObject(T t) {
		return new LinearizableStoreObject<T>(t,this);
	}

}
