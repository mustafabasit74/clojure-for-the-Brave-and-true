(def order-details
  {:name "Mustafa Basit"
   :email "mailtobait74gmail.com"})

(def order-details-validations
  {:name ["Please enter a name :" not-empty] 
   :email ["please enter an email address" not-empty
           
           "your email address doesn't look like an email address" 
           #((or (empty? %) (re-seq #"@" %)))]})

(defn error-message-for
  [data message-validator-pairs]
  (map first (filter #(not ((second %) data))
                     (partition 2 message-validator-pairs))))

(error-message-for "" ["name should not be empty" not-empty])
;; => ("name should not be empty")
 
(error-message-for 33 ["number should be even" even?])
;; => ("number should be even")
;; 
(error-message-for 33 ["number should be even" even?
                       "number should be <= 50" #(>= % 50)])
;; => ("number should be even" "number should be <= 50")

(error-message-for 2 ["number should be even" even?
                       "number should be <= 50" #(>= % 50)])
;; => ("number should be <= 50")

(defn validate
  [user-data validations]
  (reduce (fn [errors validation]
            (let [[field-name validation-vector] validation
                  value (get field-name user-data)
                  error-messages (error-message-for value validation-vector)]
             (if (empty? error-messages)
               errors
               (assoc errors field-name error-messages ))))
          {}
          validations))

(validate order-details order-details-validations)