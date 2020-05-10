(take 3 [1 2 3 4 5])
(take 3 '(1 2 3 4 5))

(drop 3 [1 2 3 4 5])
(drop 3 '(1 2 3 4 5))

(take-while #(< % 4) [1 2 3 4 5])
(drop-while #(< % 4) [1 2 3 4 5])

(take-while neg? [-1 -3 -93 -10 0 -3 -2 -40 10 22 49])
;; => (-1 -3 -93 -10)

(drop-while neg? [-1 -3 -93 -10 0 -3 -2 -40 10 22 49])
;; => (0 -3 -2 -40 10 22 49)

(def food-journal
  [{:month 1 :day 1 :human 2 :critter 0}
   {:month 1 :day 2 :humsan 5 :critter 1}
   {:month 2 :day 1 :human 1 :critter 4}
   {:month 2 :day 2 :human 3 :critter 2}
   {:month 3 :day 1 :human 2 :critter 8}
   {:month 3 :day 2 :human 1 :critter 2}
   {:month 4 :day 1 :human 2 :critter 4}
   {:month 4 :day 2 :human 0 :critter 5}
   {:month 5 :day 1 :human 0 :critter 4}
   {:month 5 :day 2 :human 2 :critter 4}]) 


(take-while #(< (:month %) 3 ) food-journal)  
(filter #(< (:month %) 3) food-journal)


(drop-while #(< (:month %) 3)  food-journal)   
(filter #(> (:month %) 2) food-journal)

;; filter is lit bit slower than take-while and drop-while

;; get data from 3rd and 4th month
(take-while #(< (:month %) 5 )
            (drop-while #(< (:month %) 3) food-journal) )
;; or

(drop-while #(< (:month %) 3)
             (take-while #(< (:month %) 5) food-journal))
            