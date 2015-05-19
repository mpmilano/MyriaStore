package operations;

import remote.*;
import util.*;
import java.util.Collection;
import java.io.Serializable;

public class InsertFactory<Obj extends RemoteObject<?,?>,
									   Set extends RemoteObject<? extends Collection<?>, ? >,
												   Sto extends Insert<Set,Obj> > {

	public InsertFactory(Sto s){}
	
	public <T extends Serializable, J extends Collection<T> & Serializable, C extends consistency.Top, Stok>
		Operation<Void,C> build(final Handle<J,C,? extends access.Write, ?, Stok> set,
								final Handle<T,C,? extends access.Read, ?, Stok> elem){
		return new Operation<Void,C>() {
			@Override
			public Void execute(){
				try {
					//This try block casts the store and object to
					//those natively supported by the store's insert operation.
					//If it doesn't support such an operation, this will fail
					//and fall back to the everystore catch-block implementation.
					@SuppressWarnings("unchecked")
						Sto sto = ((Sto) set.ro.getStore());
					@SuppressWarnings("unchecked")
						Set st = (Set) set.ro;
					@SuppressWarnings("unchecked")
						Obj obj = (Obj) elem.ro;
					sto.insert(st,obj);
				}
				catch (ClassCastException e){
					J j = set.ro.get();
					j.add(elem.ro.get());
					set.ro.put(j);
				}
				catch (MyriaIOException e){
					System.err.println("something is very wrong!");
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}
	public <T extends Serializable, J extends Collection<T> & Serializable, C extends consistency.Top>
		Operation<Void,C> build2(final Handle<J,C,? extends access.Write, ?, ?> set, final T elem){
		return new Operation<Void,C>() {
			@Override
			public Void execute(){
				try {
					//This try block casts the store and object to
					//those natively supported by the store's insert operation.
					//If it doesn't support such an operation, this will fail
					//and fall back to the everystore catch-block implementation.
					@SuppressWarnings("unchecked")
						Sto sto = ((Sto) set.ro.getStore());
					@SuppressWarnings("unchecked")
						Set st = (Set) set.ro;
					sto.insert(st,elem);
				}
				catch (ClassCastException e){
					J j = set.ro.get();
					j.add(elem);
					set.ro.put(j);
				}
				catch (MyriaIOException e){
					System.err.println("something is very wrong!");
					throw new RuntimeException(e);
				}
				return null;
			}
		};
	}
}

