package demo;

import consistency.Linearizable;
import remote.BackingStore;
import remote.RemoteObject;

public class LinearizableStore extends BackingStore<Linearizable> {

	public LinearizableStore(){
		super(Linearizable.model());
	}

	
	@Override
	public <T> RemoteObject<T, Linearizable, BackingStore<Linearizable>> newObject(T t) {
		return new LinearizableStoreObject<T>(t,this);
	}

}
