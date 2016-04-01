all:
	find . -name "*.java" | xargs javac
clean:
	find . -name "*.class" -type f -print0 | xargs -0 rm -f
run:
	find . -name "*.java" | xargs javac
	java root.Main call_graph
