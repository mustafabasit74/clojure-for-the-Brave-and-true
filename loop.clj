(loop [iteration 0 ]
  (println "Iteration " iteration)

  (if (> iteration 3)
    (println "Goodbye!")
  (recur (inc iteration) ) ) ) 

;factorial via loop

(loop [fact (rand-int 6) accumulator 1]
  (if (zero? fact)
    accumulator
    (recur (dec fact) (* accumulator fact) ) ) ) 
    
(defn filter-odd-nums
  [collection]
  (loop [filtered-collection []
         remaining-collection collection]
        (if (empty? remaining-collection)
              filtered-collection
              (if (even? (first remaining-collection) )
                  (recur (conj filtered-collection (first remaining-collection)) (rest remaining-collection) )
                  (recur filtered-collection (rest remaining-collection)) ) )))  

(filter-odd-nums [0 1 2 3 4 5 6 7 8 9 10])

(filter even? [0 1 2 3 4 5 6 7 8 9 10])