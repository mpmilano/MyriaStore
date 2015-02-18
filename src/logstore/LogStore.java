package logstore;

import consistency.*;
import operations.*;
import access.*;
import remote.*;
import java.io.Serializable;
import java.util.LinkedList;


public class LogStore extends Store<Causal, LogStore.LogObject<?>, Void, LogStore> {

	LinkedList<LinkedList<Runnable>> log = new LinkedList<>();

	@Override
	protected Void genArg(){
		return null;
	}


	public class LogObject<T extends Serializable> implements RemoteObject<T> {

		private T t;

		private LogObject(T t) {this.t = t;}
		
		@Override
		public LogStore getStore() {return LogStore.this;}

		@Override
		public void put(final T t){
			log.getLast().add(new Runnable(){
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

}
