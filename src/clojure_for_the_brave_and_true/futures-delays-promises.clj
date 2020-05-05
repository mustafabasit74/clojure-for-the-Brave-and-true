;; When you write serial code, you bind together these three events:
;; Task definition
;; Task execution
;; Requiring the task’s result

;; As an example, look at this hypothetical code, which defines a simple API call task:
;; (web-api/get :dwarven-beard-waxes)
;; As soon as Clojure encounters this task definition, it executes it. It
;; also requires the result right now, blocking until the API call finishes.

;; ***
;; Part of learning concurrent programming is learning to identify when these
;; chronological couplings aren’t necessary. Futures, delays, and promises
;; allow you to separate task definition, task execution, and requiring the result.

;; Futures
;; futures define a task and place it on another
;; thread without requiring the result immediately

(do
  (future (Thread/sleep 4000)
          (println "I will print after 4 seconds"))
  (println "I will print imediately"))

;; ***
;; future creates a new thread and places each
;; expression you pass it on the new thread, including Thread/sleep

;; ***
;; The future function returns a reference value that you can use to request the result

;; Requesting a future’s result is called dereferencing the future, and you
;; do it with either the deref function or the @reader macro
(let [result (future 
               (println "this prints once")
               (+ 1 1 ))]
  (println "dref: " (deref result))
  (println "dref: " @result))
;; => this prints once
;;    dref:  2
;;    dref:  2
;; ***
;; A future’s body executes only once, and its value gets cached.
;; the string "this prints once" indeed prints only once, even
;; though you dereference the future twice. This shows that the future’s body
;; ran only once and the result, 2, got cached

;; prove using (time ) result gets cached, add another example - pending 


(def sym (future (Thread/sleep 4000)
                 (inc 2)
                 [1 2 3 4 5]))
sym
;; => #future[{:status :pending, :val nil} 0x1c57399]

;; after 4 sec
sym
;; => #future[{:status :ready, :val [1 2 3 4 5]} 0x1c57399]

;; Dereferencing a future will block execution if the future hasn’t finished running
(let [result (future (Thread/sleep 4000)
                     (+ 1 2))]
  (println "The result is:" @result)
  (println "It will be at least 4 seconds before I print"))

;; Sometimes you want to place a time limit on how long to wait for a future.

(let [result (future (Thread/sleep 4000)
                     (+ 1 2))]
  (println "The result is:" (deref result 2000 5))
  (println "It will be at least 2 seconds before I print"))

;; This code tells deref to return the value 5 if the future doesn’t return avalue within 2000 milliseconds.

(realized? (future (Thread/sleep 2000)
                   (+ 1 1)))
;; => false

(realized? (future (+ 1 1)))
;; => false
;; ???


(let [result (future (+ 1 1))]
  (if (realized? result)
    (str "future realised, result: " @result)
    "Not yet Realised"))
;; => "Not yet Realised"

(let [result2 (future (+ 1 1))
      consume-time (range 1 20)]
  (println consume-time)
  (if (realized? result2)
    (str "future realised, result: " @result2)
    "Not yet Realised"))
;; => (1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19)
;;    "future realised, result: 2"

;; Futures are a dead-simple way to sprinkle some concurrency on your program.

;; On their own, they give you the power to chuck tasks onto other
;; threads, which can make your program more efficient. They also let your
;; program behave more flexibly by giving you control over when a task’s result is required.

;; ***
;; When you dereference a future, you indicate that the result is required
;; right now and that evaluation should stop until the result is obtained.
;; This can help you deal with the mutual exclusion problem

;; ***
;; Alternatively, you can ignore the result. For example, you can use
;; futures to write to a log file asynchronously, in which case you don’t need
;; to dereference the future to get any value back.


;; Clojure also allows you to treat task definition and requiring the result
;;  independently with delays and promises.

;; Delay
;; Delays allow you to define a task without having to execute it or require the
;; result immediately.

(def jackson-5-delay
  (delay (let [message "Just call my name and I will be there"]
           (println "First deref:" message)
           message)))
(force jackson-5-delay)
;; => First deref: Just call my name and I will be there
;;    "Just call my name and I will be there"

(force jackson-5-delay)
;; => "Just call my name and I will be there"
;; Like futures, a delay is run only once and its result is cached

(deref jackson-5-delay)
@jackson-5-delay

;; ***
;; One way you can use a delay is to fire off a statement the first time one
;; future out of a group of related futures finishes.

(def data [1 2 3 4 5 6 7 8 9 10])

(defn process-item
  "perform a specific task"
  [item]
  (println "processing item:" item)
  true)

(defn send-notification
  [email]
  (println "sending notification to " email ", we have started processing your data"))

(let [notify (delay (send-notification "mailtobasit@gmail.com"))]
  (doseq [item data]
    (future 
      (process-item item)
      (force notify))))
;; => processing item: processing item:processing item:  213
;;    sending notification to  mailtobasit@gmail.com , we have started processing your data
;;    processing item: 4
;;    processing item: 7
;;    processing item:processing item:  68
;;    processing item: 
;;    processing item: 5
;;    processing item: 10
;;    9

;; change println to 
;;(println (str "processing item:" item))
;; output 
;; => processing item:3processing item:1
;;    sending notification to  mailtobasit @gmail.com , we have started processing your data
;;    processing item:2
;;    processing item:7processing item:6
;;    processing item:10
;;    processing item:5
;;    processing item:8
;;    processing item:4
;;    processing item:9

;; Actual book example