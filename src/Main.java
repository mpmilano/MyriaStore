import remote.*;
import fsstore.*;
import logstore.*;
import simplecausal.*;
import operations.*;
import java.util.*;
import util.*;
import transactions.*;
import consistency.*;

// -6 

public class Main{


	
	public static void main(String[] args) throws Exception{

		Lin lin = new Lin(){};
		Causal cause = new Causal(){};
		FSStore fs = FSStore.inst;
		System.out.println((new GetOp<>(fs.newObject("foofoo","/tmp/foo",fs))).execute());
		for (String s : (new ListOp<>((fs.new DirFact<String>()).newObject("/")).execute())){
			System.out.println(s);
		}

		ArrayList<String> al = new ArrayList<>();
		al.add("TESTING TESTING");
		al.add("TESTING TESTING");
		
		for (String s : (new ListOp<>(fs.newObject(al, "/tmp/testing",fs)).execute())){
			System.out.println(s);
		}

		PrintFactory<String, consistency.Lin, Handle<String, consistency.Lin, access.ReadWrite, consistency.Lin, FSStore>> pf =
			new PrintFactory<>();


		System.out.println("using ForEach");
		(new ForEachOp<>(pf, (fs.new DirFact<String>()).newObject("/tmp/filesonly/"))).execute();
		

		((new InsertFactory<>(fs)).build((fs.new DirFact<String>()).newObject("/tmp/fooey"), fs.newObject("poopoo","/tmp/poopoo",fs))).execute();
		(new ForEachOp<>(pf, (fs.new DirFact<String>()).newObject("/tmp/fooey/"))).execute();
		
		Handle h = null;
		Store s = null;
		GetOp go = null;
		ListOp lo = null;
		Gets g = null;

		logstore.LogStore ls;
		(new blog.Blog<>(fs.newObject(new ArrayList<blog.BlogEntry>(), fs)))
			.postNewEntry(fs,"This is an entry!")
			.addComment(fs.ifact(), 3, "A COMMENT!");


		TestCrossStore tcs = new TestCrossStore();
	}

}


class TestCrossStore {

	private class SimpleCounter implements java.io.Serializable{
		private int i = 0;
		public void incr() {++i;}
		public void decr() {--i;}
		public int get() {return i;}
	}

	Runnable r = new Runnable(){
			SimpleNameManager snm = new SimpleNameManager();
			
			@Override
			public void run(){
				IndirectStore<Causal, SafeInteger, Void> cross
					= new IndirectStore<>
					(new CrossStore<>
					 (new SimpleCausal(), snm,
					  FSStore.inst, FSStore.inst));

				//cross.newObject(new SimpleCounter(), cross);
			}
		};
	
	public TestCrossStore(){
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
	}
}
