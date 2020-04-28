;; Chapter 8 
;; Macro writing is all about building a list for Clojure to evaluate, and it
;; requires a kind of inversion to your normal way of thinking.

;; One key difference between functions and macros is that function arguments are fully evaluated 
;; before they’re passed to the function whereas macros receive arguments as unevaluated data.

(defmacro infix
  [infixed]
  (list (second infixed) (first infixed) (last infixed)))

(infix (1 + 1))
;; => 2

(defmacro postfix
  [[operand1 operand2  operator]]
  (list operator operand1 operand2))

(postfix (2 4 +))
;; => 6

(defmacro eval-fn
  [fn & data]
  (conj data fn))

(eval-fn + 1 2 3)
;; => 6

(eval-fn str "hello" " world")
;; => "hello world"

(eval-fn max 2 90 10 19 39 )
;; => 90

;; you’ll need to be extra careful about the difference between a symbol and its value.

(defmacro print-my-expression
  "print and retruns expression"
  [expression]
  (list let [result expression]
        (println result)
        result))
;; => Syntax error compiling at (macros-in-action.clj:40:3).
;;    Can't take value of a macro: #'clojure.core/let

(defmacro print-my-expression
  "print and retruns expression"
  [expression]
  (list 'let ['result expression]
        (list 'println 'result)
        'result))
;; => #'user/print-my-expression

(print-my-expression (+ 1 2 3))
;; => 6
;;    6

(print-my-expression (str "Hello" "World"))
;; => HelloWorld
;;    HelloWorld

;; The single quote character is a reader macro for (quote x):
;; single quote character is a macro

'(+ 1 2)
;; => (+ 1 2)

(when (= 1 1)
  (println "Hello")
  (println "World")
  (str "Bye!"))
;; => Hello
;;    World
;;    "Bye!"

;; when is a macro
;; This is when’s actual source code

(defmacro when
  "Evaluates test. If logical true, evaluates body in an implicit do."
  {:added "1.0"}
  [test & body]
  (list 'if test (cons 'do body)))
;; Notice that the macro definition quotes both if and do. That’s because
;; you want these symbols to be in the final list that when returns for evaluation.

(defmacro my-when
  [test & body]
  (list 'if  test (conj body 'do )))


(my-when (= 1 1)
         (println "Hello")
         (println "World")
         (str "Bye!"))
;; => Hello
;;    World
;;    "Bye!"

(defmacro if-or
  [tests-vec exp]
  (list 'if (cons 'or tests-vec) exp))

(if-or [(= 1 1) (= 2 2)]
       (println "Success"))
;; => Success
;;    nil

(macroexpand '(if-or [(= 1 1) (= 2 2)]
                     (println "Success")))
;; => (if (or (= 1 1) (= 2 2)) (println "Success"))


(if-or [false (= 2 2)]
       (println "Success"))
;; => Success
;;    nil

(if-or [false false]
       (println "Success"))
;; => nil


(defmacro unless
  "Inverted-if"
  [test & branches]
  (conj (reverse branches) test 'if ))


(unless true
        "true"
        "false"  )

;; Quoting v/s Syntax Quoting
;; '       v/s `

;; Syntax quoting returns unevaluated data structures, similar to normal
;; quoting. However, there are two important differences. One difference is
;; that syntax quoting will return the fully qualified symbols (that is, with the 
;; symbol’s namespace included) .

;; Syntax quoting will always include the symbol’s full namespace

'+
;; => +

`+
;; => clojure.core/+

;; The reason syntax quotes include the namespace is to help you avoid name collisions

;;The other difference between quoting and syntax quoting is that the latter allows you
;; to unquote forms using the tilde, ~

'(+ 1 ~(inc 2))
;; => (+ 1 (clojure.core/unquote (inc 2)))

`(+ 1 ~(inc 2))
;; => (clojure.core/+ 1 3)

;; unquote is not like eval
`(+ 1 (eval (inc 2)))
;; => (clojure.core/+ 1 (clojure.core/eval (clojure.core/inc 2)))

(macroexpand `(+ 1 ~(inc 2)))
;; => (clojure.core/+ 1 3)

;; syntax quoting and unquoting allow you to create lists more
;; clearly and concisely. Compare using the list function

(list '+ 1 (inc 2))
;; => (+ 1 3)

`( +  1 ~(inc 2))
;; => (clojure.core/+ 1 3)


;; *** 
;; list of list ??

(list 1 2 3 4)
;; => (1 2 3 4)

;; ***
(list (1 2) (3 4) (5 6))
;; => Execution error (ClassCastException) at user/eval7893 (form-init5927026118290817335.clj:184).
;;    class java.lang.Long cannot be cast to class clojure.lang.IFn (java.lang.Long is in module java.base of loader 'bootstrap'; clojure.lang.IFn is in unnamed module of loader 'app')

(list '(1 2) '(3 4) '(5 6))
;; => ((1 2) (3 4) (5 6))


;; code as a data and data as code ??? - clear some confusions 

(quote (1 + 2))
;; => (1 + 2)

'(1 + 2)
;; => (1 + 2)

;; ***
(def msg "Bye!!")
;; => #'user/msg

(macroexpand (println "Hello world" msg))
;; => Hello world Bye!!
;;    nil
(macroexpand (list println "Hello world" msg))
;; => (#function[clojure.core/println] "Hello world" "Bye!!")

(macroexpand '(list println "Hello world" msg))
;; => (list println "Hello world" msg)

(macroexpand (list 'println "Hello world" msg))
;; => (println "Hello world" "Bye!!")

(macroexpand (list 'println "Hello world" 'msg))
;; => (println "Hello world" msg)

(macroexpand `(list println "Hello world" ~msg))
;; => (clojure.core/list clojure.core/println "Hello world" "Bye!!")

(macroexpand `(println "Hello world" msg))
;; => (clojure.core/println "Hello world" user/msg)

(macroexpand `(println "Hello world" ~msg))
;; => (clojure.core/println "Hello world" "Bye!!")


(defmacro code-critic
  [bad good]
  (list 'do
        (list 'println 
              "Great squid of Madrid, this is bad code:" 
              (list 'quote bad))
        (list 'println 
              "Sweet Gorilla of Manila, this is good code:"
              (list 'quote good))))

(code-critic (1 + 2) (+ 1 2))
;; => Great squid of Madrid, this is bad code: (1 + 2)
;;    Sweet Gorilla of Manila, this is good code: (+ 1 2)
;;    nil

(defmacro code-critic
  [bad good]
  `(do
     (println "Great squid of Madrid, this is bad code:" (quote ~bad))
     (println "Sweet Gorilla of Manila, this is good code:" (quote ~good))))

(code-critic (1 + 2) (+ 1 2))
;; => Great squid of Madrid, this is bad code: (1 + 2)
;;    Sweet Gorilla of Manila, this is good code: (+ 1 2)
;;    nil


;; To sum up, macros receive unevaluated, arbitrary data structures
;; as arguments and return data structures that Clojure evaluates.
