package operations;

import consistency.BaseModel;
import remote.Handle;
import remote.RemoteObject;

public abstract class BaseNativeOperation1<ReturnType, 
	ObjectType, 
	M extends BaseModel> implements BaseOperation<ReturnType,M>{

	public final Handle<ObjectType,?,M,?> h;
	
	public BaseNativeOperation1 (Handle<ObjectType,?,M,?> h){
		this.h = h;
	}
	
	public abstract <OtherModel /*compat*/ extends BaseModel> ReturnType executeOn(RemoteObject<ObjectType,OtherModel,?> bs);
		
	@Override
	public ReturnType call() throws Exception {
		// TODO Auto-generated method stub
		return execute();
	}
	
	@Override
	public ReturnType execute(){
		return executeOn(h.ro);
	}
	
	public abstract ReturnType noop();
	
}
