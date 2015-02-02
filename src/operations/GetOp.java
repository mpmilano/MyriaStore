#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;

public class GetOp<R_(HT), S extends Get<HBSObj>, HBSObj, H extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Read> > extends Operation<R_g(HT)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h;
	
	public GetOp(H h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		return (HT) h.getStore().getObj(h.getUnderlyingObj());
	}

}
