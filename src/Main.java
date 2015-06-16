#define cassert(x,s) assert((new util.Function<Void,Boolean>(){@Override public Boolean apply(Void v){ if (!(x)) throw new RuntimeException(s); return true; }}).apply(null));

import remote.*;
import fsstore.*;
import logstore.*;
import simplecausal.*;
import operations.*;
import java.util.*;
import util.*;
import transactions.*;
import consistency.*;
import java.io.*;
import testcrossstore.*;

// -6 

public class Main{


	
	public static void basics(String[] args) throws Throwable{

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

		PrintFactory<consistency.Lin> pf =
			new PrintFactory<>();


		System.out.println("using ForEach");
		OperationFactory<Void,consistency.Lin,Handle<?,consistency.Lin,access.Read,?,?> > pf1 = pf;
		(new ForEachOp<>(pf1, (fs.new DirFact<String>()).newObject("/tmp/filesonly/"))).execute();
		

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

	}

	public static void main(String[] args){
		//TestCrossStore tcs = new TestCrossStore();

		scs.StressCrossStore scs = new scs.StressCrossStore();
	}

}


