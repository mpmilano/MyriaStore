package remote;

public abstract class RemoteObject<T, Model extends consistency.BaseModel, 
								S extends BackingStore<Model> > {
	public final S store;
	RemoteObject(S store){
		this.store = store;
	}
	
}
