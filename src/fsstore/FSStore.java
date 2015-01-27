#include "FSStore.h"
package fsstore;
import remote.*;
import access.*;
import java.io.*;
	
public class FSStore extends FSS_t /* implements Get<FSS_>, Put<FSS_>, Print<FSS_> */{
	public class FSObject<R_(T)> extends FSS_t.RemoteObject<R_g(T)> {

		private final File location;
		
		public FSObject(String location, R_g(T) initialValue) throws IOException {
			this.location = new File(location);
			this.location.createNewFile();
			new ObjectOutputStream(new FileOutputStream(this.location)).writeObject(initialValue);
		}
	}

	@Override
	public <R_(T)> Handle<T, FSS_, ReadWrite, consistency.Lin> newObject(R_g(T) init, String name){
		try {
			return new Handle<>(new FSObject<R_g(T)>(name, init));
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public FSStore getStore(){
		return FSStore.this;
	}

	@SuppressWarnings("unchecked")
	public <R_(T)> R_g(T) getObj(FSObject<R_g(T)> o){
		try {
			return (R_g(T)) (new ObjectInputStream(new FileInputStream(o.location))).readObject();
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}
}
