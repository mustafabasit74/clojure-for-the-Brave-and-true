;; (not= x) (not= x y) (not= x y & more)
;; Same as (not (= obj1 obj2))


(not= 3) 
(not= nil)
(not= true)
(not= false)
;; => false

(not= 1 2)
;; => true

(not= 2 2)
;; => false

(not= false nil)
;; => true

(not= true true)
;; => false

(not= [1 2] ["a" "b"])
(not= nil true false)
