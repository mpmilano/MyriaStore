package consistency;

import handles.access.AccessLevel;
import handles.access.Unspecified;

//this will pretty much have to available at compile-time, somehow.  
//Language extensions ho!

//must be singleton!
public abstract class BaseModel<T extends Unspecified> implements AccessLevel<T>, Comparable<BaseModel<T>>{

	private static boolean fls = false;
	
	protected BaseModel(){
		if (fls) throw new RuntimeException("Must be singleton!");
		fls = true;
	}
	
	//TODO: use lattice 'n stuff to make this correct
	@Override
	public int compareTo(BaseModel<T> o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
