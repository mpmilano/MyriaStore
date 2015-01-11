package demo;

import operations.BaseNativeOperation;
import remote.Handle;
import remote.RemoteObject;
import consistency.BaseModel;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation<Void, Linearizable> {

	public CustomOp(Handle<?, ?, Linearizable, ?> h) {
		super(h);
		// TODO Auto-generated constructor stub
	}

	@Override
	public <OtherModel extends BaseModel> Void executeOn(
			RemoteObject<?, OtherModel, ?> bs) {
		// TODO Auto-generated method stub
		return bs.runOp(this);
	}

	@Override
	public Void noop() {
		return null;
	}

}
