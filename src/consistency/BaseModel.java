package consistency;

//this will pretty much have to available at compile-time, somehow.  
//Language extensions ho!

//must be singleton!
public abstract class BaseModel {

	private static boolean fls = false;
	
	abstract boolean equals(BaseModel bm);
	public BaseModel(){
		if (fls) throw new RuntimeException("Must be singleton!");
		fls = true;
	}
}
