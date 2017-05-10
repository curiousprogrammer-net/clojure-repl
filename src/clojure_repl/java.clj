(ns clojure-repl.java
  "Useful functions for interacting with java classes/objects.
  You can use these functions to explore public API of java classes,
  especially `jmethods` and `jinfo` can be useful."
  (:require [clojure.string :refer [join trim]]))

(defn- deprecated?
  "Returns true if given method is deprecated, false otherwise.
  Only methods marked with `java.lang.Deprecated` annotation are recognized as deprecated."
  [method]
  (.isAnnotationPresent method java.lang.Deprecated))

(defn- method-description
  "Returns a text description of java.lang.reflect.Method."
  [method]
  (->> [(.getName method)
        (java.util.Arrays/toString (.getParameterTypes method))
        "->"
        (.getReturnType method)
        (when (deprecated? method) "(DEPRECATED)")]
       (join " ")
       trim))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Public API
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(defn jconstructors
  "Returns a sequence of all constructors of given java class."
  [clazz]
  (map str (:constructors (bean clazz))))

(defn jmethods
  "Returns a sequence of all _public_ methods of given java class
  whether directly declared or inherited from parent(s).
  Deprecated methods are not included by default.
  You can use 2-arity version to include deprecated method."
  ([clazz]
   (jmethods clazz false))
  ([clazz include-deprecated?]
   (->> (:methods (bean clazz))
        (filter #(or
                  include-deprecated?
                  (not (deprecated? %))))
        (sort-by #(.getName %))
        (map method-description))))

(defn jinfo
  "Shows all public constructors and methods of given java class"
  [clazz]
  (concat
   (jconstructors clazz)
   (jmethods clazz)))
