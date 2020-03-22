;; One issue with recursing is that for more complex cases the runtime is adding a large amount of memory
;; overhead. This is because it's adding stack frames with each recursive iteration.
;; It is, however, possible to get the best of both worlds: immutable recursive iteration and constant memory usage.


;; Tail call V/S Tail recursive

;; Tail call - A tail call is a subroutine call performed as the final action of a procedure

;; Tail recursive - If a tail call leads to the same subroutine, the subroutine is said 
;; to be tail-recursive, which is a special case of recursion.


;; stack frame consists of subroutine's parameters, local variables and its return address

(defn sum 
    ([vals] 
      (sum vals 0) )
  
    ([vals accumulating-total]
     (if (empty? vals)
       accumulating-total
       (recur (rest vals) (+ (first vals) accumulating-total)))))

(sum (range 1 999999999))
;; => 499999998500000001

(sum (range 1 999999999999999999))


;; The Clojure documentation describes loop-recur as "a hack so that something like 
;; tail-recursive-optimization works in clojure." This suggests that tail call optimisation 
;; is not available in the JVM, otherwise loop-recur would not be needed

