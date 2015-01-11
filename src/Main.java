import operations.Get;
import operations.Put;
import handles.access.ReadWrite;
import consistency.Linearizable;
import demo.CustomOp;
import demo.LinearizableStore;
import remote.Handle;
import remote.ObjectManager_impl;


public class Main {
	public static void main(String[] args){
		ObjectManager_impl om = new ObjectManager_impl();
		LinearizableStore ln = new LinearizableStore();
		om.registerStore(ln);
		
		Handle<Integer, ReadWrite, Linearizable, Linearizable> o = 
				om.newObject(ln.getModel(), new Integer(3));
		new Put<>(o,new Get<>(o).execute() + 4).execute();
		System.out.println(new Get<>(o).execute());
		new CustomOp(o).execute();
	}

}
