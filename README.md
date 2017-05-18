# clojure-repl

Very simple project skeleton for running the REPL and doing experiments.

## Adding dependency 

Add `clojure-repl` as a dependency to your profiles.clj or project.clj.
Ideally, into the `:repl` profile to not interfere it with other tasks:
```
 :repl {:dependencies [[curiousprogrammer/clojure-repl "0.1.0-SNAPSHOT"]]}}
```

## Libraries

### [criterium](https://github.com/hugoduncan/criterium)

Can be used to do quick micro-benchmarks.
Check https://github.com/hugoduncan/criterium#usage.

### [alembic](https://github.com/pallet/alembic)

Can be used to add a new dependency to classpath dynamically without restarting the REPL.
It's used internaly by [nrepl-refactor](https://github.com/clojure-emacs/refactor-nrepl/blob/a425a8103413fe91f56907857c2043c32b3630a2/src/refactor_nrepl/artifacts.clj#L111).

Can also be used to invoke leiningen tasks programatically: https://github.com/pallet/alembic#invoking-leiningen-tasks
