package operations;

import java.io.Serializable;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Print<T extends Serializable, StoreModel extends BaseModel, 
StoreWhere extends BackingStore<StoreModel,StoreWhere>,Model extends StoreModel, A extends ReadOnly> 
extends BaseNativeOperation1<Handle<?,?,?,?,?>, T, StoreModel, StoreWhere, Model, A> {

	public Print(Handle<T,A,Model,StoreModel,StoreWhere> h){
		super(h);
	}
	
	@Override
	public Handle<?,?,?,?,?> executeOn(BackingStore<StoreModel,StoreWhere>.RemoteObject<T> bs){
		return bs.runOp(this);
	}
    	
	@Override
	public Handle<?,?,?,?,?> noop() {
		throw new RuntimeException("You MUST SUPPORT Print on your store.");
	}

}
