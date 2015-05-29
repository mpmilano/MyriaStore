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

	private class SimpleCounter implements CausalSafe<SimpleCounter>,
										   Incrementable
	{
		private TreeSet<TwoTuple<String, Boolean>> i = new TreeSet<>();

		@Override
		public void incr() {
			i.add(new TwoTuple<>(NonceGenerator.get(),true));
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
			if (c != null) i.addAll(c.i);
			return this;
		}

		@Override
		public SimpleCounter rclone(){
			SimpleCounter sc = new SimpleCounter();
			for (TwoTuple<String, Boolean> i : this.i){
				if (i.b) sc.incr();
				else sc.decr();
			}
			return sc;
		}

		@Override
		public String toString(){
			return get() + "";
		}
	}

	SafeInteger name = SafeInteger.ofString(NonceGenerator.get());
	IndirectStore<Causal, SafeInteger, Void> cross
		= new IndirectStore<>
		(new CrossStore<>
		 (new SimpleCausal(), (new SimpleNameManager()),
		  FSStore.inst, FSStore.inst));
	Handle<SimpleCounter,consistency.Causal,access.ReadWrite,consistency.Causal,IndirectStore<Causal, SafeInteger, Void>> hmaster =
		cross.newObject(new SimpleCounter(),
						name,
						cross);


	
	Runnable r = new Runnable(){
			SimpleNameManager snm = new SimpleNameManager();
			IncrementFactory incrfact = new IncrementFactory();
			
			@Override
			public void run(){
				final IndirectStore<Causal, SafeInteger, Void> cross
					= new IndirectStore<>
					(new CrossStore<>
					 (new SimpleCausal(), snm,
					  FSStore.inst, FSStore.inst));

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
				cross.tick();
				incrfact.build(h).execute();
				(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(h)).execute();
				cross.tick();
			}
		};
	
	public TestCrossStore(){
		cross.tick();
		new Thread(r).start();
		new Thread(r).start();/*
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start();
		new Thread(r).start(); //*/
		cross.tick();
		(new PrintFactory<consistency.Causal>()).build(Handle.readOnly(hmaster)).execute();
	}
}
