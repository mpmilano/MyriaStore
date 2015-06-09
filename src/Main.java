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
		TestCrossStore tcs = new TestCrossStore();
	}

}


class TestCrossStore {

	private static Object synchlock = "";
	
	private class SimpleCounter implements CausalSafe<SimpleCounter>,
										   Incrementable
	{
		private TreeSet<TwoTuple<String, Boolean>> i = new TreeSet<>();

		@Override
		public void incr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),true));
			synchronized(synchlock){
				System.out.println("incr: " + Thread.currentThread().getName());
				for (TwoTuple<String,Boolean> tt : i){
					System.out.println("  " + tt.a + ": " + tt.b);
				}
			}
		}

		@Override
		public void decr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),false));
		}
		public int get() {
			int i = 0;
			for (TwoTuple<String, Boolean> e : this.i){
				if (e.b) ++i;
				else --i;
			}
			return i;
		}

		@Override
		public SimpleCounter merge(SimpleCounter c){
			//System.out.println("merge");
			if (c != null) i.addAll(c.i);
			return this;
		}

		@Override
		public SimpleCounter rclone(){
			SimpleCounter sc = new SimpleCounter();
			sc.i.addAll(i);
			return sc;
		}

		@Override
		public String toString(){
			String ret =  get() + "";
			for (TwoTuple<String,Boolean> tt : i){
				ret += ("  " + tt.a + ": " + tt.b);
			}
			return ret;
		}
	}

	SafeInteger name = SafeInteger.ofString(NonceGenerator.get());
	IndirectStore<Causal, SafeInteger, SafeInteger> cross
		= new IndirectStore<>
		(new SimpleCausal());
		/*(new CrossStore<>
		 (new SimpleCausal(), (new SimpleNameManager()),
		 FSStore.inst, FSStore.inst));*/
	Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,IndirectStore<Causal, SafeInteger, SafeInteger>> hmaster =
		cross.newObject(new SimpleCounter(),
						name,
						cross);


	
	Runnable r = new Runnable(){
			SimpleNameManager snm = new SimpleNameManager();
			IncrementFactory incrfact = new IncrementFactory();
			
			@Override
			public void run(){
				final IndirectStore<Causal, SafeInteger, SafeInteger> cross
					= new IndirectStore<>
					(new SimpleCausal());
					/*(new CrossStore<>
					 (new SimpleCausal(), snm,
					 FSStore.inst, FSStore.inst));*/
				cross.tick();
				Handle<SimpleCounter,consistency.Causal,access.ReadWrite,?,?> h;
				cassert(cross.objectExists((SafeInteger)hmaster.ro.name()),"failure: cross tick did not receive master object by name.");
				try{
					synchronized(hmaster){
						h = hmaster.getCopy(cross);
					}
				}
				catch(MyriaException e){
					h = null;
					System.err.println("failure: cross tick did not receive master object by name.");
					throw new RuntimeException(e);
				}

				assert((cross.newObject(new SimpleCounter(),
										SafeInteger.ofString(NonceGenerator.get()),
										cross)).ro.get() != null);
				
				incrfact.build(h).execute();
				incrfact.build(h).execute();
				(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(h)).execute();
				cross.tick();
			}
		};
	
	public TestCrossStore(){
		try{
			cross.tick();
			Thread t1 = new Thread(r);
			t1.start();
			Thread t2 = new Thread(r);
			new Thread(r).start();/*
									new Thread(r).start();
									new Thread(r).start();
									new Thread(r).start();
									new Thread(r).start(); //*/
			t1.join();
			t2.join();
			System.out.println("threads done");
			cross.tick();
			(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(hmaster)).execute();
		}
		catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
