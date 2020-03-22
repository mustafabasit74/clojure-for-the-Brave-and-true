 {}
{:one 1 :two 2}
{:one 1 :two {:one 1, :two 2}}





{:first-name "Mustafa" :last-name "Basit" }
{:first-name "Mustafa", :last-name "Basit" }
(get {:first-name "Mustafa" :last-name "Basit" } :first-name )
(get {:a 1 :b 2 } :A )



{:primary-operation + :secondary-operation - }
(get {:primary-operation + :secondary-operation - } :secondary-operation )
( (get {:primary-operation + :secondary-operation - } :secondary-operation )  20  5 )



{:primary-operation + :secondary-operation (def sum -) }

( (get {:primary-operation + :secondary-operation (def sum -) } :secondary-operation )  20 5 )


(get {:primary-operation + :secondary-operation (if (= nil (println "wow") ) 
                                                             -
                                                             + ) } :secondary-operation ) 


( (get {:primary-operation + :secondary-operation (if (= nil (println "wow") ) 
                                                             -
                                                             + ) } :secondary-operation ) 20 5 )





(hash-map :one 1 :two 2 )
(hash-map :one 1 :two {:one 1 :two 2} :three 3)

(get {:first-name "Mustafa" :last-name "Basit"}  :middle-name ) 


(get {:first-name "Mustafa" :last-name "Basit"}  :middle-name "no data found" )


(get {:first-name "Mustafa" :last-name "Basit"}  :middle-name (when (= 1 1) 
                     						     (println "no")
								     (println "data")
								     (println "found") 
								     "bye" )  )



(get {:a 1 :b { :c 3 :d 4 } :e 5} :c  "no data found")
(get-in {:a 1 :b { :c 3 :d 4 } :e 5} [:b :c]   "no data found")

(get-in {:a 1 :b { :c 3 :d 4 } :e 5} [:b :e]   "no data found")

(get-in {:a 1 :b { :c 3 :d 4 } :e 5} [:a :e]   "no data found")


(get-in {:a 1 :b { :c 3 :d 4 } :e 5} [:b ]   "no data found") 


;; map as a function and key as argument
({:a 1 :b 2 :c 3 }  :c )


;; key as function and map as its argument
(:c {:a 1 :b 2 :c 3 } )

;; keywords can be used as a functions that look up the coressponding value in the data structure
(:first-name {:first-name "Mustafa" :last-name "Basit" })

;; ***** words of wisdom ****
;; Using a keyword as a function is pleasantly succinct, and Real Clojurists
;; do it all the time. You should do it too!


