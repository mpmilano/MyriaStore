#include "BackingStore.h"
#include "Handle.h"

package remote;
import operations.*;

public abstract class BackingStore<BackingStore_(HBS)> implements Get<HBSObj>, Put<HBSObj>{

public abstract class RemoteObject {
	public abstract HBS getStore();
	public abstract Class<?> getUnderlyingClass();
	public abstract consistency.Consistency getModel();
	public abstract void setModel(consistency.Consistency c);
}

	protected abstract HBSObj newObj(HBSAtype arg, Object init) throws Exception;

	public <R_(T)> Handle<R_g(T), Handle_fromBS(H)> newObject(R_g(T) init, HBSAtype arg){
		try {
			HBSObj newobj = newObj(arg,init);
			assert(init.getClass() == newobj.getUnderlyingClass());
			return new Handle<>(newobj);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public abstract class AltObjFact<R_(T), OHBSObj extends HBSObj> {
		protected Handle<R_g(T), Handle_fromBS2(H,OH)> buildHandle(OHBSObj oh){
			return new Handle<>(oh);
		}
	}
	protected abstract class ObjFact<R_(T), Access_(A)> {
		public Handle<R_g(T), BSref_(HBS), A, HBSCons> buildHandle(HBSObj o) {
			return new Handle<>(o);
		}
	}


}
