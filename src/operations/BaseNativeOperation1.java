package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation1<ReturnType, 
	ObjectType,
	StoreAt extends BaseModel,
	ExecuteAt /*compat */ extends StoreAt> implements Operation<ReturnType,ExecuteAt>{

	public final Handle<ObjectType,?,ExecuteAt,StoreAt,?> h;
	
	public BaseNativeOperation1 (Handle<ObjectType,?,ExecuteAt,StoreAt,?> h){
		this.h = h;
	}
	
	public abstract  
	ReturnType executeOn(BackingStore<StoreAt>.RemoteObject<ObjectType> bs);
		
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
