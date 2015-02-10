all:
	@ls -d */ | while read dir; do mkdir -p /tmp/workdir/"$$dir"; mkdir -p /tmp/outdir/src/"$$dir"; done; find . -name '*[.java,.h]' | while read name; do if [[ ! -d "$$name" ]]; then ln -f "$$name" /tmp/workdir/"$$name"; fi; done; find . -name '*.java' | while read name; do cat /tmp/workdir/"$$name" > /tmp/outdir/src/"$$name"; done; cd /tmp/outdir/src/; javac -Xlint:unchecked Main.java; cd -
