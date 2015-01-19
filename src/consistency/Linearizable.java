package consistency;

import handles.access.ReadWrite;
import handles.access.Unspecified;

public class Linearizable<T extends Unspecified> extends BaseModel<T> {
	
	private static Linearizable<ReadWrite> el;
	
	@SuppressWarnings("unchecked")
	private Linearizable(){
		super();
		el = (Linearizable<ReadWrite>) this;
	}
	
	public static Linearizable<ReadWrite> model(){
		if (el == null) el = new Linearizable<ReadWrite>();
		return el;
	}
	
}
