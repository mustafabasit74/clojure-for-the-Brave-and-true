;; recur is a real loop in Clojure, it implements tails recursion

;; if we have a very large collection to process via recursive function, we run the risk of blowing our stack

(defn sum 
    ([vals] (sum vals 0) )

    ([vals accumulating-total]
     (if (empty? vals)
       accumulating-total
       (sum (rest vals) (+ (first vals) accumulating-total)))))

(sum (range 0 10000))
;; => Execution error (StackOverflowError) at user/sum (REPL:7).

;; Tail call optimizaiton allows us to reuse a memory location when we call a function recursively

(defn sum 
    ([vals] 
      (sum vals 0) )
  
    ([vals accumulating-total]
     (if (empty? vals)
       accumulating-total
       (recur (rest vals) (+ (first vals) accumulating-total)))))
  
(sum (range 0 10000000)) 
;; => 49999995000000


;; Even though we can write code without recur, the use of recur is strongly recommended 
;; in Clojure because of tail-call optimization (TCO).
   