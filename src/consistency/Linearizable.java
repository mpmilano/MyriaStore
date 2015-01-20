package consistency;

public class Linearizable<A extends handles.access.Any> extends BaseModel<handles.access.Any> {
	
	private static Linearizable<handles.access.Any> el;
	
	private Linearizable(){
		super();
		el = generalize(this);
	}
	
	@SuppressWarnings("unchecked")
	public static <A extends handles.access.Any, Aold extends A> Linearizable<A> generalize(Linearizable<Aold> b){
		return (Linearizable<A>) b;
	}
	
	public static Linearizable<handles.access.Any> model(){
		if (el == null) el = new Linearizable<handles.access.Any>();
		return el;
	}
	
}
