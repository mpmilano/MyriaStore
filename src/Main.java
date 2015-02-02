#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;
import operations.*;

#define hargs String, FSS_, access.ReadWrite, consistency.Lin
public class Main {
	public static void main(String[] args) throws java.io.IOException{
		FSStore fs = new FSStore();
		Handle<hargs> h = fs.newObject("run away!","/tmp/foofoofoo");
		System.out.println((new GetOp<>(h)).execute());
		System.out.println((new LinGet<>(h)).execute());
		(new PutOp<>(h, "newFoo")).execute();
		
	}
}
