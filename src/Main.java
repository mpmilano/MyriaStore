#include "fsstore/FSStore.h"
import fsstore.*;
import remote.*;

public class Main {
	public static void main(String[] args){
		FSStore fs = new FSStore();
		Handle<String, FSS_, access.ReadWrite, consistency.Lin> h = fs.newObject("/tmp/tmpobj","/tmp/tmpobj");
		
		
	}
}
