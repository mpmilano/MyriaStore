package remote;

import handles.access.Unspecified;

import java.lang.reflect.InvocationTargetException;

import operations.BaseNativeOperation1;
import operations.BaseNativeOperation2;
import operations.Get;
import operations.Put;


public abstract class BackingStore<Access extends Unspecified, Model extends consistency.BaseModel<Access>, 
	BS extends BackingStore<Access, Model, BS>> {
	
	public abstract class RemoteObject<T>{
		public final BS store;
		protected RemoteObject(BS store){
			this.store = store;
		}

		protected <T_old extends T> RemoteObject(RemoteObject<T_old> o){
			this.store = o.store;
		}

		public abstract <M /*compat*/ extends Model> T runOp(Get<T,Model,M> op);
		public abstract <M /*compat*/ extends Model> void runOp(Put<T, Model,M> op);

		//HOLY BALLS IS THIS SKETCHY.
		@SuppressWarnings("unchecked")
		public <T1, M extends Model> T1 runOp(
				BaseNativeOperation1<T1, T, Model, M> op) {
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
				BaseNativeOperation2<Ret, T, T2, Model, Model, BS> op, RemoteObject<T2> ro2) {
			//TODO: aaaaah where do the arguments go.
			return null;
		}
		//public abstract <T1, M /*compat*/ extends consistency.BaseModel> T1 runOp(BaseNativeOperation1<T1,T,M> op);
		//*/

		protected abstract T exposeRef();
		protected abstract <T2> RemoteObject<T2> newRef(T2 t);
	}
	
	public static <Access extends Unspecified, Model extends consistency.BaseModel<Access>, 
	BS extends BackingStore<Access, Model, BS>, T2, T extends T2>
	BackingStore<Access, Model, BS>.RemoteObject<T2> generalize(BackingStore<Access, Model, BS>.RemoteObject<T> r){
		T2 ref = r.exposeRef();
		return r.newRef(ref);
	}
	
	public abstract <T> RemoteObject<T> newObject(T t);
	private final Model m;
	public Model getModel() {return m;}
	protected BackingStore(Model m){this.m = m;}
}
