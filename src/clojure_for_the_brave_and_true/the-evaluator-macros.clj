;; clojure evaluator doesn’t actually care where its input comes from; it doesn’t have to 
;; come from the reader. As a result, you can send your program’s data structures directly
;;  to the Clojure evaluator with eval

(def addition-list (list + 1 2))
(eval addition-list)
;; => 3

;; The data structures of your running program and those of the evaluator live in the same space,
;; and the upshot is that you can use the full power of Clojure and all the code you’ve written to
;; construct data structures for evaluation

text
;; => (#function[clojure.core/+] 1 2)

(eval (concat addition-list [3]))
;; => 6

(eval (list 'def 'msg "hello world"))
;; => #'user/msg

msg
;; => "hello world"

(eval (list 'def 'lucky-number (concat addition-list [3])))
;; => #'user/lucky-number

lucky-number
;; => 6

(eval '(+ 1 (eval '(+ 2 3))))
;; => 6

;; You can think of Clojure’s evaluator as a function that takes a data structure
;; as an argument, processes the data structure using rules corresponding to
;; the data structure’s type, and returns a result.

;; To evaluate a symbol, Clojure looks up what the symbol refers to.
;; To evaluate a list, Clojure looks at the first element of the list and calls a function, macro, or special form


;; REPL first reads your text to get a data structure, then sends that data structure 
;; to the evaluator and then prints the result as text

;; Whenever Clojure evaluates data structures that aren’t a list or symbol, the
;; result is the data structure itself:
true
;; => true

false
;; => false

{}
;; => {}

nil
;; => nil

:connections
;; => :connections

()
;; => ()

;; One of your fundamental tasks as a programmer is creating abstractions by
;; associating names with values.

;; Clojure uses symbols to name functions, macros, data, and anything else you can use, and evaluates them by
;; "resolving" them. To resolve a symbol, Clojure traverses any bindings you’ve created and then looks up the 
;; symbol’s entry in a namespace mapping. Ultimately, a symbol resolves to either a value or 
;; a special form — a built-in Clojure operator that provides fundamental behavior

;; In general, Clojure resolves a symbol by:
;; 1. Looking up whether the symbol names a special form. If it doesn’t . . .
;; 2. Looking up whether the symbol corresponds to a local binding. If it doesn’t . . .
;; 3. Trying to find a  namespace mapping introduced by def. If it doesn’t . . .
;; 4. Throwing an exception

;; 1. symbol resolving to a special form

(if true :a :b)
;; => :a

;; In this case, if is a special form and it’s being used as an operator.
;; If you try to refer to a special form outside of this context, you’ll get an exception:

if
;; => Syntax error compiling at (the-evaluator-macros.clj:1:8184).
;;    Unable to resolve symbol: if in this context

(def if "foo")
;; => #'user/if

if
;; => "foo"

(if true :a :b)
;; => :a
;; symbol resolving to a special form. Special forms, like if, are always used in the context
;;  of an operation; they’re always the first element in a list:
(if true if :b)
;; => "foo"

;; 2. local binding

(let [x 5]
  (+ 2 3))
;; => 5

(def x 20)
(+ x 5)
;; => 25

;; In the next example, x is mapped to 15, but we introduce a local binding of x to 5 using let. So x is resolved to 5:
(def x 15)
(let [x 5]
  (+ x 3))
;; => 8

(let [x 5]
  (let [x 6]
    (+ x 3)))
;; => 9

;; Functions also create local bindings
(defn foo
  [msg]
  (str msg "!"))
(foo "Hello")
;; => "Hello!"
(foo 'Hello)
;; => "Hello!"

(map inc [1 2 3])
;; When Clojure evaluates this code, it first evaluates the map symbol, looking up the corresponding 
;; function and applying it to its arguments. The symbol map refers to the map function, but it shouldn’t be
;;  confused with the function itself. The map symbol is still a data structure, the same way that the
;; string "fried salad" is a data structure, but it’s not the same as the function itself:

(read-string "+")
;; => +

(type (read-string "+"))
;; => clojure.lang.Symbol

(type (read-string "hello world"))
;; => clojure.lang.Symbol

(list + 1 2)
;; => (#function[clojure.core/+] 1 2)

(list '+ 1 2)
;; => (+ 1 2)

(type (first (list '+ 1 2) ))
;; => clojure.lang.Symbol

(list (read-string "+") 1 2)
;; => (+ 1 2)

;; In these examples, you’re interacting with the plus symbol, +, as a data
;; structure. You’re not interacting with the addition function that it refers to.
;; If you evaluate it, Clojure looks up the function and applies it:

(eval (list (read-string "+") 1 2))
;; => 3

;; On their own, symbols and their referents don’t actually do anything;
;; Clojure performs work by evaluating lists.









(eval "(+ 1 2 3)")
;; => "(+ 1 2 3)"

(eval '(+ 1 2 3))
;; => 6
