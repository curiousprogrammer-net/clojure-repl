(ns clojure-repl.java
  "Useful functions for interacting with java classes/objects.
  You can use these functions to explore public API of java classes,
  especially `jmembers`, `jmethods`, `jconstructors`"
  (:require [clojure.string :refer [join lower-case]]))

(defn- subs-after-last-dot [s]
  (let [to-string (str s)]
    (if (= -1 (.indexOf to-string "."))
      to-string (last (re-find #".*\.([^.]+)" to-string))))) 
(defn- member-type
  "Returns a string description of member type - e.g. 'Field', 'Method', 'Constructor'."
  [member]
  (subs-after-last-dot (type member)))

(defn- normalized-name [member]
  (subs-after-last-dot (:name member)))


;;; PUBLIC STUFF

(defn member-as-string
  "Returns a string descriptions of given java object/class member.
  For more info see `get-members`."
  [member]
  (join
   " "
   [(normalized-name member)
    (:parameter-types member)
    (when-let [return-type (:return-type member)]
      (str "-> " (:return-type member)))]))

(defn get-members
  "Returns a sequence of all (by default) public members of given java object/classs.
  The structure of items in collection is determined by what the `clojure.reflect/reflect` returns.
  The items are sorted by name alphabetically.
  The 1-arity variant will return all public methods.
  The 2-arity variant will return all methods with given visibility.
  The 3-arity variant will return all members with type tag from given set of member-types and given visibility (e.g. :public or :private).

  Example: get all public constructors of java class
    `(get-members java.util.ArrayList :public #{\"constructor\"})`"
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

(defn print-members
  "Generic method for printing members of java object/class.
  This will will just delegate to the `get-members`
  and print each member's string represetnation on a new line."
  [& args]
  (let [ms (apply get-members args)
        ms-str (map member-as-string ms)]
    (doseq [m ms-str]
      (println m))))


;;; Following functions are the ones that should be used in most cases
;;; They are simple to use and print important information.

(defn jfields
  "Prints all public fields of given java object/class."
  [object]
  (print-members object :public #{"field"}))

(defn jconstructors
  "Prints all public constructors of given java object/class."
  [object]
  (print-members object :public #{"constructor"}))

(defn jmethods
  "Prints all public methods of given java object/class."
  [object]
  (print-members object :public #{"method"}))

(defn jmembers
  "Prints all public members of given java object/class."
  [object]
  (println "\nFields:")
  (jfields object)
  (println "\nCtors:")
  (jconstructors object)
  (println "\nMethods:")
  (jmethods object))

