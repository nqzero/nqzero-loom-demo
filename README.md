# Project Loom

Project Loom is an oracle-sponsored project attempting to add continuations to the JVM.
this is similar to what kilim does

http://openjdk.java.net/projects/loom/

a first prototype was released in 2018:

http://mail.openjdk.java.net/pipermail/loom-dev/2018-July/000061.html

after that, shipilev maintained builds until june 2019.
currently, there are no public builds avaialable and loom must be built from source.
the results below are based on `55911:d8b1636c4512` from 25 june 2019.

# This Demo

this demo is a Xorshift implementation ported from `kilim Continuations` to `Project Loom Continuations`. an equivalent kilim version is in `kilim`. note: the two versions are nearly identical

there's also a XorDeep variant that exercises deeper stacks that was contributed by `Hamlin Li <huaming.li@oracle.com>`
and is used with his permission (via email).
loom includes an optimization that copies only the head of the stack, so this demo highlights that benefit.


# Status

project loom continuations work.
initially, loom was approximately 600 times slower than kilim for Xorshift,
and had a break-even depth of 170 for XorDeep (using an i3).
As of June 2019, this has improved to 17x slower with a break-even depth of 80 (using an i5).
This is not a proper benchmark, and loom and kilim have different goals and requirements,
so these numbers are intended to be a rough gauge only.

project loom is not mature yet, so performance will likely continue to improve.
if it gets competitive, it would make sense to use the Project Loom Continuations in kilim instead of bytecode weaving.
however, loom is much more ambitious than kilim in terms of what code can yield, and as a result performance may be fundamentally limited


# Running

by default `Xorshift` does a 5 second warmup and then 10 reps of 200000 rotations

- loom: `$loom/bin/java Xorshift.java`
- java 12: `$java12/bin/java -cp $cp kilim/Xorshift.java`
- XorDeep: `$loom/bin/java XorDeep.java 200000 10 80`

note: `cp=$(mvn -f kilim/pom.xml -q dependency:build-classpath -Dmdep.outputFile=/dev/fd/1)`


results:

```
# Project Loom
loom : 522.98     nanos/op,           -1379446094540640482
loom : 523.56     nanos/op,            5204491086981418152
loom : 525.66     nanos/op,            8246585696269541781
loom : 525.19     nanos/op,           -6223228706767765329
loom : 525.14     nanos/op,            6319753122833807335


# kilim with java 12
kilim: 30.03      nanos/op,           -1379446094540640482
kilim: 30.09      nanos/op,            5204491086981418152
kilim: 30.13      nanos/op,            8246585696269541781
kilim: 30.51      nanos/op,           -6223228706767765329
kilim: 30.71      nanos/op,            6319753122833807335
```



# command line flags

loom can disable certain checks, which results in faster but potentially unsafe code. try:

```
loomflags="-XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:+UseNewCode -XX:-DetectLocksInCompiledFrames"
$loom/bin/java $loomflags Xorshift.java
```

currently these flags don't appear to significantly affect performance for this example.





