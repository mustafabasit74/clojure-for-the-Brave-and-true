(map (fn [name] (str "Hi, " name) )
     ["Syed Nasir" "Zakir" "Usmaan"] )      



( inc                    1)
( (fn [x] (+ x 1) )      1)


(def my-special-multiplier  (fn[x] (* x 5)) )

(my-special-multiplier 2)



( (fn[[first-choice second-choice & unimportant-choices]]
    (println "Your first choice is: " first-choice)
    (println "Your second choice is:" second-choice)
    (println "Sorry, we are ignoring the rest of your choices"
              "Here they are in case you need to cry over them: " 
               (clojure.string/join ", " unimportant-choices)) )
    ["Powerbank" "Phone" "Laptop" "Speaker" "Earphone"]  )  



(#(* % 2) 5 )

(map #(str "Hi, " %)
     ["Syed Nasir" "Eshaan" "Zahid" "Usmaan"] )

;; This strange-looking style of writing anonymous functions is made 
;; possible by a feature called reader macros.


(#(str "I like " %) "grapes")

(#(str "I like " %1 " and " %2 ) "grapes" "orange")

;; % is equivalent to %1
(#(str "I like " %1 " and " % ) "grapes" ) 

;; ************************
;; (#(str "I like " %1 " and " %1 ) "grapes" "orange" )
;; Execution error (ArityException)
;; Wrong number of args (2) passed to:


;; Identity returns the argument it’s given without altering it. 
(identity 1)
(identity "Basit")

;; (identity "Basit" "Danish")

(#(identity %&)  1 "Basit" "Dahish" :something)












