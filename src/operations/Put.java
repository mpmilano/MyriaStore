package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Put<T, StoreModel extends BaseModel, StoreWhere extends BackingStore<StoreModel, StoreWhere>, Model extends StoreModel> 
extends BaseNativeOperation1<Void, T, StoreModel, StoreWhere, Model> {

	public final T t;
	
	public <A extends ReadOnly> Put(Handle<T,A,Model,StoreModel,StoreWhere> h, T t){
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
