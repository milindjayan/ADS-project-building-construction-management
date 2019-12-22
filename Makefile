
# define a makefile variable

JC = javac

# define a makefile variable for compilation flags

JFLAGS = -g

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
    risingCity.java

default: classes

classes: $(CLASSES:.java=.class)

#print make clean to start from scratch
clean:
	$(RM) *.class
	$(RM) */*.class
