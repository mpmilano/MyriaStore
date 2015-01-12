package remote;

import handles.access.ReadWrite;
import consistency.BaseModel;

public interface ObjectManager {

	public abstract <Model extends BaseModel, S extends BackingStore<Model,S>> boolean registerStore(
			S e);

	public abstract <Model extends BaseModel, S extends BackingStore<Model, S>> void deactivateStore(
			S e);

	public abstract <Model extends BaseModel, T, Location extends BackingStore<Model, Location>> 
	Handle<T, ReadWrite, Model, Model,Location> newObject(
			Model m, T t, Location l);

}