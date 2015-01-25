package operations;

import handles.access.Unspecified;

import java.io.Serializable;
import java.util.concurrent.Callable;

import remote.BackingStore;
import remote.Handle;

import consistency.BaseModel;

public interface Operation<T, 
	A extends Unspecified, Obj extends Serializable, Orig extends BaseModel, Store extends BackingStore<Orig,Store>,
	M extends Orig, This_p extends Operation<T,A,Obj,Orig,Store,M,This_p>> extends Callable<T>{
	public T execute();
	public This_p build(Handle<Obj,A,M,Orig,Store> h, This_p op);
}
