(defn recursively-use-a-collection
  [collection]
  (println (first collection) )
  
  (if (empty? collection) 
    (print-str "No more values to process")
    (recursively-use-a-collection (rest collection)) ))


(recursively-use-a-collection [1 2 3 4 5] )




;; ;; recursion with polymorphism

(defn sum 
  ([vals] (sum vals 0) )

  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (sum (rest vals) (+ (first vals) accumulating-total)))))
   
(sum [1 2 3 4 5] )
(sum (vec (range 1 6)) )
(sum [1 2 3 4 5] 100 )

;; if we have a very large collection, we run the risk of blowing our heap space
;; (sum (vec (range 1 999999999)) )


;; using tail call optimizaiton allows us to reuse a memory location when we call a function recursively

;; recur allows the processing of a very large data set without blowing the heap space


(defn sum 
  ([vals] (sum vals 0) )

  ([vals accumulating-total]
   (if (empty? vals)
     accumulating-total
     (recur (rest vals) (+ (first vals) accumulating-total)))))
   
(sum [1 2 3 4 5] )
(sum (vec (range 1 6666666)) )
(sum [1 2 3 4 5] 100 )
