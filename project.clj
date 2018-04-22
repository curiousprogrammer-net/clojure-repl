(defproject curiousprogrammer/clojure-repl "0.2.3"
  :description "Sample project with a few useful dependencies used just to run `lein repl` and do clojure experiments."
  :url "https://github.com/jumarko/clojure-repl"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 ;; newer dependency with updated dynapath dependency (fix for java 9: https://github.com/tobias/dynapath/issues/7)
                 [clojure-emacs/alembic "0.3.3"]
                 [criterium "0.4.4"]
                 [org.openjdk.jol/jol-core "0.8"]
                 [com.clojure-goes-fast/clj-memory-meter "0.1.0"]
                 ]
  :main ^:skip-aot clojure-repl.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :repl {:dependencies [[org.clojure/clojure "1.9.0" :classifier "sources"]]}})
