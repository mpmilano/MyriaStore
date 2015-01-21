package demo;

import java.io.Serializable;

import operations.BotherSomeSyntax;
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
			RemoteObject<T> {
		
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
		protected <T2 extends Serializable> RemoteObject<T2> newRef(T2 t) {
			//used in a (very) safe way.
			@SuppressWarnings("unchecked")
			RemoteObject<T2> thisp = 
			(RemoteObject<T2>)(this);
			if (this.t == t) return thisp;
			else throw new RuntimeException("called from invalid context");
		}
	}
	public class ComparableLinStoreObject<T extends BotherSomeSyntax<T> > 
		extends LinearizableStoreObject<T> {
		
		public ComparableLinStoreObject(T t, LinearizableStore ls){
			super(t,ls);
		}

		public Integer runOp(
				Compare<T, Linearizable, Linearizable, LinearizableStore> op,
				remote.BackingStore<Linearizable, LinearizableStore>.RemoteObject<T> ro2q) {
			//this is safe, because LinearizableStore has only one RemoteObject.  
			//Yay type-level This pointers!
			LinearizableStoreObject<T> ro2 = (LinearizableStoreObject<T>) ro2q;
			return null;
		}
	}
	
	@Override
	public <T extends Serializable> LinearizableStoreObject<T> newDumbObject(T t) {
		return new LinearizableStoreObject<T>(t,this);
	}
	
	public <T extends BotherSomeSyntax<T>> Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore> 
	newObject(T t) {
		return new Handle<T,handles.access.ReadWrite,Linearizable,Linearizable,LinearizableStore>(new ComparableLinStoreObject<>(t,this));
	}

}
