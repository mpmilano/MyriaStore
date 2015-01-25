package operations;

import handles.access.ReadOnly;
import handles.access.WriteOnly;

import java.io.Serializable;
import java.util.Set;

import consistency.BaseModel;

import remote.BackingStore;
import remote.Handle;

public class Insert<T extends Serializable, S extends Set<T> & Serializable, 
	O extends BaseModel,St extends BackingStore<O,St>, M extends O, A extends WriteOnly> 
	extends BaseNativeOperation2<BackingStore<O,St>.RemoteObject<S>,S,T,O,St,M,A> {

	public <B extends ReadOnly>
		Insert(Handle<S, A, M, O, St> h1, Handle<T, B, M, O, St> h2) {
		super(h1, h2);
	}

}
