package remote;

public interface BackingStore<Model extends consistency.BaseModel> {
	public <T> RemoteObject<T, Model, BackingStore<Model> > newObject();
	public Model getModel();
}
