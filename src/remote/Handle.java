#include "BackingStore.h"
#include "Handle.h"	
package remote;


//Things we can track statically:
// Origin, Origin's consistency, Handle's consistency, 
// handle's access level, 

//of these, only access level and consistency are really "user-facing" "public" things.

public final class Handle<Handle_P_(H)> implements GetStore<HBS>, GetUnderlyingObj<HBSObj>, access.HasAccess<HA>, consistency.HasConsistency<HC> {
	public final HBSObj obj;

	public Handle(Class<HT> c, HBSObj obj){
		this.obj = obj;
	}

	@Override
	public HBS getStore(){
		return this.obj.getStore();
	}

	@Override
	public HBSObj getUnderlyingObj(){
		return this.obj;
	}

}
