all:
	find . -name '*.java' | while read name; ln "$name" /tmp/foofoofoo; cpp /tmp/foofoofoo | sed s/'^#.*'//g > "$name"; rm /tmp/foofoofoo; javac Main.java
