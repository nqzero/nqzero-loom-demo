# Project Loom

Project Loom is an oracle-sponsored project attempting to add continuations to the JVM.
this is similar to what kilim does

http://openjdk.java.net/projects/loom/


# Prototype

a prototype was recently released:

http://mail.openjdk.java.net/pipermail/loom-dev/2018-July/000061.html


# This Demo

this demo includes a "hello world" and a xorshift implementation ported from `kilim Continuations` to `Project Loom Continuations`

# Status

project loom continuations work, but they appear to be quite slow. for the xorshift example, kilim takes approx 30 nanos per op and loom takes approx 18000 nanos per op (linux on an i3)

it's still early in the project lifecycle, so it's possible that performance will improve.
if so, it might make sense to use the Project Loom Continuations in kilim instead of bytecode weaving.
however, loom is much more ambitious than kilim in terms of what code can yield, and as a result performance may be fundamentally limited




