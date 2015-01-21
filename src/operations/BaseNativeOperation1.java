package operations;

import java.io.Serializable;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation1<ReturnType, 
	ObjectType extends Serializable,
	StoreAt extends BaseModel,
	StoreWhere extends BackingStore<StoreAt, StoreWhere>,
	ExecuteAt /*compat */ extends StoreAt> implements Operation<ReturnType,ExecuteAt>{

	public final Handle<ObjectType,?,ExecuteAt,StoreAt,StoreWhere> h;
	
	public BaseNativeOperation1 (Handle<ObjectType,?,ExecuteAt,StoreAt,StoreWhere> h){
		this.h = h;
	}
	
	public abstract  
	ReturnType executeOn(BackingStore<StoreAt,StoreWhere>.RemoteObject<ObjectType> bs);
		
	@Override
	public ReturnType call() throws Exception {
		return execute();
	}
	
	@Override
	public ReturnType execute(){
		return executeOn(h.ro);
	}
	
	public abstract ReturnType noop();
	
}
