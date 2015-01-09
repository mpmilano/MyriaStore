package handles;


import remote.*;
import handles.access.*;

public class Handle <T, Access extends Unspecified, 
						Consistency extends consistency.BaseModel,
						Original extends consistency.BaseModel> {
	private final RemoteObject<T,Original,?> ro;
	
	public Handle(BackingStore<Original> bs){
		ro = bs.newObject();
	}
	
}
