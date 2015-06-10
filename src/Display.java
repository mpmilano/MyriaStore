import access.*;
import blog.*;
import consistency.*;
import fsstore.*;
import logstore.*;
import operations.*;
import remote.*;
import simplecausal.*;
import testcrossstore.*;
import transactions.*;
import util.*;
import vectorclockgc.*;
import java.io.*;

public class Display {

	public static void main(String[] args) throws Exception{
		System.out.println(FileOps.readFromFS(new File(args[0])).toString());
	}
	
	
}
