(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1} 
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part 
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part) } )


(defn symmetrize-body-parts
  [asym-body-parts]
  (loop [reamining-body-parts asym-body-parts
         final-body-parts []]
         (if (empty? reamining-body-parts)
               final-body-parts
               (let [[part & remaining] reamining-body-parts]
                 (recur remaining 
                     (into final-body-parts 
                        (set [part (matching-part part)])))))))
                  
;(symmetrize-body-parts asym-hobbit-body-parts )

;----------------------------------------------------------------------------------

;The pattern of process each element in a sequence and build a result is 
;so common that there’s a built-in function for it called reduce

;Reduce abstracts the task “process a collection and build a result”

(defn better-symmetrize-body-parts
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
              (into final-body-parts 
                (set [part (matching-part  part)] ) )) 
          []
          asym-body-parts))

;(better-symmetrize-body-parts asym-hobbit-body-parts )


;------------------------------------------------------------
;spider-expander multiply eyes and legs

;pending, do it as soon as possible
;------------------------------------------------------
;Hobbit Violence
(defn hit 
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce  + (map :size sym-parts) )
        target (rand body-part-size-sum)]
;we can't use reduce below to process the collection and bulid result, because we don't have to
;process all the elements of collection. we have to left the collection processing in mid way when (> accumulated-size target)        
        (loop [[part & reamining] sym-parts
                accumulated-size (:size part)]               
              (if ( > accumulated-size target)
                part 
                (recur reamining (+ accumulated-size (:size (first reamining)))) ) ) ) )
                
;(hit asym-hobbit-body-parts)

      