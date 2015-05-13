package util;

import java.io.Serializable;

public class TwoTuple<A extends Serializable,
						B extends Serializable>
	implements Serializable{
	public final A a;
	public final B b;
	
	public TwoTuple(A a, B b){
		this.a = a; this.b = b;
	}
}
