#include "BackingStore.h"
#include "Handle.h"

package remote;

public abstract class BackingStore<BackingStore_(HBS)> implements util.Dummy {

public abstract class RemoteObject {
	public abstract HBS getStore();
	public abstract Class<?> getUnderlyingClass();
	public abstract consistency.Consistency getModel();
	public abstract void setModel(consistency.Consistency c);
}

	protected abstract HBSObj newObj(HBSAtype arg, Object init) throws Exception;

	public <R_(HT)> Handle<Handle_fromBS(H)> newObject(R_g(HT) init, HBSAtype arg){
		try {
			HBSObj newobj = newObj(arg,init);
			assert(init.getClass() == newobj.getUnderlyingClass());
			return new Handle<>(newobj);
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}

	public abstract class AltObjFact<R_(HT), OHBSObj extends HBSObj> {
		protected Handle<Handle_fromBS2(H,OH)> buildHandle(OHBSObj oh){
			return new Handle<>(oh);
		}
	}


}
