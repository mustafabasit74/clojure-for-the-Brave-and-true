;; The reader converts the textual source code you save in a file or enter in the
;; REPL into Clojure data structures. It’s like a translator between the human
;; world of Unicode characters and Clojure’s world of lists, vectors, maps, symbols, and other data structures. 

;; The text you type in the REPL e.g (str "Hello" "World") is really just a sequence of Unicode characters, 
;; but it’s meant to represent a combination of Clojure data structures.
;; This textual representation of data structures is called a "reader form".

;; Reading and evaluation are discrete processes that you can perform independently.

;; One way to interact with the reader directly is by using the
;; read-string function. read-string takes a string as an argument and processes
;; it using Clojure’s reader, returning a data structure:

(read-string "(+ 1 2)")
;; => (+ 1 2)

(list? '(+ 1 3))
;; => true

(list? "(+ 1 3)")
;; => false

(list? (read-string "(+ 1 2)"))
;; => true

(conj (read-string "(+ 1 2)") :connections)
;; => (:connections + 1 2)

(eval (conj (read-string "(1 2 3)") +))
;; => 6

;; The takeaway is that reading and evaluating are independent of each other. You can read text 
;; without evaluating it, and you can pass the result to other functions.

(read-string "#()")
;; => (fn* [] ())

(read-string "#{1 2 3 3 3 3}")
;; => Execution error (IllegalArgumentException) at user/eval7999 (form-init9096743642097882595.clj:39).
;;    Duplicate key: 3
(read-string "str")

(eval (+ 1 2))

(read-string "(eval ( + 1 2))")
;; => (eval (+ 1 2))

(list?  (read-string "(eval ( + 1 2))"))
;; => true

(eval (+ 1 (eval (+ 1 2))))
;; => 4

;; the reader can employ more complex behavior when converting text to data structures.

(#(+ 1 %) 3)
;; => 4

(read-string "(#(+ 1 %) 3)")
;; => ((fn* [p1__8053#] (+ 1 p1__8053#)) 3)

(read-string "(#(+ 1 %1 %2) 3 10)")
;; => ((fn* [p1__8062# p2__8063#] (+ 1 p1__8062# p2__8063#)) 3 10)

(read-string "#(+ %1 %2 %3)")
;; => (fn* [p1__8073# p2__8074# p3__8075#] (+ p1__8073# p2__8074# p3__8075#))

;; Reader macros are sets of rules for transforming text into data structures.
;; ***
;; They often allow you to represent data structures in more compact ways because 
;; they take an abbreviated reader form and expand it into a full form.
(read-string "'(a b c)")
;; => (quote (a b c))

(def msg "hello world")
;; => #'user/msg
(ns-interns *ns*)
;; => {msg #'user/msg, text2 #'user/text2, addition-list #'user/addition-list, lucky-number #'user/lucky-number}
(deref #'user/msg)
;; => "hello world"

(def secret #'user/msg)
;; => #'user/secret
(ns-interns *ns*)
;; => {msg #'user/msg, 
;;    secret #'user/secret, text2 #'user/text2, addition-list #'user/addition-list, lucky-number #'user/lucky-number}

secret
;; => #'user/msg
(print secret)
;; => nil

(deref secret)
;; => "hello world"


(read-string "@var")
;; => (clojure.core/deref var)

(read-string "@secret")
;; => (clojure.core/deref secret)

(eval (read-string "@secret"))
;; => "hello world"

;; They’re designated by "macro characters", like '(the single quote), # and @.
;; They’re also completely different from the macros

;; Reader macros can also do crazy stuff like cause text to be ignored. The
;; semicolon designates the single-line comment reader macro:
(read-string "; Ignore this ....\n(+ 1 2)")
;; => (+ 1 2)



