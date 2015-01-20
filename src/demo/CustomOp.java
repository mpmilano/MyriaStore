package demo;

import handles.access.Any;
import operations.BaseNativeOperation1;
import remote.BackingStore;
import remote.Handle;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation1<Void, Object, Linearizable<handles.access.Any>, Linearizable<handles.access.Any>> {

	public <T, a extends Any> CustomOp(Handle<T, a, Linearizable<handles.access.Any>, Linearizable<handles.access.Any>, LinearizableStore> h) {
		super(new Handle<Object, a, Linearizable<handles.access.Any>, Linearizable<handles.access.Any>, LinearizableStore>(h, h.c));
		// TODO Auto-generated constructor stub
	}

	@Override
	public Void executeOn(BackingStore<Linearizable<handles.access.Any>>.RemoteObject<Object> bs) {
		// TODO Auto-generated method stub
		return bs.runOp(this);
		
	}
	
	@Override
	public Void noop() {
		return null;
	}

}
