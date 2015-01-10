package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation<T, M extends BaseModel> implements BaseOperation<T,M>{

	public final Handle<?,?,M,?> h;
	
	public BaseNativeOperation (Handle<?,?,M,?> h){
		this.h = h;
	}
	
	public abstract <OtherModel /*compat*/ extends BaseModel> T executeOn(BackingStore<OtherModel> bs);
		
	@Override
	public T call() throws Exception {
		// TODO Auto-generated method stub
		return execute();
	}
	
	@Override
	public T execute(){
		return executeOn(h.ro.store);
	}
	
	public abstract T noop();
	
}
