package operations;

import remote.*;
import java.util.Collection;
import java.io.Serializable;
import util.*;

public class IncrementFactory {

	private IncrementFactory(){}

	public static IncrementFactory inst = new IncrementFactory();
	
	public <T extends Serializable & Incrementable, C extends consistency.Top>
		Operation<Void,C> build(final Handle<T,C,? extends access.Write, ?, ?> elem){
		return new Operation<Void,C>() {
			
			@Override
			public Void execute(){
				try {
					//This try block casts the store and object to
					//those natively supported by the store's insert operation.
					//If it doesn't support such an operation, this will fail
					//and fall back to the everystore catch-block implementation.
					@SuppressWarnings("unchecked")
						Increment<RemoteObject<? extends Incrementable,?>> sto =
						((Increment<RemoteObject<? extends Incrementable,?>>) elem.ro.getStore());
					RemoteObject<? extends Incrementable,?> obj =
						(RemoteObject<? extends Incrementable,?>) elem.ro;
					sto.incr(obj);
				}
				catch (ClassCastException e){
					assert(elem.ro != null);
					T a = elem.ro.get();
					assert(a != null);
					a.incr();
					elem.ro.put(a);
				}
				catch (java.io.IOException e){
					System.err.println("something is very wrong!");
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}
}

