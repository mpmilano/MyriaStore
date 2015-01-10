package operations;

import consistency.BaseModel;
import handles.access.ReadOnly;
import remote.BackingStore;
import remote.Handle;

public class Get<T, Model extends BaseModel> extends BaseNativeOperation<T, Model> {

	public <A extends ReadOnly> Get(Handle<T,A,Model,?> h){
		super(h);
	}
	
	@Override
	public <OtherModel /*compat*/ extends BaseModel> T executeOn(BackingStore<OtherModel> bs) {
		return bs.runOp(this);
	}

	@Override
	public T noop() {
		// TODO Auto-generated method stub
		throw new RuntimeException("You MUST SUPPORT Put and Get on your store.");
	}

}
