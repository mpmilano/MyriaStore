package demo;

import java.io.Serializable;

import handles.access.Unspecified;
import operations.BaseNativeOperation1;
import remote.BackingStore;
import remote.Handle;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation1<Void, Serializable, Linearizable, LinearizableStore, Linearizable> {

	public <T extends Serializable, a extends Unspecified> CustomOp(Handle<T, a, Linearizable, Linearizable, LinearizableStore> h) {
		super(new Handle<Serializable, a, Linearizable, Linearizable, LinearizableStore>(h));
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void executeOn(BackingStore<Linearizable, LinearizableStore>.RemoteObject<Serializable> bs) {
		// TODO Auto-generated method stub
		return bs.runOp(this);
		
	}
	
	@Override
	public Void noop() {
		return null;
	}

}
