package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation2<ReturnType, 
Constraint, ObjectType1 extends Constraint, ObjectType2 extends Constraint,  
M extends BaseModel, O extends BaseModel,
S extends BackingStore<O,Constraint,S>> implements Operation<ReturnType,M>{

	public final Handle<Constraint, ObjectType1,?,M,O,S> h1;
	public final Handle<Constraint, ObjectType2,?,M,O,S> h2;

	public BaseNativeOperation2 (Handle<Constraint, ObjectType1,?,M,O,S> h1, 
			Handle<Constraint, ObjectType2,?,M,O,S> h2){
		this.h1 = h1;
		this.h2 = h2;
	}

	public abstract ReturnType 
	executeOn(BackingStore<O,Constraint, S>.RemoteObject<ObjectType1> bs1, 
			BackingStore<O,Constraint, S>.RemoteObject<ObjectType2> bs2);

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
