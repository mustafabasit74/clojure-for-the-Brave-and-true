;***Tradional approach & Mutability

;(def severity :mild )
(def severity :something-else)
(def error-message "OH GOD! IT'S A DISASTER! WE ARE ")
(if (= severity :mild) 
           (def error-message (str error-message "MILDLY INCONVIENCED!") )
           (def error-message (str error-message "DOOOOOOMED!") ) )

(str "output - " error-message )






;***functioanl approach & Immutablity

(defn error-message
 [severity] 
 (str "OH GOD! IT'S A DISASTER! WE ARE "
      (if (= severity :mild)
	  "MILDLY INCONVIENCED!"
 	  "DOOOOOMEED!" ) ) )	

(error-message :mild)
;(error-message :something-else)

