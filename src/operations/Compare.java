package operations;

import handles.access.ReadOnly;

import java.io.Serializable;

import consistency.BaseModel;

import remote.BackingStore;
import remote.Handle;

public class Compare<T extends Comparable<T> & Serializable, O extends BaseModel,S extends BackingStore<O,S>, M extends O, A extends ReadOnly> 
	extends BaseNativeOperation2<Integer,T,T,O,S,M,A> {

	public <B extends ReadOnly>
		Compare(Handle<T, A, M, O, S> h1, Handle<T, B, M, O, S> h2) {
		super(h1, h2);
	}


	@Override
	public Integer noop() {
		return 0;
	}

}
