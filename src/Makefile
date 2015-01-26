all:
	ls -d */ | while read dir; do mkdir -p /tmp/workdir/"$$dir"; mkdir -p /tmp/"$$dir"; done; find . -name '*[.java,.h]' | while read name; do ln -f "$$name" /tmp/workdir/"$$name"; done; find . -name '*.java' | while read name; do cpp /tmp/workdir/"$$name" | sed s/'^#.*'//g > /tmp/"$$name"; done; cd /tmp/; javac -Xlint:unchecked Main.java; cd -
