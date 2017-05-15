(defproject curiousprogrammer/clojure-repl "0.1.0-SNAPSHOT"
  :description "Sample project with a few useful dependencies used just to run `lein repl` and do clojure experiments."
  :url "https://github.com/jumarko/clojure-repl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [alembic "0.3.2"]
                 [criterium "0.4.4"]]
  :main ^:skip-aot clojure-repl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :repl {:dependencies [[org.clojure/clojure "1.9.0-alpha16" :classifier "sources"]]}})
