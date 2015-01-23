package demo;

import java.io.Serializable;

import handles.access.Unspecified;
import operations.BaseNativeOperation1;
import remote.Handle;
import consistency.Linearizable;

public class CustomOp extends BaseNativeOperation1<Void, Serializable, Linearizable, LinearizableStore, Linearizable> {

	public <T extends Serializable, a extends Unspecified> CustomOp(Handle<T, a, Linearizable, Linearizable, LinearizableStore> h) {
		super(new Handle<Serializable, a, Linearizable, Linearizable, LinearizableStore>(h));
	}

}
