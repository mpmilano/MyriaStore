package demo;

import operations.BaseNativeOperation1;
import operations.Get;
import operations.Put;
import remote.BackingStore;
import remote.RemoteObject;
import consistency.BaseModel;
import consistency.Linearizable;

public class LinearizableStoreObject<T> extends
		RemoteObject<T, Linearizable, BackingStore<Linearizable>> {
	
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

	//REALLY want a "super" keyword here.
	@Override
	public <T2> RemoteObject<T2, Linearizable, BackingStore<Linearizable>> generalize() {
		// TODO Auto-generated method stub
		return (RemoteObject<T2, Linearizable, BackingStore<Linearizable>>) this;
	}


}