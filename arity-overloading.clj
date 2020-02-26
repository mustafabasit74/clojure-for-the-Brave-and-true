(defn arity-overloading
 "This program demonstrate arity-overloading in clojure"
 ([a b c] 
  (println (str  "Addition - " a " + " b " + " c " = " (+ a b c) ) ) ) 
 
 ([a b]
  (println (str "Subtraction - " a " - " b " = " (- a b) ) ) ) 
 
 ([a] 
  (println (str "Change the Sign : " (* a -1) ) ) ) ) 


(arity-overloading 5 3 )
(arity-overloading 5 3 2 )
(arity-overloading -1 )



(defn raise-salary 
 "provide the ID and name(optional) of the employee whose salary you want increase by 10%"
 ([id name]
  (println "Processing....." )
  (str "Salary of " name ", has been sucessfully increased.") ) 
 
 ([id]
  (raise-salary id (str "#EMP-ID-" id) ) ) ) 


(raise-salary 10055 "Basit" )
(raise-salary 10055 )

