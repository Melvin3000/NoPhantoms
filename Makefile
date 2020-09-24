SPIGOT=../spigot-1.15.2.jar
JAVA=1.8
PLUGIN=NoPhantoms
TARGET_DIR=com

JFLAGS = -Xlint:all -classpath $(SPIGOT) -d ./ -source $(JAVA) -target $(JAVA)
JC = javac
SOURCEFILES = $(wildcard src/com/Melvin3000/NoPhantoms/*.java)

default: jar_file

class_files:
	$(JC) $(JFLAGS) $(SOURCEFILES)

jar_file: class_files
	jar -cf ./$(PLUGIN).jar ./*


clean:
	rm -f *.jar
	rm -rf $(TARGET_DIR)