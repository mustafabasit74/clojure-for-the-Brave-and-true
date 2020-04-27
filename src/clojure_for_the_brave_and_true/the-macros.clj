;; see - the-reader,the-evaluator

;; Clojure evaluates data structures — the same data structures that
;; we write and manipulate in our Clojure programs
;; Wouldn’t it be awesome if we could use Clojure to manipulate the data structures 
;; that Clojure evaluates? Yes, yes it would! And guess what? You can do this with macros

;; infix notation

;; convert first into data Structure
(read-string "(1 + 2)")
;; => (1 + 2)

;; use Clojure to transform it so it will actually execute
(eval (let [infix (read-string "(1 + 1)")]
        (list (second infix) (first infix) (last infix))))
;; => 2

;; ***
;; Macros give you a convenient way to manipulate lists before Clojure evaluates them.
;;  Macros are a lot like functions: they take arguments and return a value, just like a function would.

;; What makes them unique and powerful is the way they fit
;; in to the evaluation process. They are executed in between the reader and
;; the evaluator—so they can manipulate the data structures that the reader
;; spits out and transform with those data structures before passing them to the evaluator

(defmacro backwards
  [form]
  (reverse form))

(backwards (" backwards" " am" "I" str))
;; => "I am backwards"


(defmacro ignore-last-operand
  [function-call]
  (butlast function-call))

(ignore-last-operand (+ 1 2 10))
;; => 3

(ignore-last-operand (+ 10 20 "foo"))
;; => 30

;; the macro ignore-last-operand receives the list (+ 1 2 10) as its
;; argument, not the value 13. This is very different from a function call
;; because function calls always evaluate all of the arguments passed in

;;  the unevaluated list data structure is passed in.

;; ???
;; Another difference is that the data structure returned by a function is
;; not evaluated, but the data structure returned by a macro is.

;; The process of determining the return value of a macro is called macro expansion, and you
;; can use the function macroexpand to see what data structure a macro returns
;; before that data structure is evaluated.

(macroexpand '(ignore-last-operand (+ 1 2 "foo")))
;; => (+ 1 2)

(macroexpand 2)
;; => 2

(macroexpand (ignore-last-operand (+ 1 2 "foo")))
;; => 3

;; But why would you want to do this? The reason is that macros allow you to transform an arbitrary 
;; data structure like (1 + 2) into one that can Clojure can evaluate, (+ 1 2) .
;; That means you can use Clojure to extend itself so you can write programs however you please.
;;  In other words, macros enable "syntactic abstraction"

;; Syntactic Abstraction and the -> Macro