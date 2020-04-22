;; pure function 
;; It's return value is the same for the same arguments
;; pure functions cannot access anything other than what they are passed
;; An expression is said to be pure if its evaluation lacks side-effects
 
;; This function only relies on variable explicitly passed to it, and does not mutate any 
;; external state or the argument passed to it

;; Easy to debug  

;; When a function performs any other “action”, apart from calculating its return value, the function is impure.
;; It follows that a function which calls an impure function is impure as well. Impurity is contagious.

(defn calculate-area
  [radius]
  (* 3.14 (* radius radius)) )

(calculate-area 215)
(calculate-area 215)
(calculate-area 215)


;; impure function 
(defn foo 
  [num]
  (+ num (rand-int 50))) 

(foo 3) 
(foo 3) 
(foo 3) 


(rand-int 10)
(rand-int 10)
(rand-int 10)


;; pure fucntion 
;; don't modify argument being passed to them
;; It's evaluation has no side effects
;; never alter state/ does not modify external variable


;; pure function 
;; no use of global variables 

(defn pure-foo 
  [x y]
  (def mult 2)
  (* (+ x y) mult))

(pure-foo 4 5)

;; impure function 
(def mult 2)

(defn impure-foo
  [x y]
  (* (+ x y) mult))

(impure-foo 4 5)

;; impure fucntion 
(def x 100)

(defn impure-foo
  []
  x)

(impure-foo )

;; pure function 
;; takes in a specific argument and returns a determinstic value

(defn increment-numbers
  [number-collection]
  (map inc number-collection) )

(increment-numbers '(1 2 3 4 5))


;; impure function 
;; uses global value rather than the argument so the result is inderminstic

(def global-value '(44 93 11 89 10) )

(defn impure-increment-numbers
  [number-collection]
  (map inc global-value) )
  
(impure-increment-numbers '(1 2 3 4 5))


;; https://www.sitepoint.com/functional-programming-pure-functions/

;; Pure functions can always be parallelized. Distribute the input values over a number of threads, and collect the results.

;; Memoization
;; Because pure functions are referentially transparent, we only need to compute their output once for given inputs.
;; Caching and reusing the result of a computation is called memoization, and can only be done safely with pure functions.

;; Laziness
;; A variation on the same theme. We only ever need to compute the result of a pure function once,
;; but what if we can avoid the computation entirely? Invoking a pure function means you specify a dependency:
;; this output value depends on these input values. But what if you never use the output value? Because the function 
;; can not cause side effects, it does not matter if it is called or not. Hence a smart system can be lazy and optimize 
;; the call away.
;; Some languages, like Haskell, are completely built on lazy evaluation. Only values that are needed to
;; achieve side effects are computed, the rest is ignored. Ruby’s evaluation strategy is called strict evaluation,
;; each expression is completely evaluated before its result can be used in another expression
