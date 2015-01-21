package remote;

import handles.access.ReadWrite;
import consistency.BaseModel;

public interface ObjectManager {

	public abstract <Model extends BaseModel, T, Location extends BackingStore<Model, Location>,
	RemoteObject extends BackingStore<Model, Location>> 
	Handle<T, ReadWrite, Model, Model,Location> newObject(
			T t, Location l);

}