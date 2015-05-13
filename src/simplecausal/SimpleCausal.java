package simplecausal;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;
import java.util.*;
import java.io.*;
import util.*;

public class SimpleCausal
	extends Store<Causal,
					  SimpleCausal.SimpleRemoteObject<?>,
					  SafeInteger, SafeInteger, SimpleCausal>
	implements HasClock, AccessReplica<Causal,
										   SimpleCausal.SimpleRemoteObject<?>
										   , SafeInteger,
										   SafeInteger,
										   SimpleCausal>,
			   Synq<SafeInteger>, Runnable {
	
	private static HashMap<SafeInteger, RCloneable<?>> master
		= new HashMap<>();
	private static HashMap<SafeInteger, SimpleCausal> replicas =
		new HashMap<>();

	private HashMap<SafeInteger, RCloneable<?>> local = new HashMap<>();
	private SafeInteger myID = SafeInteger.wrap(NonceGenerator.get().hashCode());

	public SimpleCausal(){
		replicas.put(myID, this);
		registerOnTick(this);
	}

	public void run(){
		synchronized(master){
			for (Map.Entry<SafeInteger, RCloneable<?>> e : local.entrySet()){
				@SuppressWarnings("unchecked")
				Mergable<RCloneable<?>> elem = (Mergable<RCloneable<?>>) e.getValue().rclone();
				SafeInteger key = e.getKey();
				RCloneable<?> merged = elem.merge(master.get(key));
				master.put(key, merged);
			}
			for (Map.Entry<SafeInteger, RCloneable<?>> e : master.entrySet()){
				local.put(e.getKey(),(RCloneable<?>)e.getValue().rclone());
			}
		}
	}

	@Override
	public Timestamp currentTime(){
		return new Timestamp(System.currentTimeMillis(), 10);
	}

	@Override
	public SimpleCausal access_replica(SafeInteger i){
		return replicas.get(i);
	}

	@Override
	public boolean sync_req(SafeInteger from, SafeInteger to){
		access_replica(from).run();
		access_replica(to).run();
		return true;
	}

	@Override
	public SafeInteger this_replica(){
		return myID;
	}

	@Override
	public SafeInteger genArg(){
		return SafeInteger.wrap(NonceGenerator.get().hashCode());
	}
	
	@Override
	@SuppressWarnings("unchecked")	
	protected <T extends Serializable> SimpleRemoteObject<?> newObject(SafeInteger i){
		return new SimpleRemoteObject(i);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <T extends Serializable> SimpleRemoteObject<?>
		newObject(SafeInteger i, T t){
		return new
			SimpleRemoteObject(i,CloneFromMergable.wrap(MakeMerge.wrap(t)));
	}

	public class SimpleRemoteObject
		<T extends Serializable &
				   Mergable<T> &
				   RCloneable<T>> 
		implements RemoteObject<T>{

		public SafeInteger a;
		public T b;

		public SimpleRemoteObject(SafeInteger name){
			@SuppressWarnings("unchecked")
			T t = (T) local.get(name);
			b = t;
			assert(b != null);
			a = name;
		}
		
		public SimpleRemoteObject(SafeInteger name, T t){
			assert(t != null);
			local.put(name,t);
			this.a = name;
			this.b = t;
		}

		public SimpleCausal getStore()
		{ return SimpleCausal.this; }

		@Override
		public void put(T t){
			for (Function<SafeInteger, Void> f : onWrite)
				f.apply(a);
			b = t;
			assert(local.get(a) == this);
		}

		@Override
		public T get(){
			for (Function<SafeInteger, Void> f : onRead)
				f.apply(a);
			return b;
		}
		
	}

	private LinkedList<Function<SafeInteger, Void>> onRead = new LinkedList<>();
	private LinkedList<Function<SafeInteger, Void>> onWrite = new LinkedList<>();

	@Override
	public void registerOnRead(Function<SafeInteger, Void> f){
		onRead.add(f);
	}

	@Override
	public void registerOnWrite(Function<SafeInteger, Void> f){
		onWrite.add(f);
	}
	
}
