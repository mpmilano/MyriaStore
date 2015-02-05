#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;
import operations.*;
import java.io.*;

#define hargs String, FSS_, access.ReadWrite, consistency.Lin
public class Main {
	public static void main(String[] args) throws java.io.IOException{
		FSStore fs = new FSStore();

		
		Handle<hargs> h = fs.newObject("run away!","/tmp/foofoofoo");
		System.out.println((new GetOp<>(h)).execute());



		
		System.out.println((new LinGet<>(h)).execute());
		(new PutOp<>(h, "newFoo")).execute();
		System.out.println((new LinGet<>(h)).execute());
		System.out.println("finished");
		(new ReplaceOp<>(h,h)).execute();
		Handle<Serializable, FSS_, access.ReadWrite, consistency.Lin> rel = Handle.relaxT(h);
		Handle<Serializable, FSS_, access.Write, consistency.Lin> op = Handle.changeDown(rel);
		Handle<Serializable, FSS_, ?, ?> op2 = Handle.changeUp(rel);
		Handle<Serializable, FSS_, access.ReadWrite, consistency.Lin> orig = op2.restore();
		Handle<Serializable, FSSp_, FSStore.FSDir, access.ReadWrite, consistency.Lin> dir = fs.df.newObj("/tmp/");
		for (String s : (new ListOp<>(dir)).execute()) 	System.out.println(s);
		(new SwapOp<>(h,h)).execute();

		OperationFactory of = null;
		Repeat rp = null;
		
	}
}
