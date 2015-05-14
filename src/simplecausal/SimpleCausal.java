package simplecausal;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
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
	
	private static ConcurrentNavigableMap<SafeInteger, ImmutableContainer<?>> master
		= new ConcurrentSkipListMap<>();
	private static ConcurrentNavigableMap<SafeInteger, SimpleCausal> replicas =
		new ConcurrentSkipListMap<>();

	private ConcurrentSkipListMap<SafeInteger, ImmutableContainer<?>> local = new ConcurrentSkipListMap<>();
	private final SafeInteger myID = SafeInteger.wrap(NonceGenerator.get().hashCode());

	ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

	public SimpleCausal(){
		replicas.put(myID, this);
		registerOnTick(this);
	}

	@Override
	public void run(){
		try{
			lock.writeLock().lock();
			synchronized(master){
				for (Map.Entry<SafeInteger, ImmutableContainer<?>> e : local.entrySet()){
					@SuppressWarnings("unchecked")
						Mergable<RCloneable<?>> elem = (Mergable<RCloneable<?>>) e.getValue().get();
					SafeInteger key = e.getKey();
					RCloneable<?> merged = elem.merge(master.get(key).readOnlyIpromise());
					@SuppressWarnings("unchecked")
						ImmutableContainer<?> formaster = new ImmutableContainer(merged);
					master.put(key, formaster);
				}
				for (Map.Entry<SafeInteger, ImmutableContainer<?>> e : master.entrySet()){
					local.put(e.getKey(),e.getValue());
				}
			}
		}
		finally{
			lock.writeLock().unlock();
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
		//TODO - simulate the need for this.  Right now it's not keenly felt.
		return false;
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
		public ImmutableContainer<T> b;

		public SimpleRemoteObject(SafeInteger name){
			@SuppressWarnings("unchecked")
			ImmutableContainer<T> t = (ImmutableContainer<T>) local.get(name);
			b = t;
			assert(b != null);
			a = name;
		}
		
		public SimpleRemoteObject(SafeInteger name, T t){
			try{
				lock.readLock().lock();
				assert(t != null);
				this.a = name;
				this.b = new ImmutableContainer<>(t);
				local.put(name,b);
			}
			finally{
				lock.readLock().unlock();
			}
		}

		@Override
		public SimpleCausal getStore()
		{ return SimpleCausal.this; }

		@Override
		public void put(T t){
			for (Function<SafeInteger, Void> f : onWrite)
				f.apply(a);
			b = new ImmutableContainer<>(t);
			try{
				lock.readLock().lock();
				local.put(a,b);
			}
			finally{
				lock.readLock().unlock();
			}
		}

		@Override
		public T get(){
			for (Function<SafeInteger, Void> f : onRead)
				f.apply(a);
			return b.get();
		}
		
	}

	private java.util.List<Function<SafeInteger, Void>> onRead = new CopyOnWriteArrayList<>();
	private java.util.List<Function<SafeInteger, Void>> onWrite = new CopyOnWriteArrayList<>();

	@Override
	public void registerOnRead(Function<SafeInteger, Void> f){
		onRead.add(f);
	}

	@Override
	public void registerOnWrite(Function<SafeInteger, Void> f){
		onWrite.add(f);
	}
	
}
