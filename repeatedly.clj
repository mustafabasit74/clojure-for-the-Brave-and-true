(repeatedly #(rand-int 100 ))

(repeatedly  5 #(rand-int 100 ))

(take 5 (repeatedly #(rand-int 100)))

;repeat will call function only once;
(repeat 5 (rand-int 100))
