package remote;

import handles.access.ReadWrite;
import consistency.BaseModel;

public interface ObjectManager {

	public abstract <Model extends BaseModel<handles.access.Any>, S extends BackingStore<?, Model>> boolean registerStore(
			S e);

	public abstract <Model extends BaseModel<handles.access.Any>, S extends BackingStore<?, Model>> void deactivateStore(
			S e);

	public abstract <Model extends BaseModel<handles.access.Any>, T, Location extends BackingStore<?, Model>> 
	Handle<T, ReadWrite, Model, Model,Location> newObject(
			Model m, T t, Location l);

}