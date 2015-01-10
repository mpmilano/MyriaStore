package remote;


public abstract class BackingStore<Model extends consistency.BaseModel> {
	public abstract <T> RemoteObject<T, Model, BackingStore<Model> > newObject(T t);
	private final Model m;
	public Model getModel() {return m;}
	protected BackingStore(Model m){this.m = m;}
	
}
