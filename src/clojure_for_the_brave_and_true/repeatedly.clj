(repeatedly #(rand-int 100 ))

(repeatedly  5 #(rand-int 100 ))

(take 5 (repeatedly #(rand-int 100)))

(take 5 (repeatedly (partial rand-int 100)))
;; => (47 94 19 20 30)

;; repeat will call function only once;; 
(repeat 5 (rand-int 100))
;; => (72 72 72 72 72)

(def items [:speaker :laptop :rubic-cube :mouse :keyboard :pen :clock :printer 
            :power-bank :pot :marker :table :phone :usb-cable :pendrive :sd-card])

(def warehouse-capacity 8)
(take warehouse-capacity (repeatedly #(rand-int (count items))))
;; => (11 8 5 7 0 0 6 8)

(map #(nth items %)
 (take warehouse-capacity (repeatedly #(rand-int (count items)))))
;; => (:mouse :rubic-cube :pot :pen :table :table :power-bank :printer)

(->> (repeatedly #(rand-int (count items)))
     (take warehouse-capacity)
     (map #(nth items %)))
;; => (:keyboard :clock :phone :clock :marker :printer :printer :marker)


