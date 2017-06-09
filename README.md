# clojure-repl

Very simple project skeleton for running the REPL and doing experiments.

## Adding dependency 

Latest version: 
[![Clojars Project](https://img.shields.io/clojars/v/curiousprogrammer/clojure-repl.svg)](https://clojars.org/curiousprogrammer/clojure-repl)

Add `clojure-repl` as a dependency to your profiles.clj or project.clj.
Ideally, into the `:repl` profile to not interfere it with other tasks:
```
 :repl {:dependencies [[curiousprogrammer/clojure-repl "x.y.z"]]}}
```

## Libraries

### [criterium](https://github.com/hugoduncan/criterium)

Can be used to do quick micro-benchmarks.
Check https://github.com/hugoduncan/criterium#usage.

### [alembic](https://github.com/pallet/alembic)

Can be used to add a new dependency to classpath dynamically without restarting the REPL.
It's used internaly by [nrepl-refactor](https://github.com/clojure-emacs/refactor-nrepl/blob/a425a8103413fe91f56907857c2043c32b3630a2/src/refactor_nrepl/artifacts.clj#L111).

Can also be used to invoke leiningen tasks programatically: https://github.com/pallet/alembic#invoking-leiningen-tasks

### JOL (Java Object Layout)

Handy library and command line tool for analyzing java objects' layout and estimating their size.
Check http://openjdk.java.net/projects/code-tools/jol/.

Check also examples: http://hg.openjdk.java.net/code-tools/jol/file/tip/jol-samples/src/main/java/org/openjdk/jol/samples/

The basic example: http://hg.openjdk.java.net/code-tools/jol/file/018c0e12f70f/jol-samples/src/main/java/org/openjdk/jol/samples/JOLSample_01_Basic.java

```
(import '(org.openjdk.jol.info ClassLayout))
(.toPrintable (ClassLayout/parseInstance []))
;;=>
clojure.lang.PersistentVector object internals:
 OFFSET  SIZE                                 TYPE DESCRIPTION                               VALUE
      0     4                                      (object header)                           21 00 00 00 (00100001 00000000 00000000 00000000) (33)
      4     4                                      (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                                      (object header)                           c7 df 00 f8 (11000111 11011111 00000000 11111000) (-134160441)
     12     4                                  int APersistentVector._hash                   -1
     16     4                                  int APersistentVector._hasheq                 -2017569654
     20     4                                  int PersistentVector.cnt                      0
     24     4                                  int PersistentVector.shift                    5
     28     4   clojure.lang.PersistentVector.Node PersistentVector.root                     (object)
     32     4                   java.lang.Object[] PersistentVector.tail                     []
     36     4          clojure.lang.IPersistentMap PersistentVector._meta                    null
Instance size: 40 bytes
Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
```

**Note**: *you may want to clone source repository to get latest version*:

```
hg clone http://hg.openjdk.java.net/code-tools/jol/ jol
cd jol
mvn clean install
```

Then update version in project.clj.


#### Using GraphLayout

`GraphLayout` is one of more useful classes which can be used for getting total object footprint.
Check [ObjectFootprint.java source code](http://hg.openjdk.java.net/code-tools/jol/file/018c0e12f70f/jol-cli/src/main/java/org/openjdk/jol/operations/ObjectFootprint.java).

One problem with this class is that `parseInstance` method accepts varargs,
which means it's harder to use from Clojure.
You can use it like this:

```
(import '(org.openjdk.jol.info GraphLayout))

(.toFootprint (GraphLayout/parseInstance (doto (object-array 1) (aset 0 [1 2 3]))))
;;=>
clojure.lang.PersistentVector@4999f4e6d footprint:
     COUNT       AVG       SUM   DESCRIPTION
         2        88       176   [Ljava.lang.Object;
         1        40        40   clojure.lang.PersistentVector
         1        24        24   clojure.lang.PersistentVector$Node
         3        24        72   java.lang.Long
         1        16        16   java.util.concurrent.atomic.AtomicReference
         8                 328   (total)
```

Check https://stackoverflow.com/questions/11702184/how-to-handle-java-variable-length-arguments-in-clojure
for more details.

## API

### Java

List all public java methods (including inherited ones) in given java class:
```clojure
(require '[clojure-repl.java :as java])
(java/jmethods java.util.Date)
```
