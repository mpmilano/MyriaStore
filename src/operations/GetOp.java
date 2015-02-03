#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class GetOp<R_(HT), OpBasics & HasAccess<? extends Read> & HasConsistency<C> > extends Operation<R_g(HT),C> {

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
