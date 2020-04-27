;; macros allow you to transform an arbitrary data structure like (1 + 2) into one that can Clojure can 
;; evaluate, (+ 1 2). That means you can use Clojure to extend itself so you can write programs however you please.
;;  In other words, macros enable "syntactic abstraction"

;; Syntactic Abstraction and the -> Macro

;;Often, Clojure code consists of a bunch of nested function calls


(defn clean
  [data]
   (clojure.string/upper-case  (clojure.string/trim data)))

(clean "    often, clojure code consists of a bunch of nested functions calls      ")
;; => "OFTEN, CLOJURE CODE CONSISTS OF A BUNCH OF NESTED FUNCTIONS CALLS"

(defn clean
  [data]
  (-> data
      clojure.string/trim
      clojure.string/upper-case))
(clean "    often, clojure code consists of a bunch of nested functions calls      ")
;; => "OFTEN, CLOJURE CODE CONSISTS OF A BUNCH OF NESTED FUNCTIONS CALLS"

;; if you want to translate Clojure code so you can read it in a more familiar,
;; left-to-right, top-tobottom manner, you can use the built-in -> macro, which 
;; is also known as the "threading" or "stabby" macro
(def path (str (System/getProperty "user.dir") "/core.clj" ))

(defn read-resource 
  "read a resource into a string"
  [path]
  (read-string (slurp (clojure.java.io/resource path))))

(read-resource path)
;; => Execution error (IllegalArgumentException) at user/read-resource (form-init9096743642097882595.clj:4).
;;    Cannot open <nil> as a Reader.
;; fix - pending

(defn read-resource
  [path]
  (-> path
      clojure.java.io/resource
      slurp
      read-string))

;; You can read this as a pipeline that goes from top to bottom instead of
;; from inner parentheses to outer parentheses

;; ***
;; The -> also lets us omit parentheses, which means there’s less visual noise to contend with.
;; This is a syntactic abstraction because it lets you write code in a syntax that’s
;; different from Clojure’s built-in syntax but is preferable for human consumption

;; Clojure is homoiconic: its text represents data structures, and those data structures
;; represent abstract syntax trees, allowing you to more easily reason about
;; how to construct syntax-expanding macros.