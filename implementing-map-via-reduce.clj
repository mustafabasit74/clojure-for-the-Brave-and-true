(map inc [1 2 3 4 5 ])
(reduce #(conj %1 (inc %2)) [] [1 2 3 4 5]) 


(map str ["a" "b" "c"] ["A" "B" "C"])
;; (reduce #() [] .....) - pending

(map + [1 2 3] [1 2 3])
;; (reduce #() [] .......) -pending 

(def geeks [{:name "Basit" :interest "Clojure":age 24}
            {:name "Zakir" :interest "Python" :age 29}
            {:name "Nuzhat" :interest "Python" :age 23}])

(map (fn[geek]
      {:name (:name geek)
       :interest (:interest geek)
       :age (- (:age geek) 2 )} ) geeks)

(reduce #(conj %1 {:name (:name %2)
                   :interest (:interest %2)
                   :age (- (:age %2) 2) }) [] geeks)
                   
