package util;

import java.io.*;

public class CloneFromMergable<T extends Serializable & Mergable<T>>
	implements Shim<T>, Mergable<CloneFromMergable<T>>, RCloneable<CloneFromMergable<T>>{

	private T t;
	private boolean native_support = false;
	private boolean second_level_support = false;
	
	public CloneFromMergable(T t){
		if (t instanceof RCloneable<?>)
			throw new RuntimeException("built in error!");
		if (t instanceof Shim<?> && ((Shim<?>) t).t() instanceof RCloneable<?>)
			throw new RuntimeException("built in error!");
		this.t = t;
	}

	@SuppressWarnings("unchecked")
	public static <E extends Mergable<E> & RCloneable<E>>
		E wrap(Mergable<?> t){
		if (t instanceof RCloneable<?>) return (E) t;
		else return (E) new CloneFromMergable(t);
	}

	@Override
	public T t(){ return t;}

	@Override
	@SuppressWarnings("unchecked")
	public CloneFromMergable<T> rclone(){
		if (native_support)
			return new CloneFromMergable<>(((RCloneable<T>)t).rclone());
		if (second_level_support){
			throw new UnsupportedOperationException("error!");
		}
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			(new ObjectOutputStream(baos)).writeObject(t);
			return
				new CloneFromMergable<T>
				((T) (new ObjectInputStream
					  (new ByteArrayInputStream
					   (baos.toByteArray()))).readObject());
		}
		catch(IOException | ClassNotFoundException e){
			//this IS NOT POSSIBLE	
			throw new RuntimeException("INCONCEIVABLE!");
		}
	}

	@Override
	public CloneFromMergable<T> merge(CloneFromMergable<T> o){
		t = t.merge(o.t);
		return this;
	}
}
