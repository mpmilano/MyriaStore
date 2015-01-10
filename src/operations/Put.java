package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.Handle;
import remote.RemoteObject;

public class Put<T, Model extends BaseModel> extends BaseNativeOperation<Void, Model> {

	public final T t;
	
	public <A extends ReadOnly> Put(Handle<T,A,Model,?> h, T t){
		super(h);
		this.t = t;
	}
	
	@Override
	public <OtherModel /*compat*/ extends BaseModel> Void executeOn(RemoteObject<?, OtherModel, ?> bs) {
		return bs.runOp(this);
	}

	@Override
	public Void noop() {
		return null;
	}
}
