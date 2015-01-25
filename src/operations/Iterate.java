package operations;

import java.io.Serializable;
import java.util.Set;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Iterate<T extends Serializable & Set<?>, StoreModel extends BaseModel, 
StoreWhere extends BackingStore<StoreModel,StoreWhere>,Model extends StoreModel, A extends ReadOnly> 
extends BaseNativeOperation1<Handle<?,?,?,?,?>, T, StoreModel, StoreWhere, Model, A> {

	public Iterate(Handle<T,A,Model,StoreModel,StoreWhere> set, Operation<?,?,?,?,?,?,?> op){
		super(set);
	}
	
}
