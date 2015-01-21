package remote;


import handles.access.*;

public final class Handle <Constraint, T extends Constraint, Access extends Unspecified, 
						Consistency extends consistency.BaseModel,
						Original extends consistency.BaseModel,
						Location extends BackingStore<Original, Constraint, Location>> {
	public final remote.BackingStore<Original, Constraint, Location>.RemoteObject<T> ro;
	public final Original oc;
	
	//TODO: constrain that this constructor variant only works
	//when original consistency and thishandle Consistency are the 
	//same.
	Handle(Location bs, T t){
		ro = bs.newObject(t);
		oc = bs.getModel();
	}
	
	//TODO - need a "compatible" construct here
	public <OldConsistency /* compatible */extends Original, 
			OldAccess extends Access, OldT extends T>
	//actually, I think an allow_when or enable_if construct would do just as well.
	Handle(Handle<Constraint, OldT,OldAccess,OldConsistency,Original, Location> h){
		this.ro = Location.generalize(h.ro);
		this.oc = h.oc;
	}
}
