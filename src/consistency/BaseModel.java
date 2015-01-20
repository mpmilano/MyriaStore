package consistency;

//this will pretty much have to available at compile-time, somehow.  
//Language extensions ho!

//must be singleton!
public abstract class BaseModel implements Comparable<BaseModel>{

	private static boolean fls = false;
	
	protected BaseModel(){
		if (fls) throw new RuntimeException("Must be singleton!");
		fls = true;
	}
	
	//TODO: use lattice 'n stuff to make this correct
	@Override
	public int compareTo(BaseModel o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
