#include "../remote/BackingStore.h"
#include "../remote/Handle.h"	
package operations;

import remote.*;

public class GetOp<HT extends java.io.Serializable,HBS extends remote.BackingStore<?,? ,? , HBS, ?> & Get<HBSObj>, HBSObj extends BackingStore<?, ?, ?, HBS, HBSObj>.RemoteObject> extends Operation<R_g(HT)> {

//public class GetOp<R_(HT), GT extends BackingStore<?,?,?,?>.RemoteObject<?>  > extends Operation<R_g(HT)> {

	private Handle<HT, ?, ?, ?, HBS, HBSObj, ?, ?> h;
	
	public GetOp(Handle<HT, ?, ?, ?, HBS, HBSObj, ?, ?> h){
		this.h = h;
	}

	@Override
	@SuppressWarnings("unchecked")
	public HT execute(){
		HBSObj tmp = h.obj;
		return (HT) h.obj.getStore().getObj(tmp);
	}

}
