(in-ns 'edu.iust.clojure)
;; => #namespace[edu.iust.clojure]

(def awesome-languages ["Clojure" "Scala" "Java" "Python"])
;; => #'edu.iust.clojure/awesome-languages

(def to-do-list ["learn Scala" "learn LISP" "download books" "send mail"])
;; => #'edu.iust.clojure/to-do-list

(in-ns 'jet-brains)
;; => #namespace[jet-brains]

to-do-list
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:8151).
;; => Unable to resolve symbol: to-do-list in this context
;; => class clojure.lang.Compiler$CompilerException

;; how to access objects that belong to another namespace?

edu.iust.clojure/awesome-languages
;; => ["Clojure" "Scala" "Java" "Python"]

;; using refer 
;; refer gives you fine-grained control over how you refer to objects in other namespaces.

(clojure.core/refer 'edu.iust.clojure)
;; => nil
;; here we are calling a function "refer" which is defined in "clojure.core" namespace,

;; Calling refer with a namespace symbol lets you refer to the corresponding namespace’s objects
;; without having to use fully qualified symbols. It does this by updating the
;;  current namespace’s symbol/object map.  (but snapshot data, any new change that namespace will will not reflect here)

awesome-languages
;; => ["Clojure" "Scala" "Java" "Python"]

to-do-list
;; => ["learn Scala" "learn LISP" "download books" "send mail"]

;; ***
(clojure.core/ns-interns clojure.core/*ns*)
;; => {}

(in-ns 'jet-brains)

;; ***
(clojure.core/get (clojure.core/ns-map *ns*) 'awesome-languages)
#'edu.iust.clojure/awesome-languages

(clojure.core/refer 'clojure.core) 
;; => nil

(get (ns-map *ns*) 'awesome-languages)
;; => #'edu.iust.clojure/awesome-languages

(deref (get (ns-map *ns*) 'awesome-languages))
;; => ["Clojure" "Scala" "Java" "Python"]

(in-ns 'user)
;; => #namespace[user]

(ns-map *ns*)
;; => {primitives-classnames #'clojure.core/primitives-classnames,
;;     +' #'clojure.core/+',
;;     Enum java.lang.Enum,
;;     commute #'clojure.core/commute,
;;     .
;;     .
;;     .
;;     coll? #'clojure.core/coll?,
;;     bytes #'clojure.core/bytes,
;;     refer-clojure #'clojure.core/refer-clojure}

(count (ns-map *ns*))
;; => 757


(count (assoc (ns-map *ns*) 'key1 "value1"))
;; => 758

(type (ns-map *ns*))
;; => clojure.lang.PersistentHashMap

;; ***
;; refer-clojure

(in-ns 'foo)
;; => #namespace[foo]

(assoc {} :key1 "1")
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:1).
;; => Unable to resolve symbol: assoc in this context
;; => class clojure.lang.Compiler$CompilerException

(clojure.core/refer-clojure)
;; => nil
;; it is as if we are doing (clojure.core/refer 'clojure.core)

(assoc {} :key1 "1")
;; => {:key1 "1"}
