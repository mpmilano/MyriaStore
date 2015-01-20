package demo;

import consistency.Linearizable;
import operations.BaseNativeOperation2;
import remote.BackingStore;
import remote.Handle;

public class CutomOp2<T1, T2>
		extends
		BaseNativeOperation2<Void, T1, T2, consistency.Linearizable<handles.access.Any>, consistency.Linearizable<handles.access.Any>, demo.LinearizableStore> {

	public CutomOp2(
			Handle<T1, ?, Linearizable<handles.access.Any>, Linearizable<handles.access.Any>, LinearizableStore> h1,
			Handle<T2, ?, Linearizable<handles.access.Any>, Linearizable<handles.access.Any>, LinearizableStore> h2) {
		super(h1, h2);
	}

	@Override
	public Void executeOn(
			BackingStore<?,Linearizable<handles.access.Any>>.RemoteObject<T1> bs1,
			BackingStore<?,Linearizable<handles.access.Any>>.RemoteObject<T2> bs2) {
		return bs1.runOp(this,bs2);
	}

	@Override
	public Void noop() {
		return null;
	}

}
