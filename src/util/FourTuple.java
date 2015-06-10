package util;

import java.io.Serializable;

public class FourTuple<A extends Serializable,
						B extends Serializable,
						C extends Serializable,
						D extends Serializable>
	implements Serializable{
	public final A a;
	public final B b;
	public final C c;
	public final D d;
	
	public FourTuple(A a, B b, C c, D d){
		this.a = a; this.b = b; this.c = c; this.d = d;
	}

	@Override
	public String toString(){
		return "< " + a.getClass().toString() + " : " + a.toString() + " | " + b.getClass().toString() + " : " + b.toString() + " | " + c.getClass().toString() + " : " + c.toString() + " | " + d.getClass().toString() + " : " + d.toString() + " >";
	}
}
