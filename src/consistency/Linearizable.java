package consistency;

public class Linearizable extends BaseModel {
	
	private static Linearizable el;
	
	private Linearizable(){
		super();
		el = this;
	}
	
	public static Linearizable model(){
		if (el == null) el = new Linearizable();
		return el;
	}
	
}
