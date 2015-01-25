package operations;

import handles.access.Unspecified;

import java.io.Serializable;

import consistency.BaseModel;
import remote.BackingStore;
import remote.Handle;

public abstract class BaseNativeOperation1<ReturnType, 
	ObjectType extends Serializable,
	StoreAt extends BaseModel,
	StoreWhere extends BackingStore<StoreAt, StoreWhere>,
	ExecuteAt /*compat */ extends StoreAt, Access extends Unspecified> 
		implements Operation<ReturnType,Access,ObjectType,StoreAt,StoreWhere,ExecuteAt,BaseNativeOperation1<ReturnType,ObjectType,StoreAt,StoreWhere,ExecuteAt, Access>>{

	public final Handle<ObjectType,Access,ExecuteAt,StoreAt,StoreWhere> h;
	
	public BaseNativeOperation1 (Handle<ObjectType,Access,ExecuteAt,StoreAt,StoreWhere> h){
		this.h = h;
	}
	
	public ReturnType executeOn(BackingStore<StoreAt,StoreWhere>.RemoteObject<ObjectType> bs){
		return bs.runOp(this);
	}
	
	@Override
	public ReturnType call() throws Exception {
		return execute();
	}
	
	@Override
	public ReturnType execute(){
		return executeOn(h.ro);
	}
	
	public ReturnType noop(){
		return null;
	}
	
	@Override
	public BaseNativeOperation1<ReturnType,ObjectType,StoreAt,StoreWhere,ExecuteAt, Access> 
	build(Handle<ObjectType,Access,ExecuteAt,StoreAt,StoreWhere> h, BaseNativeOperation1<ReturnType,ObjectType,StoreAt,StoreWhere,ExecuteAt, Access> op){
		return op;
	}

	
}
