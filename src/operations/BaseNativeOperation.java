package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;
import remote.RemoteObject;

public abstract class BaseNativeOperation<T, M extends BaseModel> implements BaseOperation<T,M>{

	public final Handle<?,?,M,?> h;
	
	public BaseNativeOperation (Handle<?,?,M,?> h){
		this.h = h;
	}
	
	public abstract <OtherModel /*compat*/ extends BaseModel> T executeOn(RemoteObject<?,OtherModel,?> bs);
		
	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return execute();
	}
	
	@Override
	public T execute(){
		return executeOn(h.ro);
	}
	
	public abstract T noop();
	
}
