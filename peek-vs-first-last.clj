;For a list or queue, same as first
;For a vector, same as last, but much more efficient
;If the collection is empty, returns nil.

(last '(1 2 3 4 5))
; => 5
(last  [1 2 3 4 5])
; => 5

(last {:a 1 :b 2 :c 3})
; => [:c 3]


(first '(1 2 3 4 5))
;;=> 1
(first  [1 2 3 4 5])
;;=> 1
(first {:a 1 :b 2 :c 3})
; => [:c 3]


(first '())
(first [])
(first {})
(first nil)

(last '())
(last [])
(last {})

(last nil)
;=>nil


(peek '(1 2 3 4 5))
;;=> 1

(peek  [1 2 3 4 5]) 
;=> 5

(peek '())
(peek [])
(peek nil)


;Efficency of peek vs last
(def large-vec (vec (range 1 1000000)))

(time (last large-vec))
; => "Elapsed time: 46.10215 msecs"

(time (peek large-vec))
; => "Elapsed time: 0.029386 msecs"
