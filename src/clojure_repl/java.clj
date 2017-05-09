(ns clojure-repl.java
  "Useful functions for interacting with java classes/objects.
  You can use these functions to explore public API of java classes,
  especially java-members, java-methods, java-constructors."
  (:require [clojure.string :refer [join lower-case]]))

(defn- subs-after-last-dot [s]
  (let [to-string (str s)]
    (if (= -1 (.indexOf to-string "."))
      to-string
      (last (re-find #".*\.([^.]+)" to-string)))))

(defn- member-type
  "Returns a string description of member type - e.g. 'Field', 'Method', 'Constructor'."
  [member]
  (subs-after-last-dot (type member)))

(defn- normalized-name [member]
  (subs-after-last-dot (:name member)))

(defn get-members
  ([object]
   (get-members object :public))
  ([object visibility]
   (get-members object visibility #{"method"}))
  ([object visibility member-types]
   (->> (clojure.reflect/reflect object)
        :members
        (filter (fn [member] (and ((:flags member) visibility)
                                  ((set (map lower-case member-types))
                                   (lower-case (member-type member))))))
        (sort-by :name))))

(defn get-members-as-strings
  "Returns the sequence of string description of public methods of given object.
  Private members can be retrieved by using 2-arity version and passing visibility flag :private.
  Fields and constructors can be retrieved by using 3-arity version and passing desired
  member type(s) as a set, e.g. `(get-members-as-strings object :public #{\"constructor\"}`"
  ([object]
   (get-members-as-strings object :public))
  ([object visibility]
   (get-members-as-strings object visibility #{"method"}))
  ([object visibility member-types]
   (->> (get-members object visibility member-types)
        ;; only interesting data
        (map (fn [member]
               (join
                " "
                [(normalized-name member)
                 (:parameter-types member)
                 (when-let [return-type (:return-type member)]
                   (str "-> " (:return-type member)))]))))))

(defn print-java-members [& args]
  (doseq [m (apply get-members-as-strings args)]
    (println m)))

(defn java-methods [object]
  (print-java-members object :public #{"method"}))

(defn java-constructors
  "Prints all public constructors of given java object/class."
  [object]
  (print-java-members object :public #{"constructor"}))

(defn java-fields
  "Prints all public fields of given java object/class."
  [object]
  (print-java-members object :public #{"field"}))

(defn java-members
  "Prints all public members of given java object/class."
  [object]
  (println "\nFields:")
  (java-fields object)
  (println "\nCtors:")
  (java-constructors object)
  (println "\nMethods:")
  (java-methods object))

