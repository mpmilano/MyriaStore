package remote;

import operations.BaseNativeOperation;
import operations.Get;
import operations.Put;

public abstract class RemoteObject<T, Model extends consistency.BaseModel, 
								S extends BackingStore<Model> > {
	public final S store;
	protected RemoteObject(S store){
		this.store = store;
	}
	
	public abstract <M /*compat*/ extends consistency.BaseModel> T runOp(Get<T,M> op);
	public abstract <M /*compat*/ extends consistency.BaseModel> void runOp(Put<T, M> op);

	public <T1, M /*compat*/ extends consistency.BaseModel> T1 runOp(BaseNativeOperation<T1,M> op){
		//TODO - other option is make this a no-op.  Pretty sure I need to do that for soundness.
		System.err.println("Native operation unspported on this store");		
		return op.noop();
	}

	
}
