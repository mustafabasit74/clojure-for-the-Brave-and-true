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

;; another eg. to prove result gets cached
(def result (future (range 1 999)))
(time result)
;; => "Elapsed time: 0.017603 msecs"
;;    #future[{:status :ready, :val (1 2 3 ... 998)} 0x36685219]

(time result)
;; => "Elapsed time: 0.012351 msecs"
;;    #future[{:status :ready, :val (1 2 3 ... 998)} 0x36685219]


(def sym (future (Thread/sleep 4000)
                 (inc 2)
                 (+ 1 2)))
sym
;; => #future[{:status :pending, :val nil} 0x4667dae6]

;; after 4 sec
sym
;; => #future[{:status :ready, :val 3} 0x4667dae6]


;; 