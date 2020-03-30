;; pure function 
;; It's return value is the same for the same arguments
;; pure functions cannot access anything other than what they are passed
;; An expression is said to be pure if its evaluation lacks side-effects
 
;; This function only relies on variable explicitly passed to it, and does not mutate any 
;; external state or the argument passed to it

;; Easy to debug  

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

