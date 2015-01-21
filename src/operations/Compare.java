package operations;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public class Compare<ObjectType1 extends Comparable<?>,
M extends BaseModel, O extends BaseModel,
S extends BackingStore<O,Comparable<?>,S>> extends
BaseNativeOperation2<Integer, Comparable<?>, ObjectType1, ObjectType1, M, O, S>
{


	public Compare (Handle<Comparable<?>,ObjectType1,?,M,O,S> h1, 
			Handle<Comparable<?>,ObjectType1,?,M,O,S> h2){
		super(h1,h2);
	}

	public Integer 
	executeOn(BackingStore<O,Comparable<?>,S>.RemoteObject<ObjectType1> bs1, 
				BackingStore<O,Comparable<?>,S>.RemoteObject<ObjectType1> bs2){
		return null;
	}

	public Integer noop(){
		return 0;
	}

}
