;; Validators allow you to be super controlling and restrict what 
;; states are allowable. 

;; Validators let you specify what states are allowable for a reference.
(def fred (atom {:cuddle-hunger-level 0
                 :percent-deteriorated 20}))
@fred
;; => {:cuddle-hunger-level 0, :percent-deteriorated 20}

(swap! fred update-in [:percent-deteriorated] - 5000)
;; => {:cuddle-hunger-level 0, :percent-deteriorated -4980}

;; ensure that a zombie’s :percent-deteriorated is between 0 and 100: 

(reset! fred{:cuddle-hunger-level 10
             :percent-deteriorated 20})

(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (and (>= percent-deteriorated 0) (<= percent-deteriorated 100)))

(set-validator! fred percent-deteriorated-validator)
;; => (set-validator! fred percent-deteriorated-validator)

(swap! fred update-in [:percent-deteriorated] + 55)
;; => {:cuddle-hunger-level 10, :percent-deteriorated 75}

(swap! fred update-in [:percent-deteriorated] + 900)
;; => Execution error (IllegalStateException) at user/eval64535 (form-init7132465729232468380.clj:1) .
;;    Invalid reference state
;;    class java.lang.IllegalStateException

@fred
;; => {:cuddle-hunger-level 10, :percent-deteriorated 75}

;; You can throw an exception to get a more descriptive error message:
(defn percent-deteriorated-validator
  [{:keys [percent-deteriorated]}]
  (or (and (>= percent-deteriorated 0) (<= percent-deteriorated 100))
      (throw (IllegalStateException. "percent-deteroirated should be in between 0 and 100"))))
;; => #'user/percent-deteriorated-validator

(swap! fred update-in [:percent-deteriorated] + 900)
;; => Execution error (IllegalStateException) at user/eval67289 (form-init7132465729232468380.clj:42).
;;    Invalid reference state




