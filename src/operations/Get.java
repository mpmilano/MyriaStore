package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Get<T, StoreModel extends BaseModel<handles.access.Any>, Model extends StoreModel> extends BaseNativeOperation1<T, T, StoreModel, Model> {

	public <A extends ReadOnly> Get(Handle<T,A,Model,StoreModel,?> h){
		super(h);
	}
	@Override
	public T executeOn(BackingStore<StoreModel>.RemoteObject<T> bs) {
		return bs.runOp(this);
	}

	@Override
	public T noop() {
		// TODO Auto-generated method stub
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}


}
