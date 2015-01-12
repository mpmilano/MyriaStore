package remote;


public abstract class BackingStore<Model extends consistency.BaseModel, 
	BS extends BackingStore<Model, BS>> {
	public abstract <T> RemoteObject<T, Model, BS> newObject(T t);
	private final Model m;
	public Model getModel() {return m;}
	protected BackingStore(Model m){this.m = m;}
}
