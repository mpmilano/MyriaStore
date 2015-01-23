package demo;

import java.io.Serializable;

import javax.management.Descriptor;

import operations.Compare;
import operations.Get;
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
		public <M extends Linearizable> T runOp(Get<T, Linearizable, LinearizableStore, M> op) {
			return t;
		}

		@Override
		public <M extends Linearizable> void runOp(Put<T, Linearizable, LinearizableStore, M> op) {
			t = op.t;
		}
		
		public void runOp(CustomOp c){
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
	}
	public class DescrLinStoreObject<T extends Descriptor > 
		extends LinearizableStoreObject<T> {
		
		public DescrLinStoreObject(T t, LinearizableStore ls){
			super(t,ls);
		}

		public String runOp(
				GetFieldValue<T, Linearizable, Linearizable, LinearizableStore> op) {
			return (String) t.getFieldValue(op.arg);
		}
	}
	
	public class ComparableObject<T extends Serializable & Comparable<T>> 
		extends LinearizableStoreObject<T>{

		public ComparableObject(T t, LinearizableStore ls) {
			super(t, ls);
		}
		
		public Integer runOp(Compare<T, Linearizable, LinearizableStore, Linearizable> c, ComparableObject<T> o){
			return t.compareTo(o.t);
		}
		
	}
	
	@Override
	public <T extends Serializable> LinearizableStoreObject<T> newDumbObject(T t) {
		return new LinearizableStoreObject<T>(t,this);
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
