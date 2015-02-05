#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class PutOp<R_(T), OpBasics(HasAccess<? extends Write> & PointsTo<T> & HasConsistency<C>)>
	extends Operation<R_g(Void),C> {

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
