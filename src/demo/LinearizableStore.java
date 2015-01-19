package demo;

import handles.access.ReadWrite;
import operations.Get;
import operations.Put;
import consistency.Linearizable;
import remote.BackingStore;

public class LinearizableStore extends BackingStore<Linearizable<ReadWrite>, LinearizableStore> {

	public LinearizableStore(){
		super(Linearizable.model());
	}

	public class LinearizableStoreObject<T> extends
			RemoteObject<T> {
		
		public T t;
		
		public LinearizableStoreObject(T t, LinearizableStore ls){
			super(ls);
			this.t = t;
		}

		@Override
		public <M extends Linearizable> T runOp(Get<T, Linearizable, M> op) {
			return t;
		}

		@Override
		public <M extends Linearizable> void runOp(Put<T, Linearizable, M> op) {
			t = op.t;
		}
		
		public void runOp(CustomOp c){
			System.out.println("custom fever!");
		}

		@Override
		protected T exposeRef() {
			return t;
		}

		@Override
		protected <T2> RemoteObject<T2> newRef(T2 t) {
			@SuppressWarnings("unchecked")
			RemoteObject<T2> thisp = (RemoteObject<T2>)(this);
			if (this.t == t) return thisp;
			else throw new RuntimeException("called from invalid context");
		}
	}
	
	@Override
	public <T> RemoteObject<T> newObject(T t) {
		return new LinearizableStoreObject<T>(t,this);
	}

}
