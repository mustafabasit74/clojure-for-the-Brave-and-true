;; In general, special forms are special because they implement core behavior 
;; that can’t be implemented with functions. For example:

 (if true (inc 1) (inc 5))
 ;; => 2

(if true 1 2)
;; => 1

(def if 20)
(if true if :ispegged?)
;; => 20

;; ***
;; Here, we ask Clojure to evaluate a list beginning with the symbol if.
;; That if symbol gets resolved to the if special form, and Clojure calls that
;; special form with the operands true, 1, and 2.
;; Special forms don’t follow the same evaluation rules as normal functions.
;; For example, when you call a function, each operand gets evaluated.
;; However, with if you don’t want each operand to be evaluated. You only
;; want certain operands to be evaluated, depending on whether the 
;; condition is true or false.



;; Another important special form is quote.
'(a b c )
;; => (a b c)

;; this invokes a reader macro so that we end up with this:
(read-string "'(a b c)")
;; => (quote (a b c))

(quote (a b c))
;; => (a b c)

;; ***
;; Normally, Clojure would try to resolve the a symbol and then call it
;; because it’s the first element in a list. The quote special form tells the evaluator,
;; “Instead of evaluating my next data structure like normal, just return
;; the data structure itself.” In this case, you end up with a list consisting
;; of the symbols a, b, and c.

(quote (map inc [1 2 3 4 5]))
;; => (map inc [1 2 3 4 5])

;; def, let, loop, fn, do, and recur are all special forms as well. You can see 
;; why: they don’t get evaluated the same way as functions

;; ***
;; So the evaluator receives a combination of data structures from the reader, and it goes
;; about resolving the symbols and calling the functions or special forms at
;; the beginning of each list. But there’s more! You can also place a macro
;; at the beginning of a list instead of a function or a special form, and this
;; can give you tremendous power over how the rest of the data structures are evaluated.

;; treat list as as string
(eval "(+ 1 2 3)")
;; => "(+ 1 2 3)"
"(+ 1 2 3)"
;; => "(+ 1 2 3)"

(eval "(hello) world)))))")
;; => "(hello) world)))))"


;; treat list as a data
(eval '(+ 1 2 3))
;; => 6
  
(eval '"hello world")
;; => "hello world"
