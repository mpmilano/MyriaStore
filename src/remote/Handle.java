package remote;


import handles.access.*;

public final class Handle <T, Access extends Unspecified, 
						Consistency extends consistency.BaseModel,
						Original extends consistency.BaseModel> {
	public final RemoteObject<T,Original,?> ro;
	public final Consistency c;
	
	public Handle(Consistency m, BackingStore<Original> bs, T t){
		ro = bs.newObject(t);
		c = m;
	}
	
	//TODO - need a "compatible" construct here
	public <OldConsistency /* compatible */extends Original, OldAccess extends Access>
	//actually, I think an allow_when or enable_if construct would do just as well.
	Handle(Handle<T,OldAccess,OldConsistency,Original> h, Consistency c){
		this.ro = h.ro;
		this.c = c;
	}
	
}
