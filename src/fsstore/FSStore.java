#include "FSStore.h"
package fsstore;
import remote.*;
import access.*;
import operations.*;
import java.io.*;
	
public class FSStore extends FSS_t implements Get<FSStore.FSObject<?>> {
	public class FSObject<R_(T)> extends FSS_t.RemoteObject<R_g(T)> {

		private final File location;
		private final Class<T> storedclass;
		
		public FSObject(String location, R_g(T) initialValue) throws IOException {
			@SuppressWarnings("unchecked")
				Class<T> class1 = (Class<T>) initialValue.getClass();
			this.storedclass = class1;
			this.location = new File(location);
			FileOutputStream fos = null;
			ObjectOutputStream oos = null;
			try {
				this.location.createNewFile();
				fos = new FileOutputStream(this.location);
				oos = new ObjectOutputStream(fos);
				oos.writeObject(initialValue);
			}
			finally {
				if (oos != null) oos.close();
				if (fos != null) fos.close();
			}
		}

		@Override
		public FSStore getStore(){
			return FSStore.this;
		}

		@Override
		public Class<T> getUnderlyingClass(){
			return storedclass;
		}

		
	}
	
	@Override
	public <R_(T)> Handle<T, FSS_, FSObject<T>, ReadWrite, consistency.Lin> newObject(R_g(T) init, String name){
		try {
			return new Handle<>(new FSObject<R_g(T)>(name, init));
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public <R_(T)> R_g(T) getObj(Class<T> c, FSObject<?> o){
		try {
			return (R_g(T)) (new ObjectInputStream(new FileInputStream(o.location))).readObject();
		}
		catch (IOException e){
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
	
