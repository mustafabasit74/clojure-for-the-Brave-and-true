;; ***
;; Reduce abstracts the task “process a collection and build a result
;; use reduce, map, filter when you want to process all the elements of 
;; a collection. These functions handles low level machinery for you .


(def broken-bio ["Hello" "everyone!" "my" "name" "is" "Mustafa" "Basit"])
(reduce #(str %1 " " %2) broken-bio)

;; ***
;; Reduce can be used to transform a map’s values, producing a new map with 
;; the same keys but with updated values

(def geeks [{:name "Basit" :intrest "Clojure":age 24}
            {:name "Zakir" :intrest "Python" :age 29}
            {:name "Nuzhat" :intrest "Python" :age 23}])

;; reduce age of geeks by 2 years
(reduce #(conj %1 {:name (:name %2) 
                   :age (- (:age %2) 2)})  [] geeks)
;; map implementation
(map (fn[geek]
      {:name (:name geek) :age (- (:age geek) 2 )} ) geeks)

;; reduce treats the argument {:max 30 :min 10} as a sequence of vectors, like ([:max 30] [:min 10])
;; as we know it first call seq function first 
(reduce (fn [new-map [key val]]
          (assoc new-map key (inc val))) {} {:min 10 :max 100})


;; Another use for reduce is to filter out keys from a map based on their value. 
(reduce (fn[new-map [key val]]
          (if (> val 8)
            (assoc new-map key val)
            new-map)) {} {:srinagar 9.2 :baramulla 5.8 :pulwama 7.9 :jammu 9.7} )

;; Whenever you want to derive a new value from a seqable data structure, reduce will 
;; usually be able to do what you need

    























