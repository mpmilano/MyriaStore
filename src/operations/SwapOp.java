#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class SwapOp<R_(T), OpBasics & HasAccess<? extends ReadWrite> & HasConsistency<C> & PointsTo<T>>
	extends Operation<Void,C> {

//public class SwapOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h1;
	private H h2;
	
	public SwapOp(H h1, H h2){
		this.h1 = h1;
		this.h2 = h2;
	}

	@Override
	public Void execute(){
		S s = h1.getStore();
		try {
			@SuppressWarnings("unchecked")
				Swap<HBSObj> sw = (Swap<HBSObj>) s;
			sw.swapObj(h1.getUnderlyingObj(), h2.getUnderlyingObj());
		}
		catch (ClassCastException | java.io.IOException e){
			@SuppressWarnings("unchecked")
				T tmp1 = (T) s.getObj(h1.getUnderlyingObj());
			@SuppressWarnings("unchecked")
				T tmp2 = (T) s.getObj(h2.getUnderlyingObj());
			s.putObj(h1.getUnderlyingObj(),tmp2);
			s.putObj(h2.getUnderlyingObj(),tmp1);
		}
		return null;
	}
}
