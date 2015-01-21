package remote;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

import operations.*;


public abstract class BackingStore<Model extends consistency.BaseModel,
									This_t extends BackingStore<Model, This_t> > {
	
	public abstract class RemoteObject<T extends Serializable> {
		public final BackingStore<Model, This_t> store;
		protected RemoteObject(BackingStore<Model, This_t> store){
			this.store = store;
		}

		protected <T_old extends T> RemoteObject(RemoteObject<T_old> o){
			this.store = o.store;
		}

		//The "expression" problem
		public abstract <M /*compat*/ extends Model> T runOp(Get<T,Model,This_t,M> op);
		public abstract <M /*compat*/ extends Model> void runOp(Put<T, Model,This_t,M> op);

		//HOLY BALLS IS THIS SKETCHY.
		@SuppressWarnings("unchecked")
		public <T1, M extends Model> T1 runOp(
				BaseNativeOperation1<T1, T, Model, This_t, M> op) {
				try {
					assert(! (op instanceof Get ));
					assert(! (op instanceof Put));
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
		

		public <Ret, T2 extends Serializable> Ret runOp(
				BaseNativeOperation2<Ret, T, T2, Model, Model, This_t> op, 
				BackingStore<Model, This_t>.RemoteObject<T2> ro2) {
			//TODO: aaaaah where do the arguments go.
			return null;
		}
		

		protected abstract T exposeRef();
		protected abstract <T2 extends Serializable> RemoteObject<T2> newRef(T2 t);
	}
	
	public static <Model extends consistency.BaseModel,
		BS extends BackingStore<Model, BS>, T2 extends Serializable, T extends T2> 
	BackingStore<Model,BS>.RemoteObject<T2> 
		generalize(BackingStore<Model,BS>.RemoteObject<T> r){
		T2 ref = r.exposeRef();
		return r.newRef(ref);
	}
	
	public abstract <T extends Serializable> RemoteObject<T> newDumbObject(T t);
	
	
	private final Model m;
	public Model getModel() {return m;}
	protected BackingStore(Model m){this.m = m;}
}
