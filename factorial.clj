;; linear recursive process - chain of deferred operations
(defn fact
  [num]
  (if (= num 1)
      1
      (* num (fact (- num 1) ) ) ) )

(fact 20)

;; linear iterative process - Efficient than above one
(defn fact-iter
  [ num accum ]
    (if (= num 0)
          accum
          (fact-iter (- num 1) (* accum num)) ) )

(defn fact
  [num] 
  (fact-iter num 1) )


(fact 20 )

