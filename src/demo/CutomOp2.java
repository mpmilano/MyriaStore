package demo;

import java.io.Serializable;

import consistency.Linearizable;
import operations.BaseNativeOperation2;
import remote.BackingStore;
import remote.Handle;

public class CutomOp2<T1 extends Serializable, T2 extends Serializable>
		extends
		BaseNativeOperation2<Void, T1, T2, consistency.Linearizable, consistency.Linearizable, demo.LinearizableStore> {

	public CutomOp2(
			Handle<T1, ?, Linearizable, Linearizable, LinearizableStore> h1,
			Handle<T2, ?, Linearizable, Linearizable, LinearizableStore> h2) {
		super(h1, h2);
	}

	@Override
	public Void executeOn(
			BackingStore<Linearizable, LinearizableStore>.RemoteObject<T1> bs1,
			BackingStore<Linearizable, LinearizableStore>.RemoteObject<T2> bs2) {
		return bs1.runOp(this,bs2);
	}

	@Override
	public Void noop() {
		return null;
	}

}
