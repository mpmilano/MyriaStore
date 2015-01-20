package remote;

import handles.access.ReadWrite;
import consistency.BaseModel;

public interface ObjectManager {

	public abstract <Model extends BaseModel, S extends BackingStore<Model>> boolean registerStore(
			S e);

	public abstract <Model extends BaseModel, S extends BackingStore<Model>> void deactivateStore(
			S e);

	public abstract <Model extends BaseModel, T, Location extends BackingStore<Model>> 
	Handle<T, ReadWrite, Model, Model,Location> newObject(
			T t, Location l);

}