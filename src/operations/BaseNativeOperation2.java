package operations;

import java.io.Serializable;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation2<ReturnType, 
ObjectType1 extends Serializable, ObjectType2 extends Serializable,  
O extends BaseModel, S extends BackingStore<O,S>, M extends O> 
implements Operation<ReturnType,M>{

	public final Handle<ObjectType1,?,M,O,S> h1;
	public final Handle<ObjectType2,?,M,O,S> h2;

	public BaseNativeOperation2 (Handle<ObjectType1,?,M,O,S> h1, 
			Handle<ObjectType2,?,M,O,S> h2){
		this.h1 = h1;
		this.h2 = h2;
	}

	public ReturnType 
	executeOn(BackingStore<O,S>.RemoteObject<ObjectType1> bs1, 
			BackingStore<O,S>.RemoteObject<ObjectType2> bs2){
		return bs1.runOp(this, bs2);
	}

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
