package demo;

import operations.Get;
import operations.Put;
import consistency.Linearizable;
import remote.BackingStore;

public class LinearizableStore extends BackingStore<Linearizable<handles.access.Any>> {

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
		public <M extends Linearizable<handles.access.Any>> T runOp(Get<T, Linearizable<handles.access.Any>, M> op) {
			return t;
		}

		@Override
		public <M extends Linearizable<handles.access.Any>> void runOp(Put<T, Linearizable<handles.access.Any>, M> op) {
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
			//used in a (very) safe way.
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
