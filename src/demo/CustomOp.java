package demo;

import handles.access.Unspecified;
import operations.BaseNativeOperation1;
import remote.Handle;
import remote.RemoteObject;
import consistency.BaseModel;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation1<Void, Object, Linearizable> {

	public <T, a extends Unspecified> CustomOp(Handle<T, a, Linearizable, Linearizable, LinearizableStore> h) {
		super(new Handle<Object, a, Linearizable, Linearizable, LinearizableStore>(h, h.c));
		// TODO Auto-generated constructor stub
	}

	@Override
	public <OtherModel extends BaseModel> Void executeOn(
			RemoteObject<Object, OtherModel, ?> bs) {
		// TODO Auto-generated method stub
		return bs.runOp(this);
		
	}
	
	@Override
	public Void noop() {
		return null;
	}

}
