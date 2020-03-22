(def food-journal
  [{:month 1 :day 1 :human 2 :critter 0}
   {:month 1 :day 2 :human 0 :critter 1}
   {:month 2 :day 1 :human 1 :critter 4}
   {:month 2 :day 2 :human 3 :critter 2}
   {:month 3 :day 1 :human 2 :critter 8}
   {:month 3 :day 2 :human 1 :critter 2}
   {:month 4 :day 1 :human 2 :critter 4}
   {:month 4 :day 2 :human 0 :critter 5}
   {:month 5 :day 1 :human 5 :critter 4}
   {:month 5 :day 2 :human 2 :critter 4}]) 


(filter #(= (:human %) 0) food-journal)

(filter #() food-journal)


;; Returns the first logical true value of (pred x) for any x in coll, else nil.
(some #(= (:human %) 0) food-journal)

;; ***
;; beautiful hack
(some #(and (= (:human %) 0) %) food-journal)

(some #(> (:human %) 10) food-journal) 
(some #(and (> (:human %) 10) %) food-journal) 


(some even? [1 2 3 4 5 6])
(some #(and (even? %) %) [1 2 3 4 5 6])

(some even? [1 3  5 ])
(some even? [])
(some nil? [])
(some nil? [0 2 3 4 5 nil ] )

(some #(= %  3) [0 1 2 3 4 5])
