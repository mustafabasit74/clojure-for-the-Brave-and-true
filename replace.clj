(require '[clojure.string :as s])

(defn clean 
  [text]
  (s/replace (s/trim text) #"lol" "LOL"))

(clean "My boa constrictor is so sassy lol!    ")
;; => "My boa constrictor is so sassy LOL!"

;;Combining functions like this—so that the return value of one function is passed as an 
;; argument to another—is called function composition. 

;; The boa constrictor, also called the red-tailed boa or the common boa,
;; is a species of large, non-venomous, heavy-bodied snake that is frequently kept and bred in captivity.
 
(defn clean 
  [text]
  (reduce #(%2 %1) 
          text
          [s/trim #(s/replace % #"lol" "LOL" )]))

(clean "My boa constrictor is so sassy lol!    ")
;; => "My boa constrictor is so sassy LOL!"


((comp #(s/replace % #"lol" "LOL") s/trim) "My boa constrictor is so sassy lol!    ")
;; => "My boa constrictor is so sassy LOL!"
