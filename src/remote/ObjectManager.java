package remote;

import handles.access.ReadWrite;
import consistency.BaseModel;

public interface ObjectManager {

	public abstract <Model extends BaseModel> boolean registerStore(
			BackingStore<Model> e);

	public abstract <Model extends BaseModel> void deactivateStore(
			BackingStore<Model> e);

	public abstract <Model extends BaseModel, T> Handle<T, ReadWrite, Model, Model> newObject(
			Model m, T t);

}