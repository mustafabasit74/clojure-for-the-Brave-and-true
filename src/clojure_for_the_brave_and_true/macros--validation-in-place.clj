(def order-details
  {:name "Mustafa Basit"
   :email "mailtobait74gmail.com"})

(def order-details-validations
  {:name  ["Please enter a name" not-empty]
   
   :email ["please enter an email address" not-empty        
           "your email address doesn't look like an email address" 
           #(or (empty? %) (re-seq #"@" %))]})

(defn error-messages-for
  [data message-validator-pairs]
  (map first (filter #(not ((second %) data))
                     (partition 2 message-validator-pairs))))

(error-messages-for "" ["name should not be empty" not-empty])
;; => ("name should not be empty")
 
(error-messages-for 33 ["number should be even" even?])
;; => ("number should be even")
;; 
(error-messages-for 33 ["number should be even" even?
                       "number should be <= 50" #(>= % 50)])
;; => ("number should be even" "number should be <= 50")

(error-messages-for 2 ["number should be even" even?
                       "number should be <= 50" #(>= % 50)])
;; => ("number should be <= 50")

(defn validate
  [user-data validations]
  (reduce (fn [errors validation]
            (let [[field-name validation-vector] validation
                  value (field-name user-data)
                  error-messages (error-messages-for value validation-vector)]
             (if (empty? error-messages)
               errors
               (assoc errors field-name error-messages ))))
          {}
          validations))

(let [errors (validate order-details order-details-validations)]
  (if (empty? errors)
    (println :success)
    (println :failure errors)))
;; => :failure {:email (your email address doesn't look like an email address)}
;;    nil

;; *** 
;;  introduce an abstraction that would hide the repetitive parts: applying the 
;;  validate function, binding the result to some symbol, and checking whether the result is empty

(defn if-valid 
  [record validations success-code failure-code]
  (let [error (validate record validations)]
    (if (empty? error)
      success-code
      failure-code)))

;; However, this wouldn’t work, because success-code and failure-code
;; would get evaluated each time. A macro would work because macros let
;; you control evaluation.

(if-valid order-details order-details-validations (println "sucess") (println "failure" error))
;; => Syntax error compiling at (macros--validation-in-place.clj:65:70).
;;    Unable to resolve symbol: error in this context

(if-valid order-details order-details-validations (println "sucess") (println "failure" 'error))
;; => sucess
;;    failure error
;;    nil

(if-valid order-details order-details-validations '(println "sucess") '(println "failure" error))
;; => (println "failure" error)

;; how to do then?
;; A macro would work because macros let you control evaluation.

(defmacro if-valid
  [data validations & then-else]
  `(let [error# (validate ~data ~validations)]
     (if (empty? error#)
       ~@then-else)))

(if-valid {:name "Basit" :email "baist@gmail.com"} order-details-validations (println "Sucess") (println "failure"))
;; => Sucess
;; nil

(if-valid order-details order-details-validations (println "Sucess") (println "failure"))
;; => failure
;; nil

;; but how to print error message as well on failure?
;; pass another variable that will store error message, and attach here it with failure 

(defmacro if-valid
  [data validations errors-name & then-else]
  `(let [~errors-name (validate ~data ~validations)]
     (if (empty? ~errors-name)
       ~@then-else)))


(if-valid order-details order-details-validations errors (println "Sucess") (println "failure" errors ))
;; =>  failure {:email (your email address doesn't look like an email address)}
;;     nil

