package util;

import java.io.Serializable;

public class TwoTuple<A extends Serializable & Comparable<A>,
								B extends Serializable & Comparable<B>>
	implements Comparable<TwoTuple<A,B>>, Serializable{
	public final A a;
	public final B b;

	@Override
	public int compareTo(TwoTuple<A,B> t){
		int fst = a.compareTo(t.a);
		if (fst == 0) return b.compareTo(t.b);
		else return fst;
	}
	
	public TwoTuple(A a, B b){
		this.a = a; this.b = b;
	}
}
