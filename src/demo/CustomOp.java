package demo;

import handles.access.Unspecified;
import operations.BaseNativeOperation1;
import remote.BackingStore;
import remote.Handle;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation1<Void, Object, Linearizable, Linearizable> {

	public <T, a extends Unspecified> CustomOp(Handle<T, a, Linearizable, Linearizable, LinearizableStore> h) {
		super(new Handle<Object, a, Linearizable, Linearizable, LinearizableStore>(h, h.c));
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void executeOn(BackingStore<Linearizable>.RemoteObject<Object> bs) {
		// TODO Auto-generated method stub
		return bs.runOp(this);
		
	}
	
	@Override
	public Void noop() {
		return null;
	}

}
