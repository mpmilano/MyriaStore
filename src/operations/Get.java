package operations;

import java.io.Serializable;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Get<T extends Serializable, StoreModel extends BaseModel, 
StoreWhere extends BackingStore<StoreModel,StoreWhere>,Model extends StoreModel, A extends ReadOnly> 
extends BaseNativeOperation1<T, T, StoreModel, StoreWhere, Model, A> {

	public  Get(Handle<T,A,Model,StoreModel,StoreWhere> h){
		super(h);
	}

	@Override
	public T executeOn(BackingStore<StoreModel,StoreWhere>.RemoteObject<T> bs){
		return bs.runOp(this);
	}
    	
	@Override
	public T noop() {
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}


}
