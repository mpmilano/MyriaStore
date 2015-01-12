package remote;

import java.lang.reflect.InvocationTargetException;

import consistency.BaseModel;
import operations.BaseNativeOperation1;
import operations.BaseNativeOperation2;
import operations.Get;
import operations.Put;

public abstract class RemoteObject<T, Model extends consistency.BaseModel, 
S extends BackingStore<Model,S> > {
	public final S store;
	protected RemoteObject(S store){
		this.store = store;
	}

	protected <T_old extends T> RemoteObject(RemoteObject<T_old, Model, S> o){
		this.store = o.store;
	}

	public abstract <M /*compat*/ extends consistency.BaseModel> T runOp(Get<T,M> op);
	public abstract <M /*compat*/ extends consistency.BaseModel> void runOp(Put<T, M> op);
	
	///arrrghh if only "super" existed in the context I want!
	static <T, T2 extends T, Model extends BaseModel, S extends BackingStore<Model,S>> 
	RemoteObject<T,Model,S> generalize(RemoteObject<T2,Model,S> ro){
		return (RemoteObject<T, Model, S>) ro;
	}

	//HOLY BALLS IS THIS SKETCHY.
	@SuppressWarnings("unchecked")
	public <T1, M extends BaseModel> T1 runOp(
			BaseNativeOperation1<T1, T, M> op) {
			try {
				return (T1) this.getClass().getMethod("runOp", op.getClass()).invoke(this, op);
			} catch (NoSuchMethodException e){
				//TODO - other option is make this a no-op.  Pretty sure I need to do that for soundness.
				assert(! (op instanceof Get ));
				assert(! (op instanceof Put));
				System.err.println("Native operation unspported on this store");	
				System.err.println(this.getClass().getName());
				return op.noop();
			}
			catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException(e);
			}
	}
	

	public <Ret, T2> Ret runOp(
			BaseNativeOperation2<Ret, T, T2, Model, Model, S> op, RemoteObject<T2,Model,S> ro2) {
		//TODO: aaaaah where do the arguments go.
		return null;
	}
	//public abstract <T1, M /*compat*/ extends consistency.BaseModel> T1 runOp(BaseNativeOperation1<T1,T,M> op);
	//*/

}
