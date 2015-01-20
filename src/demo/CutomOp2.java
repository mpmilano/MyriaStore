package demo;

import consistency.Linearizable;
import operations.BaseNativeOperation2;
import remote.BackingStore;
import remote.Handle;

public class CutomOp2<T1, T2>
		extends
		BaseNativeOperation2<Void, T1, T2, consistency.Linearizable, consistency.Linearizable, demo.LinearizableStore> {

	public CutomOp2(
			Handle<T1, ?, Linearizable, Linearizable, LinearizableStore> h1,
			Handle<T2, ?, Linearizable, Linearizable, LinearizableStore> h2) {
		super(h1, h2);
	}

	@Override
	public Void executeOn(
			BackingStore<Linearizable>.RemoteObject<T1> bs1,
			BackingStore<Linearizable>.RemoteObject<T2> bs2) {
		return bs1.runOp(this,bs2);
	}

	@Override
	public Void noop() {
		return null;
	}

}
