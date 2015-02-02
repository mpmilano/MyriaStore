#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;

public class PutOp<R_(T), S extends Put<HBSObj>, HBSObj, H extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Write> & PointsTo<T> > extends Operation<R_g(Void)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h;
	private T t;
	
	public PutOp(H h, T t){
		this.h = h;
		this.t = t;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Void execute(){
		h.getStore().putObj(h.getUnderlyingObj(), t);
		return null;
	}

}
