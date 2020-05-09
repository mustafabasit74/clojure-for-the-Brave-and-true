;; Watches allow you to be super creepy and check in on your reference types’
;; every move. 
;; see also - atom.clj

;; A watch is a function that takes four arguments: 
;; a key, that you can use for reporting
;; the reference being watched,
;; its previous state,
;; its new state.
;; You can register any number of watches with a reference type.

(def fred (atom {:cuddle-hunger-level 0
                 :precent-deteriorated 0}))
@fred
(swap! fred
        #(merge-with +
                     %
                     {:cuddle-hunger-level 1
                      :percent-deteriorated 2}))
(reset! fred {:cuddle-hunger-level 22
              :percent-deteriorated 2})
;; => {:cuddle-hunger-level 22, :percent-deteriorated 2}

;; calculate zombie's shuffle speed
(defn shuffle-speed
  [zombie]
  (* (:cuddle-hunger-level zombie) 
     (- 100 (:percent-deteriorated zombie))))

;; alert whenever a zombie’s shuffle speed reaches the dangerous level of 5,000
(defn shuffle-alert
  [key watched old-state new-state]
  (let [sph (shuffle-speed new-state)]
    (if (> sph 5000)
      (do 
        (println "Run, you fool")
        (println "The Zombie's SHP is now " sph)
        (println "This message brought to your courtesy of " key))
      (do 
        (println "All is well with " key)
        (println "Cuddle hunger: " (:cuddle-hunger-level new-state))
        (println "Percent deteriorated: " (:percent-deteriorated new-state))
        (println "SPH: " sph)))))

;; The general form of add-watch is (add-watch ref key watch-fn) .

(add-watch fred :fred-shuffle-alert shuffle-alert)
;; => #atom[{:cuddle-hunger-level 22, :percent-deteriorated 2} 0x1f47d503]


(swap! fred update-in [:percent-deteriorated] + 1)
;; => All is well with  :fred-shuffle-alert
;;    Cuddle hunger:  22
;;    Percent deteriorated:  3
;;    SPH:  2134

@fred
;; => {:cuddle-hunger-level 22, :percent-deteriorated 13}


(swap! fred update-in [:cuddle-hunger-level] + 5)
;; => All is well with  :fred-shuffle-alert
;;    Cuddle hunger:  27
;;    Percent deteriorated:  3
;;    SPH:  2619
;;    {:cuddle-hunger-level 27, :percent-deteriorated 3}

@fred
;; => {:cuddle-hunger-level 27, :percent-deteriorated 3}


(swap! fred update-in [:cuddle-hunger-level] + 30)
;; => Run, you fool
;;    The Zombie's SHP is now  5529
;;    This message brought to your courtesy of  :fred-shuffle-alert
;;    {:cuddle-hunger-level 57, :percent-deteriorated 3}





(def customer (atom {:name "Mr. Hyder Bhat"
                     :outstanding-balance 520}))

(defn balance-check
  [key watched old-state new-state]
  (let [name (:name new-state)
        current-balance (:outstanding-balance new-state)
        amount-paid (- (:outstanding-balance old-state) (:outstanding-balance new-state))]    
    (println "Thanks" name "for paying rupees" amount-paid)
    
    (if (= current-balance 0)
      (println "Your Account has been closed, as you have paid all the money Back")      
      (println "Your Account has still an outstanding balance of rupees" (:outstanding-balance new-state)))))

(add-watch customer :some-key-name balance-check )
;; => #atom[{:name "Mr. Hyder Bhat", :outstanding-balance 520} 0x1f65db25]

(defn pay
  [amount]
  (swap! customer 
         #(merge-with -
                      %
                      {:outstanding-balance amount})))
(pay 100)
;; => Thanks Mr. Hyder Bhat for paying rupees 100
;;    Your Account has still an outstanding balance of rupees 420
;;    {:name "Mr. Hyder Bhat", :outstanding-balance 420}

(pay 240)
;; => Thanks Mr. Hyder Bhat for paying rupees 240
;;    Your Account has still an outstanding balance of rupees 180
;;    {:name "Mr. Hyder Bhat", :outstanding-balance 180}

(pay 180)
;; => Thanks Mr. Hyder Bhat for paying rupees 180
;;    Your Account has been closed, as you have paid all the money Back
;;    {:name "Mr. Hyder Bhat", :outstanding-balance 0}

;; remove watch
(def phone (atom {:price 12999}))

(defn phone-watch
  [key watched old-state new-state]
  (println "From" old-state "=>" new-state))

(add-watch phone :phone-watch-key phone-watch)
;; => #atom[{:price 12999} 0x32cc923a]

(swap! phone update-in [:price] + 200)
;; => From {:price 12999} => {:price 13199}
;;    {:price 13199}

(swap! phone update-in [:price] + 500)
;; => From {:price 13199} => {:price 13699}
;;    {:price 13699}

(remove-watch phone :phone-watch-key)
;; => #atom[{:price 13699} 0x11815278]

(swap! phone update-in [:price] + 500)
;; => {:price 14199}

