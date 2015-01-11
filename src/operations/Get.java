package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.Handle;
import remote.RemoteObject;

public class Get<T, Model extends BaseModel> extends BaseNativeOperation1<T, T, Model> {

	public <A extends ReadOnly> Get(Handle<T,A,Model,?> h){
		super(h);
	}
	@Override
	public <OtherModel extends BaseModel> T executeOn(
			RemoteObject<T, OtherModel, ?> bs) {
		return bs.runOp(this);
	}

    	

	@Override
	public T noop() {
		// TODO Auto-generated method stub
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}




}
