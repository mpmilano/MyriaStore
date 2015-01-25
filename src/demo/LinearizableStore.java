package demo;

import handles.access.ReadOnly;
import handles.access.Unspecified;
import handles.access.WriteOnly;

import java.io.Serializable;
import java.util.Set;

import javax.management.Descriptor;

import operations.Compare;
import operations.Get;
import operations.Insert;
import operations.Print;
import operations.Put;
import consistency.Linearizable;
import remote.BackingStore;
import remote.Handle;

public class LinearizableStore extends BackingStore<Linearizable, LinearizableStore> {

	public LinearizableStore(){
		super(Linearizable.model());
	}

	public class LinearizableStoreObject<T extends Serializable> extends
			RemoteObject<T> implements ExtraInfo {
		
		public T t;
		
		public LinearizableStoreObject(T t, LinearizableStore ls){
			super(ls);
			this.t = t;
		}

		@Override
		public <M extends Linearizable, A extends ReadOnly> T runOp(Get<T, Linearizable, LinearizableStore, M, A> op) {
			return t;
		}

		@Override
		public <M extends Linearizable, A extends WriteOnly> void runOp(Put<T, Linearizable, LinearizableStore, M, A> op) {
			t = op.t;
		}
		
		public <M extends Linearizable,E extends Serializable, S extends Set<E> & Serializable> 
		LinearizableStoreObject<S> runOp(Insert<E, S, Linearizable, LinearizableStore, M, ?> op, LinearizableStoreObject<E> e) {
			if (this == op.h1.ro){
				@SuppressWarnings("unchecked")
				LinearizableStoreObject<S> this_p = (LinearizableStoreObject<S>) this;
				this_p.t.add(e.t);
				return this_p;
			}
			else throw new RuntimeException("internal passing is very broken here!");
		}
		
		public <A extends Unspecified> void runOp(CustomOp<A> c){
			System.out.println("custom fever!");
		}

		@Override
		protected T exposeRef() {
			return t;
		}
		
		@Override
		protected ExtraInfo exposeInfo(){
			return this;
		}

		@Override
		protected <T2 extends Serializable> RemoteObject<T2> newRef(T2 t, ExtraInfo ei) {
			//used in a (very) safe way.
			@SuppressWarnings("unchecked")
			RemoteObject<T2> thisp = 
			(RemoteObject<T2>)(this);
			if (ei == this) return thisp;
			else throw new RuntimeException("called from invalid context");
		}

		@Override
		public <M extends Linearizable, A extends ReadOnly> Handle<T,?,?,?,?> runOp(
				Print<T, Linearizable, LinearizableStore, M, A> op) {
			System.out.println(t.toString());
			return op.h;
		}
	}
	public class DescrLinStoreObject<T extends Descriptor > 
		extends LinearizableStoreObject<T> {
		
		public DescrLinStoreObject(T t, LinearizableStore ls){
			super(t,ls);
		}

		public <A extends ReadOnly> String runOp(
				GetFieldValue<T, Linearizable, Linearizable, LinearizableStore, A> op) {
			return (String) t.getFieldValue(op.arg);
		}
	}
	
	public class ComparableObject<T extends Serializable & Comparable<T>> 
		extends LinearizableStoreObject<T>{

		public ComparableObject(T t, LinearizableStore ls) {
			super(t, ls);
		}
		
		public Integer runOp(Compare<T, Linearizable, LinearizableStore, Linearizable, ?> c, ComparableObject<T> o){
			return t.compareTo(o.t);
		}
		
	}
	
	@Override
	public <T extends Serializable> Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore> 
	newDumbObject(T t) {
		return new Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore>(new LinearizableStoreObject<T>(t,this));
	}
	
	public <T extends Descriptor> Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore> 
	newObject(T t) {
		return new Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore>(new DescrLinStoreObject<>(t,this));
	}
	
	public <T extends Comparable<T> & Serializable> Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore> 
	newCObject(T t) {
		return new Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore>(new ComparableObject<>(t,this));
	}

}
