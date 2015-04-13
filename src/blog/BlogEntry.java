package blog;
import remote.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedList;
import operations.*;
import access.*;
import consistency.*;


public class BlogEntry implements Serializable{
	Handle<String,Lin, ReadWrite, ?, ?> text;
	Handle<ArrayList<Comment>, Causal, ReadWrite, ? ,?> comments = null;
	public BlogEntry(Store<Lin,?,?,?> s, String entry){
		this.text = s.newObject(entry);
	}

	public void addComment(InsertFactory<?,?,?> ifact, int time, String str){
		ifact.build2(comments, new Comment(time, str));
	}
}
