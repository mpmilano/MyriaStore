package remote;

import operations.BaseOperation;
import operations.Get;

public abstract class BackingStore<Model extends consistency.BaseModel> {
	public abstract <T> RemoteObject<T, Model, BackingStore<Model> > newObject();
	private final Model m;
	public Model getModel() {return m;}
	BackingStore(Model m){this.m = m;}
	
	public abstract <T, M /*compat*/ extends consistency.BaseModel> T runOp(Get<T,M> op);
	public <T, M /*compat*/ extends consistency.BaseModel> T runOp(BaseOperation<T,M> op){
		//TODO - other option is make this a no-op.  Pretty sure I need to do that for soundness.
		System.err.println("Native operation unspported on this store");		
		return op.noop();
	}
}
