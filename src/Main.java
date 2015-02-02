#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;
import operations.*;

public class Main {
	public static void main(String[] args) throws java.io.IOException{
		FSStore fs = new FSStore();
		#define hargs String, FSS_, access.ReadWrite, consistency.Lin
		Handle<hargs> h =
			new Handle<hargs>(String.class, fs.new FSObject("/tmp/tmpobj","/tmp/tmpobj"));
		(new GetOp<hargs>(h)).execute();
		
	}
}
