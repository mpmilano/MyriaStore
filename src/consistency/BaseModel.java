package consistency;

//this will pretty much have to available at compile-time, somehow.  
//Language extensions ho!

//must be singleton!
public abstract class BaseModel<A extends handles.access.Any> implements Comparable<BaseModel<handles.access.Any>>{

	private static boolean fls = false;
	
	protected BaseModel(){
		if (fls) throw new RuntimeException("Must be singleton!");
		fls = true;
	}
	
	//TODO: use lattice 'n stuff to make this correct
	@Override
	public int compareTo(BaseModel<handles.access.Any> o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	public static <A extends handles.access.Any, Aold extends A> BaseModel<A> generalize(BaseModel<Aold> b){
		return (BaseModel<A>) b;
	}
	
}
