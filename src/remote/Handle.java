#include "BackingStore.h"
#include "Handle.h"	
package remote;


//Things we can track statically:
// Origin, Origin's consistency, Handle's consistency, 
// handle's access level, 

//of these, only access level and consistency are really "user-facing" "public" things.

public final class Handle<Handle_P_(H)> implements StoreActions<HBS,HBSObj>, access.HasAccess<HA>,
												   consistency.HasConsistency<HC>,
												   consistency.OriginalConsistency<HBSCons>,
												   PointsTo<HT> {
	public final HBSObj obj;

	Handle(HBSObj obj){
		this.obj = obj;
	}

	//relaxing the type of the handle
	@SuppressWarnings("unchecked")
	public static <R_(NewT), OldT extends NewT, Handle_Post_(H) >
		Handle<NewT, Handle_Post_g(H)> relaxT(Handle<OldT, Handle_Post_g(H)> h){
		return (Handle<NewT, Handle_Post_g(H)>) h;
	}

	//relaxing the store internals
	@SuppressWarnings("unchecked")
	public static <Handle_P_(H), HBSObjOld extends HBSObj>
		Handle<Handle_P_g(H)> relaxStoreObj(Handle<HT, HBSCons, HBSAtype, HBS, HBSObjOld, HA, HC> h){
		return (Handle<Handle_P_g(H)>) h;
	}

	public static <R_(HT), BackingStore_PC(HBS,access.Read), Consistency_C(HC, HBSCons)>
		Handle<Handle_Pre_g(H), access.Read, HC> changeUp(Handle<Handle_Pre_g(H), ? extends access.Read, ? super HC> h) {
		return new Handle<>(h.obj);
		//TODO - signal the store that this is weaker somehow? 
	}

	public static <R_(HT), BackingStore_(HBS), Consistency_(HC)>
		Handle<HT, BSref_(HBS), access.Write, HC> changeDown(Handle<HT, BSref_(HBS), ? extends access.Write, ? extends HC> h) {		
		return new Handle<>(h.obj);
		//TODO - signal the store that this is weaker somehow? 
	}	

	@SuppressWarnings("unchecked")
	public Handle<R_g(HT), Handle_fromBS(H)> restore(){
		return (Handle<R_g(HT), Handle_fromBS(H)>) this;
	}

	@SuppressWarnings("unchecked")
	public Handle<HT, HBSCons, ?, ?, ?, HA, HC> generic(){
		return (Handle<HT, HBSCons, ?, ?, ?, HA, HC>) this;
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
