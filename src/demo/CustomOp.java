package demo;

import java.io.Serializable;

import handles.access.Unspecified;
import operations.BaseNativeOperation1;
import remote.Handle;
import consistency.Linearizable;

public class CustomOp<A extends Unspecified> extends BaseNativeOperation1<Void, Serializable, Linearizable, LinearizableStore, Linearizable, A> {

	public <T extends Serializable> CustomOp(Handle<T, A, Linearizable, Linearizable, LinearizableStore> h) {
		super(new Handle<Serializable, A, Linearizable, Linearizable, LinearizableStore>(h));
	}

}
