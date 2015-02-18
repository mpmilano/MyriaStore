package logstore;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Collection;


public class LogStore extends Store<Causal, LogStore.LogObject<?>, Void, LogStore>
	implements Insert<LogStore.LogObject<? extends Collection<? extends Serializable>>, LogStore.LogObject<?>>{

	LinkedList<LinkedList<Runnable>> log = new LinkedList<>();

	public static LogStore inst = new LogStore();
	
	private LogStore(){}

	@Override
	protected Void genArg(){
		return null;
	}


	static class LogObject<T extends Serializable> implements RemoteObject<T> {

		private T t;

		private LogObject(T t) {this.t = t;}
		
		@Override
		public LogStore getStore() {return inst;}

		@Override
		public void put(final T t){
			inst.log.getLast().add(new Runnable(){
					public void run(){
						LogObject.this.t = t;
					}
				});
		}

		@Override
		public T get(){ return t; }
	}

	@Override
	protected <T extends Serializable> LogObject<?> newObject(Void nill, T initial){
		return new LogObject<>(initial);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void insert(final LogObject<? extends Collection<? extends Serializable> > lo1, final LogObject<?> lo2){
		log.getLast().add(new Runnable(){
			public void run(){
				((LogObject)lo1).t = ((LogObject)lo2).t;
			}
			});
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void insert(final LogObject<? extends Collection<? extends Serializable> > lo1, final Serializable lo2){
		log.getLast().add(new Runnable(){
				public void run(){
					((LogObject)lo1).t = lo2;
				}
			});
	}

	@Override
	public InsertFactory<?,?,?> ifact() {return new InsertFactory<>(this); }

}
