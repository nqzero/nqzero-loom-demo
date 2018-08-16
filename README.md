# Project Loom

Project Loom is an oracle-sponsored project attempting to add continuations to the JVM.
this is similar to what kilim does

http://openjdk.java.net/projects/loom/

a first prototype was recently released:

http://mail.openjdk.java.net/pipermail/loom-dev/2018-July/000061.html


# This Demo

this demo is a xorshift implementation ported from `kilim Continuations` to `Project Loom Continuations`. an equivalent kilim version is in `kilim`. note: the two versions are nearly identical

# Status

project loom continuations work, but they appear to be quite slow. for the xorshift example, kilim takes approx 30 nanos per op and loom takes approx 18000 nanos per op (linux on an i3)

project loom is not mature yet, so performance will improve.
if it gets competitive, it would make sense to use the Project Loom Continuations in kilim instead of bytecode weaving.
however, loom is much more ambitious than kilim in terms of what code can yield, and as a result performance may be fundamentally limited


# Running

loom:
`$loom/bin/java Xorshift.java 100000 10`


kilim:
`mvn -f kilim/pom.xml compile exec:java -Dexec.mainClass=Xorshift clean`



