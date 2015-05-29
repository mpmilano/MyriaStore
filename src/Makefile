OBall:
	@make clean; ls -d */ | while read dir; do mkdir -p /tmp/workdir/"$$dir"; mkdir -p /tmp/outdir/src/"$$dir"; done; find . -name '*[.java,.h]' | while read name; do if [[ ! -d "$$name" ]]; then cp "$$name" /tmp/workdir/"$$name"; fi; done; find . -name '*.java' | while read name; do cpp /tmp/workdir/"$$name" | sed s/'^#.*'//g | sed s/'access.Unknown & access.Unknown'/'access.Unknown'/g> /tmp/outdir/src/"$$name"; done; cd /tmp/outdir/src/; javac -Xlint:unchecked Main.java; cd -;

run: 
	@cd /tmp/outdir/src/; java -ea Main; cd -; 

clean:
	rm -rf /tmp/workdir; rm -rf /tmp/outdir
