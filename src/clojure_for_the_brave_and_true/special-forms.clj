;; In general, special forms are special because they implement core behavior 
;; that can’t be implemented with functions. For example:

 (if true (inc 1) (inc 5))
 ;; => 2

(if true 1 2)
;; => 1

;; ***
;; Here, we ask Clojure to evaluate a list beginning with the symbol if.
;; That if symbol gets resolved to the if special form, and Clojure calls that
;; special form with the operands true, 1, and 2.
;; Special forms don’t follow the same evaluation rules as normal functions.
;; For example, when you call a function, each operand gets evaluated.
;; However, with if you don’t want each operand to be evaluated. You only
;; want certain operands to be evaluated, depending on whether the 
;; condition is true or false.