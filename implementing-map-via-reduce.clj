(map inc [1 2 3 4 5 ])
(reduce #(conj %1 (inc %2)) [] [1 2 3 4 5]) 


(map str ["a" "b" "c"] ["A" "B" "C"])
;; (reduce #() [] .....) - pending

(map + [1 2 3] [1 2 3])
;; (reduce #() [] .......) -pending 

(def geeks [{:name "Basit" :intrest "Clojure":age 24}
            {:name "Zakir" :intrest "Python" :age 29}
            {:name "Nuzhat" :intrest "Python" :age 23}])

(map (fn[geek]
      {:name (:name geek)
       :intrest (:intrest geek)
       :age (- (:age geek) 2 )} ) geeks)

(reduce #(conj %1 {:name (:name %2)
                   :intrest (:intrest %2)
                   :age (- (:age %2) 2) }) [] geeks)
                   
