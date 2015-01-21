package remote;


import java.io.Serializable;

import handles.access.*;

public final class Handle <T extends Serializable, Access extends Unspecified, 
						Consistency extends consistency.BaseModel,
						Original extends consistency.BaseModel,
						Location extends BackingStore<Original, Location>> {
	public final remote.BackingStore<Original, Location>.RemoteObject<T> ro;
	
	//TODO: constrain that this constructor variant only works
	//when original consistency and thishandle Consistency are the 
	//same.
	public Handle(BackingStore<Original,Location>.RemoteObject<T> ro){
		this.ro = ro;
	}
	
	//TODO - need a "compatible" construct here
	public <OldConsistency /* compatible */extends Original, 
			OldAccess extends Access, OldT extends T>
	//actually, I think an allow_when or enable_if construct would do just as well.
	Handle(Handle<OldT,OldAccess,OldConsistency,Original, Location> h){
		this.ro = Location.generalize(h.ro);
	}
}
