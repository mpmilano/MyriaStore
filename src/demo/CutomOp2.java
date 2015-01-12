package demo;

import consistency.Linearizable;
import operations.BaseNativeOperation2;
import remote.Handle;
import remote.RemoteObject;

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
			RemoteObject<T1, Linearizable, LinearizableStore> bs1,
			RemoteObject<T2, Linearizable, LinearizableStore> bs2) {
		return bs1.runOp(this,bs2);
	}

	@Override
	public Void noop() {
		return null;
	}

}
