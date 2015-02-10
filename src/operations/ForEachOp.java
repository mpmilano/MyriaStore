#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;
import access.*;
import consistency.*;

public class ForEachOp<R_(T), S extends ForEach<C,HBSObj>, HBSObj, Consistency_(C), Access_C(A, Read),
										H extends StoreActions<S,HBSObj> & HasAccess<A> & HasConsistency<C> >
	extends Operation<Void,C> {

//public class ForEachOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private H h;
	private OperationFactory<T, C, RemHandle<T,C,A,C> > of;
	private RemHandle<?,C,A,C> h_backup;
	public ForEachOp(OperationFactory<T,C, RemHandle<T,C,A,C> > of, H h){
		this.h = h;
		this.of = of;
	}

	public ForEachOp(OperationFactory<T,C, RemHandle<T,C,A,C> > of, RemHandle<?,C,A,C> h){
		this.h_backup = h;
		this.of = of;
	}

	@Override
	public Void execute(){
		if (h != null) h.getStore().foreach(of,h.getUnderlyingObj());
		//else do native version.
		return null;
	}
}
