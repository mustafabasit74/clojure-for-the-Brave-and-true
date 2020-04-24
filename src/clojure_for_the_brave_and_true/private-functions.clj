(defn foo
  [msg]
  msg)
;; Sometimes you may want a function to be available only to other functions
;; within the same namespace. Clojure allows you to define private functions
;; using defn-:
(defn- private-function
  [msg]
  msg)

(foo "Hi!")
;; => "Hi!"

(private-function "Hello!")
;; => "Hello!"

(ns-name *ns*)
;; => user

(in-ns 'iust.java)
(clojure.core/refer 'user)
(foo "Hi!")
;; => "Hi!"

;; If you try to call this function from another namespace or refer it
;; Clojure will throw an exception. You can see this when you evaluate the code

(private-function "Hello!")
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:1).
;; => Unable to resolve symbol: private-function in this context
;; => class clojure.lang.Compiler$CompilerException

;; However, you can get around the private flag; all you need to do is refer directly to the function's var:
(#'user/private-function "Not-So-Private")
;; => "Not-So-Private"

;;make it bit easy
(def secret #'user/private-function)
;; => #'iust.java/secret

(secret "no private anymore")
;; => "no private anymore"

(clojure.core/ns-interns clojure.core/*ns*)
;; => {secret #'iust.java/secret}



;; ***
(clojure.core/ns-interns clojure.core/*ns*)
;; => {}

(in-ns 'iust.scala)
(clojure.core/refer 'iust.clojure  :only ['private-function])
;; => Execution error (IllegalAccessError) at iust.scala/eval8381 (form-init10309273857393640813.clj:1) .
;; => private-function does not exist
;; => class java.lang.IllegalAccessError

;; If you want to be tricky, you can still access the private var using the 
;; arcane syntax @#'some/private-var, but you’ll rarely want to do that.
