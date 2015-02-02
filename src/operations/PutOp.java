#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;

public class PutOp<S extends Put<HBSObj>, HBSObj, H extends PutUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Write> > extends Operation<R_g(HT)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h;
	
	public GetOp(H h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		return (HT) h.getStore().putObj(h.getUnderlyingObj());
	}

}
