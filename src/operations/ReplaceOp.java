#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;

public class ReplaceOp<R_(T), S extends Replace<HBSObj>, HBSObj,
										H1 extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Write> & PointsTo<T>,
												   H2 extends GetUnderlyingObj<HBSObj> & GetStore<S> & HasAccess<? extends Read> & PointsTo<T>
															  > extends Operation<R_g(Void)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H1 h;
	private H2 t;
	
	public ReplaceOp(H1 h, H2 t){
		this.h = h;
		this.t = t;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Void execute(){
		try {
			h.getStore().replace(h.getUnderlyingObj(), t.getUnderlyingObj());
		}
		finally{
			return null;
		}
	}

}
