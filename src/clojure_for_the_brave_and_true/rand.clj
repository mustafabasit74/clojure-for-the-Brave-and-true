;; Note : rand returns float value

(rand )
;; returns random number between 0(inclusive) and n(exclusive), e.g. 0.13858237837624487

(rand 10)

(int (rand 10))

(rand 0 )
;; always returns 0


(rand-int  10) 
;; above statement is equivalent to  (int (rand 10))

;; (rand-int )
;; will throw ArityException



