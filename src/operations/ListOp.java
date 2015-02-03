#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class ListOp<S extends List<HBSObj>, HBSObj, Consistency_(C),
							  H extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Read> & HasConsistency<C> >
	extends Operation<String[],C> {

//public class ListOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h;
	
	public ListOp(H h){
		this.h = h;
	}

	@Override
	public String[] execute(){
		return h.getStore().list(h.getUnderlyingObj());
	}

}
