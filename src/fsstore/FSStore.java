#include "FSStore.h"
package fsstore;
import remote.*;
import access.*;
import operations.*;
import java.io.*;
import consistency.*;
	
public class FSStore extends FSS_t implements Replace<FSStore.FSObject>,
											  List<FSStore.FSDir>,
											  Swap<FSStore.FSObject>
											  ,ForEach<consistency.Lin, FSStore.FSDir>
{
	
	public class FSObject extends FSS_t.RemoteObject {

		protected final File location;
		protected final Class<?> storedclass;
		
		private FSObject(String location, Object initialValue) throws IOException {
			Class<?> class1 = (initialValue == null ? Void.class : initialValue.getClass());
			this.storedclass = class1;
			this.location = new File(location);
			if (initialValue != null){
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
		}

		private class Lin implements consistency.Lin {}
		@Override
		public consistency.Consistency getModel(){
			return new Lin();
		}

		@Override
		public void setModel(consistency.Consistency c) {}

		@Override
		public FSStore getStore(){
			return FSStore.this;
		}

		@Override
		public Class<?> getUnderlyingClass(){
			return storedclass;
		}

		@Override
		public Object get(){
			return getObj(this);
		}

		@Override
		public <R_(T)> void put(T o){
			putObj(this,o);
		}
	}

	public class FSDir extends FSObject {
		private FSObject[] files;
		private FSDir(String location) throws IOException {
			super(location,null);
			if (! this.location.isDirectory()) throw new IOException("must be a dir!");
			String[] fls = this.location.list();
			files = new FSObject[fls.length];
			for (int i = 0; i < files.length; ++i) files[i] = new FSObject(fls[i],null);
			
		}
	}

	@Override
	public String[] list(FSDir fs){
		return fs.location.list();
	}


	@Override
	public <R_(T), Access_(A)>
		void foreach(OperationFactory<T, Lin, RemHandle<T,Lin,A,Lin> > of, FSDir fs){
		for (FSObject f : fs.files){
			Handle<T,Lin,?,FSStore,FSObject,A,Lin> h = (new FSObjFact<T,A>()).buildHandle(f);
			
			of.build(h).execute();
		}
	}


	public class DirFact extends FSS_t.AltObjFact<Serializable, FSDir> {

		public Handle<Serializable, FSSp_, FSDir, access.ReadWrite, consistency.Lin> newObj(String arg){
			try {
				return buildHandle(new FSDir(arg));
			}
			catch (Exception e){
				throw new RuntimeException(e);
			}
		}
	}

	protected class FSObjFact<R_(T), Access_(A)> extends FSS_t.ObjFact<T,A> {
		
	}
	
	public DirFact df = new DirFact();

	@Override
	protected FSObject newObj(String name, Object init) throws IOException{
		return new FSObject(name, init);
	}

	@Override
	public Object getObj(FSObject o){
		try {
			return (new ObjectInputStream(new FileInputStream(o.location))).readObject();
		}
		catch (IOException e){
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public <R_(T)> void putObj(FSObject o, T t){
		try {
			(new ObjectOutputStream(new FileOutputStream(o.location))).writeObject(t);
		}
		catch (IOException e){
			throw new RuntimeException(e);
		}
	}

	@Override
	public void replace(FSObject dest, FSObject src) throws java.io.IOException{
		java.nio.file.Files.copy(src.location.toPath() , dest.location.toPath() );
	}

	@Override
	public void swapObj(FSObject o1, FSObject o2) throws IOException{
		File tmp = File.createTempFile("aaaa","bbbb");
		o1.location.renameTo(tmp);
		o1.location.renameTo(o2.location);
		o2.location.renameTo(tmp);
		
	}
}
	
