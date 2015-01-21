package remote;

import handles.access.ReadWrite;



import consistency.BaseModel;

//note - I want the "there is no backing store related to this consistency model" error
//to occur at compile-time.  There's no way to do that in this context, so it looks like
//we'll have to have mutually-dependent constructors or something (for model and store).

public class ObjectManager_impl implements ObjectManager {


	@Override
	public <Model extends BaseModel, T, Location extends BackingStore<Model, Location>,
	RemoteObject extends BackingStore<Model, Location>> 
	Handle<T, ReadWrite, Model, Model,Location> newObject(
			T t, Location l){		//TODO: this is definitely wrong.
		return new Handle<T,ReadWrite,Model,Model,Location>(l,t);
	}
}
