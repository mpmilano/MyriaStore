#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class PutOp<R_(T), Consistency_(C), HBSObj extends ObjPut, H extends GetUnderlyingObj<HBSObj> & HasAccess<? extends Write> & PointsTo<T> & HasConsistency<C>> extends Operation<R_g(Void),C> {

	private H h;
	private T t;
	
	public PutOp(H h, T t){
		this.h = h;
		this.t = t;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Void execute(){
		h.getUnderlyingObj().put(t);
		return null;
	}

}
