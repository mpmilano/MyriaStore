package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.Handle;
import remote.RemoteObject;

public class Put<T, Model extends BaseModel> extends BaseNativeOperation1<Void, T, Model> {

	public final T t;
	
	public <A extends ReadOnly> Put(Handle<T,A,Model,?,?> h, T t){
		super(h);
		this.t = t;
	}
	
	@Override
	public <OtherModel /*compat*/ extends BaseModel> Void executeOn(RemoteObject<T, OtherModel, ?> bs) {
		bs.runOp(this);
		return null;
	}

	@Override
	public Void noop() {
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}
}
