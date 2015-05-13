package util;
import java.io.Serializable;

public class MakeMerge<T extends Serializable>
	implements Mergable<MakeMerge<T>>, Shim<T> {
	private T t;
	private boolean native_support = false;
	private String nonce = NonceGenerator.get();
	public MakeMerge(T t){
		if (t instanceof Mergable)
			throw new RuntimeException("use wrap!");
		this.t = t;
	}

	public static Mergable<?> wrap(Serializable t){
		if (t instanceof Mergable<?>) return (Mergable<?>) t;
		else return new MakeMerge<>(t);
	}

	@Override
	public T t(){return t;}

	@SuppressWarnings("unchecked")
	public MakeMerge<T> merge(MakeMerge<T> t){
		assert(native_support == t.native_support);
		if (native_support)
			this.t = (T) (((Mergable<T>) this.t).merge(t.t));
		else if (nonce.compareTo(t.nonce) < 0) this.t = t.t;
		else t.t = this.t;
		return this;
	}
}
