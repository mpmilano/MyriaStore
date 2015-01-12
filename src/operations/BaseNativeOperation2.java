package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;
import remote.RemoteObject;

public abstract class BaseNativeOperation2<ReturnType, 
ObjectType1,ObjectType2,  
M extends BaseModel, O extends BaseModel,
S extends BackingStore<O, S>> implements BaseOperation<ReturnType,M>{

	public final Handle<ObjectType1,?,M,O,S> h1;
	public final Handle<ObjectType2,?,M,O,S> h2;

	public BaseNativeOperation2 (Handle<ObjectType1,?,M,O,S> h1, 
			Handle<ObjectType2,?,M,O,S> h2){
		this.h1 = h1;
		this.h2 = h2;
	}

	public abstract ReturnType 
	executeOn(RemoteObject<ObjectType1,O,S> bs1, RemoteObject<ObjectType2, O, S> bs2);

	@Override
	public ReturnType call() throws Exception {
		return execute();
	}

	@Override
	public ReturnType execute(){
		return executeOn(h1.ro,h2.ro);
	}

	public abstract ReturnType noop();

}
