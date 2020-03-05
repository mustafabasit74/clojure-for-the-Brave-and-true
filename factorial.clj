;;deferred operations
(defn fact
  [num]
  (if (= num 1 )
     num
    (* num  (fact (- num 1) ) ) ) ) 

(fact 5)



