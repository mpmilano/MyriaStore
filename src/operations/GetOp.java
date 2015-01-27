#include "../remote/BackingStore.h"
package operations;

public class GetOp<Handle_PAC_(H,access.Read)> extends Operation<R_g(HT), BSRef_(HBS), HC> {

	private Handle<Handle_P_g(H)> h;
	
	public GetOp(Handle<Handle_P_g(H)> h){
		this.h = h;
	}

	@Override
	public T call(){
		return h.obj.getStore().getObj(h.obj);
	}

}
