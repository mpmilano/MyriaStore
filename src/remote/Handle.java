package remote;


import handles.access.*;

public final class Handle <T, Access extends Unspecified, 
						Consistency extends consistency.BaseModel,
						Original extends consistency.BaseModel,
						Location extends BackingStore<Original, Location>> {
	public final remote.BackingStore<Original,Location>.RemoteObject<T> ro;
	public final Consistency c;
	public final Original oc;
	
	//TODO: constrain that this constructor variant only works
	//when original consistency and thishandle Consistency are the 
	//same.
	Handle(Consistency m, Location bs, T t){
		ro = bs.newObject(t);
		c = m;
		oc = bs.getModel();
	}
	
	//TODO - need a "compatible" construct here
	public <OldConsistency /* compatible */extends Original, 
			OldAccess extends Access, OldT extends T>
	//actually, I think an allow_when or enable_if construct would do just as well.
	Handle(Handle<OldT,OldAccess,OldConsistency,Original, Location> h, Consistency c){
		this.ro = Location.generalize(h.ro);
		this.c = c;
		this.oc = h.oc;
	}
}
