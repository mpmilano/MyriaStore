#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class GetOp<R_(HT), HBSObj extends ObjGet, Consistency_(C), H extends GetUnderlyingObj<HBSObj> & HasAccess<? extends Read> & HasConsistency<C> & PointsTo<HT> > extends Operation<R_g(HT),C> {

	private H h;
	
	public GetOp(H h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		return (HT) h.getUnderlyingObj().get();
	}

}
