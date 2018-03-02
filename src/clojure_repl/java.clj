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

(defn inheritance-tree
  ([clazz]
   (inheritance-tree clazz #(.getName %)))
  ([clazz sort-fn]
   (let [f (fn f [c]
             (reduce (fn [m p] (assoc m p (f p)))
                     {}
                     (sort-by sort-fn (parents c))))]
     {clazz (f clazz)})))

(defn print-tree [tree]
  (let [p (fn [c indent]
            (print (apply str (repeat (* 4 indent) \space)))
            (println "*" (if (.isInterface c)
                           (.getName c)
                           (str \< (.getName c) \>))))
        f (fn f [t indent]
            (if (map? t)
              (doseq [[k v] t]
                (p k indent)
                (f v (inc indent)))
              (p t indent)))]
    (f tree 0)))

(defn ancestors
  "Prints all ancestors of given class using a nice inheritance tree visualization.
  Classes are enclosed with angle brackets."
  [clazz]
  (print-tree (inheritance-tree clazz)))

(comment

  (inheritance-tree clojure.lang.PersistentArrayMap)

  (ancestors clojure.lang.PersistentArrayMap))
