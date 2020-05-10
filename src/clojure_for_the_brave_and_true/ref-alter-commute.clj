;; Atoms are ideal for managing the state of independent identities. Sometimes
;; though, we need to express that an event should update the state of more
;; than one identity simultaneously. Refs are the perfect tool for this scenario.

;; The atom reference type allows you to create an identity that you can safely 
;; update to refer to new values using swap! and reset!. 
;; The ref reference type is handy when you want to update more than one identity
;;  using transaction semantics, and you update it with alter! and commute!.

;; recording sock gnome transactions

(def sock-varieties #{"darned" "argyle" "wool" "horsehair" "mulleted"
                      "passive-aggressive" "striped" "polka-dotted"
                      "athletic" "business" "power" "invisible" "gollumed"})
(defn sock-count
  [sock-variety count]
  {:variety sock-variety
   :count count})

(defn generate-sock-gnome
  [name]
  {:name name
   :socks #{}})


(def sock-gnome (ref (generate-sock-gnome "ZakirGulzarMir")))
(def dryer (ref {:name "LG 123"
                 :socks (set (map #(sock-count % 2) sock-varieties))}))
@sock-gnome
;; => {:name "ZakirGulzarMir", :sock #{}}

@dryer
;; => {:name "LG 123", :socks #{{:variety "gollumed", :count 2} {:variety "striped", :count 2} {:variety "wool", :count 2} {:variety "passive-aggressive", :count 2} {:variety "argyle", :count 2} {:variety "business", :count 2} {:variety "darned", :count 2} {:variety "polka-dotted", :count 2} {:variety "horsehair", :count 2} {:variety "power", :count 2} {:variety "athletic", :count 2} {:variety "mulleted", :count 2} {:variety "invisible", :count 2}}}

sock-gnome
;; => #ref[{:status :ready, :val {:name "ZakirGulzarMir", :sock #{}}} 0x3d157411]
dryer
;; => #ref[{:status :ready, :val {:name "LG 123", :socks #{{:variety "gollumed", :count 2} {:variety "striped", :count 2} {:variety "wool", :count 2} {:variety "passive-aggressive", :count 2} {:variety "argyle", :count 2} {:variety "business", :count 2} {:variety "darned", :count 2} {:variety "polka-dotted", :count 2} {:variety "horsehair", :count 2} {:variety "power", :count 2} {:variety "athletic", :count 2} {:variety "mulleted", :count 2} {:variety "invisible", :count 2}}}} 0x44d0e9ef]

(defn steal-sock
  [gnome dryer]
  (dosync
   (when-let [pair (some #(if (= (:count %) 2) %) (:socks @dryer))]
     (let [updated-count (sock-count (:variety pair) 1)]
       (alter gnome update-in [:socks] conj updated-count)
       (alter dryer update-in [:socks] disj pair)
       (alter dryer update-in [:socks] conj updated-count)))))

(steal-sock sock-gnome dryer)
(:socks @sock-gnome)
;; => #{{:variety "gollumed", :count 1}}

(steal-sock sock-gnome dryer)
(:socks @sock-gnome)
;; => #{{:variety "gollumed", :count 1} {:variety "striped", :count 1}}



(defn similar-socks
  [target-sock sock-set]
  (filter #(= (:variety %) (:variety target-sock)) 
         sock-set ))

(similar-socks (first (:socks @sock-gnome)) (:socks @dryer))
;; => ({:variety "gollumed", :count 1})

;; ***
;; when you alter a ref, the change isn’t immediately visible outside of the current transaction.
;; This is what lets you call alter on the dryer twice within a transaction without
;; worrying about whether dryer will be read in an inconsistent state. Similarly
;; if you alter a ref and then deref it within the same transaction, the deref will return the new state.

;; ***
;; in-transaction state
(def counter (ref 0))
(do

(future 
  (dosync 
   (alter counter inc)
   (println @counter)
   (Thread/sleep 500)
   (alter counter inc)
   (println @counter)
   ))

(Thread/sleep 250)
(println @counter))
;; => 1
;;    0
;;    2

;; It’s like the transaction has its own private area for trying out changes to the
;; state, and the rest of the world can’t know about them until the transaction is done. 

;; The transaction will try to commit its changes only when it ends. The
;; commit works similarly to the compare-and-set semantics of atoms.


;; commute

;; commute behaves like this at commit time:
;; 1. Reach outside the transaction and read the ref’s current state.
;; 2. Run the commute function again using the current state.
;; 3. Commit the result.

;; commute doesn’t ever force a transaction retry. This can help improve performance

;; safe and unsafe uses of commute

;; safe use
(defn sleep-print-update
  [sleep-time thread-name update-fn]
  (fn [state]
    (Thread/sleep sleep-time)
    (println (str thread-name ":" state))
    (update-fn state)))

(def counter (ref 0))

(do
  (future (dosync (commute counter (sleep-print-update 100 "Thread A" inc))))
  (future (dosync (commute counter (sleep-print-update 150 "Thread B" inc)))))

@counter
;; => 2

;; unsafe use
(def reciever-a (ref #{}))
(def reciever-b (ref #{}))
(def giver (ref #{1}))

(do
  (future (dosync
           (let [gift (first @giver)]
             (Thread/sleep 10)
             (commute reciever-a conj gift)
             (commute giver disj gift))))
  (future (dosync
           (let [gift (first @giver)]
             (Thread/sleep 50)
             (commute reciever-b conj gift)
             (commute giver disj gift)))))

@reciever-a
;; => #{1}

@reciever-b
;; => #{1}

@giver
;; => #{}

;; research the ensure function and the phenomenon write skew.