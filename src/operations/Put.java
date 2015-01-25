package operations;

import java.io.Serializable;

import consistency.BaseModel;
import handles.access.WriteOnly;
import remote.BackingStore;
import remote.Handle;

public class Put<T extends Serializable, StoreModel extends BaseModel, StoreWhere extends BackingStore<StoreModel, StoreWhere>, Model extends StoreModel, A extends WriteOnly> 
extends BaseNativeOperation1<Void, T, StoreModel, StoreWhere, Model, A> {

	public final T t;
	
	public Put(Handle<T,A,Model,StoreModel,StoreWhere> h, T t){
		super(h);
		this.t = t;
	}
	
	@Override
	public Void executeOn(BackingStore<StoreModel, StoreWhere>.RemoteObject<T> bs) {
		bs.runOp(this);
		return null;
	}

	@Override
	public Void noop() {
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}
}
