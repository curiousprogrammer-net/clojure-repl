# Change Log

All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

* update ancestors function to automatically class class if an instance 
  of java.lang.class isn't already provided by a client.

## [0.2.2] - 2018-03-07

* add clj-memory-meter as a dependency to be able to use it transitively

## [0.2.1] - 2018-03-02

* Add the new `ancestors` function inspired by the talk "Learning Clojure: Next Steps" - Stuart Sierra

## [0.2.0] TBD description

## [0.1.0] - 2017-06-09

### Added
- Handy functions in java namespace for listing public java methods on given class
- criterium dependency for easy profiling
- alembic dependency for hotloading dependencies into running repl
- JOL dependency for examining layout of java objects and their memory footprint
