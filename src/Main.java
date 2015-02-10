import remote.*;
import fsstore.*;
import operations.*;
import java.util.*;

public class Main{
	public static void main(String[] args) throws Exception{
		FSStore fs = new FSStore();
		System.out.println((new GetOp<>(fs.newObject("foofoo","/tmp/foo"))).execute());
		for (String s : (new ListOp<>((fs.new DirFact<String>()).newObject("/tmp")).execute())){
			System.out.println(s);
		}

		ArrayList<String> al = new ArrayList<>();
		al.add("TESTING TESTING");
		al.add("TESTING TESTING");
		
		for (String s : (new ListOp<>(fs.newObject(al, "/tmp/testing")).execute())){
			System.out.println(s);
		}
		Handle h = null;
		Store s = null;
		GetOp go = null;
		ListOp lo = null;
		
	}
}
