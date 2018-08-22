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

by default `Xorshift` does a 5 second warmup and then 10 reps of 200000 rotations

- loom: `$loom/bin/java Xorshift.java`
- java 11: `$java11/bin/java -javaagent:${cp/:*} -cp $cp kilim/Xorshift.java`
- java 8: `mvn -f kilim/pom.xml compile exec:java -Dexec.mainClass=Xorshift`

note: `cp=$(mvn -f kilim/pom.xml -q dependency:build-classpath -Dmdep.outputFile=/dev/fd/1)`


results:
```
# Project Loom
loom : 17683.30   nanos/op,           -1379446094540640482
loom : 17591.34   nanos/op,            5204491086981418152
loom : 17531.60   nanos/op,            8246585696269541781
loom : 17894.27   nanos/op,           -6223228706767765329
loom : 17533.77   nanos/op,            6319753122833807335

# kilim with java 11
kilim: 43.16      nanos/op,           -1379446094540640482
kilim: 42.58      nanos/op,            5204491086981418152
kilim: 41.19      nanos/op,            8246585696269541781
kilim: 45.00      nanos/op,           -6223228706767765329
kilim: 42.60      nanos/op,            6319753122833807335

# kilim with java 8
kilim: 32.21      nanos/op,           -1379446094540640482
kilim: 30.10      nanos/op,            5204491086981418152
kilim: 29.82      nanos/op,            8246585696269541781
kilim: 29.85      nanos/op,           -6223228706767765329
kilim: 30.45      nanos/op,            6319753122833807335
```


