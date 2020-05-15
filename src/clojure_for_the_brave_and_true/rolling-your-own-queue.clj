;; The macro will require you to hold runtime logic and macro expansion logic in your head at the same
;; time to understand what’s going on

;; One characteristic The Three Concurrency Goblins have in common is
;; that they all involve tasks concurrently accessing a shared resource—a variable,
;; a printer, a dwarven war axe—in an uncoordinated way. If you want
;; to ensure that only one task will access a resource at a time, you can place
;; the resource access portion of a task on a queue that’s executed serially.
;; It’s kind of like making a cake: you and a friend can separately retrieve the
;; ingredients (eggs, flour, eye of newt, what have you), but some steps you’ll
;; have to perform serially.

;; British greeting “Ello, gov’na! Pip pip! Cheerio!”

(defmacro wait
  [timeout & body]
  `(do
     (Thread/sleep ~timeout)
     ~@body))

(wait 1000 (println "Do") (println "Something....."))
;; => Do
;;    Something.....

;; split tasks into a concurrent portion and a serialized portion

(let [saying3 (promise)]
  (future (deliver saying3 (wait 100 "Cheerio!")))
  @(let [saying2 (promise)]
     (future (deliver saying2 (wait 400 "Pip Pip!")))
     @(let [saying1 (promise)]
        (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
        (println @saying1)
        saying1)
     (println @saying2)
     saying2)
  (println @saying3))

;; write few examples on @(let .... return ...)


;; but it is working this way as well,   
(let [saying3 (promise)]
  (future (deliver saying3 (wait 100 "Cheerio!")))
  (let [saying2 (promise)]
    (future (deliver saying2 (wait 400 "Pip Pip!")))
    (let [saying1 (promise)]
      (future (deliver saying1 (wait 200 "'Ello, gov'na!")))
      (println @saying1))
    (println @saying2))
  (println @saying3))


;;The overall strategy is to create a promise for each task and create a corresponding future
;;that will deliver a concurrently computed value to the promise.
;; This ensures that all of the futures are created before any of the promises are dereferenced.

;; Returning saying1 in a let block and dereferencing the let block ensures that you’ll be 
;; completely finished with saying1 before the code moves on to do anything to saying2

;; see also - the-macros.clj 
(do
  (println "Hello")
  (println "World"))


(defmacro do-something
  [function & body]
  `(~function ~@body))

(do-something do (println "Hello") (println "World"))
;; => Hello
;;    World

(macroexpand '(do-something do (println "Hello") (println "World")))
;; => (do (println "Hello") (println "World"))




;; nested something...
(do
  (do
    (println {:a 1 :b 2}))
  (println "Hello World"))
;; => {:a 1, :b 2}
;;    Hello World


(defmacro nested-something

  ([function action]
   `(nested-something " " ~function ~action))

  ([put-in-between-F-A function action]
   `(~function
     ~put-in-between-F-A
     ~action)))

(nested-something do (println "Hello World"))
;; => Hello World 

;; ***********************************
(macroexpand '(nested-something do (println "Hello World")))
;; => (do " " (println "Hello World"))

(nested-something (do (println "A" "B" "C"))  do  (println "Hello World"))
;; => A B C
;;    Hello World

(macroexpand '(nested-something (do (println "A" "B" "C"))  do  (println "Hello World")))
;; => (do (do (println "A" "B" "C")) (println "Hello World"))


(-> (nested-something do (println "A" "B" "C"))
    (nested-something do (println "Hello World")))
;; => ;; => A B C
;;    Hello World


(macroexpand '(-> (nested-something do (println "A" "B" "C"))
    (nested-something do (println "Hello World"))
    (nested-something do (println "{:a 1 :b 2}"))))
;; =>output below 
(do (nested-something 
     (nested-something do (println "A" "B" "C"))
     do
     (println "Hello World"))
    (println "{:a 1 :b 2}"))


(macroexpand '(-> (nested-something do (println "A" "B" "C"))
                  (nested-something do (println "Hello World1"))
                  (nested-something do (println "Hello World2"))
                  (nested-something do (println "Hello World3"))
                  (nested-something do (println "{:a 1 :b 2}"))))
;; => (do (nested-something (nested-something (nested-something (nested-something do (println "A" "B" "C")) do (println "Hello World1")) do (println "Hello World2")) do (println "Hello World3")) (println "{:a 1 :b 2}"))




;; Rolling your own Queue in Action 
;; A macro that let you queue futures
(defmacro enqueue
  ([concurrent-promise-name concurrent serialised]
   `(enqueue (future) ~concurrent-promise-name ~concurrent ~serialised))

  ([q concurrent-promise-name concurrent serialised]
   `(let [~concurrent-promise-name (promise)]
      (future (deliver ~concurrent-promise-name ~concurrent))
      (deref ~q)
      ~serialised
      ~concurrent-promise-name)))


(-> (enqueue saying (wait 200 "'Ello, gov'na!") (println @saying))
    (enqueue saying (wait 400 "Pip Pip!") (println @saying))
    (enqueue saying (wait 100 "Cheerio!") (println @saying)))
;; => 'Ello, gov'na!
;;    Pip Pip!
;;    Cheerio!
;;    #promise[{:status :ready, :val "Cheerio!"} 0x3563ef59]




;; still lots of confusion - come back later
(macroexpand
 '(-> (enqueue saying1 (wait 200 "'Ello, gov'na!") (println @saying1))
      (enqueue saying2 (wait 400 "Pip Pip!") (println @saying2))
      (enqueue saying3 (wait 100 "Cheerio!") (println @saying3))))
;; => ouptput below
(let* [saying3 (clojure.core/promise)]
      (clojure.core/future (clojure.core/deliver saying3 (wait 100 "Cheerio!")))
      (clojure.core/deref (enqueue
                           ;; take below line as data, it will not get evaluated
                           (enqueue saying1 (wait 200 "'Ello, gov'na!") (println (clojure.core/deref saying1)))
                           saying2
                           (wait 400 "Pip Pip!")
                           (println (clojure.core/deref saying2))))
      (println (clojure.core/deref saying3))
      saying3)



;; ***
(macroexpand
 '(-> (enqueue saying1 (wait 200 "'Ello, gov'na!") (println @saying1))))
;; => outptu below
(let* [saying1 (clojure.core/promise)] 
      
      ;; concurrent
      (clojure.core/future 
       (clojure.core/deliver saying1 (wait 200 "'Ello, gov'na!"))) 
      
      (clojure.core/deref (clojure.core/future))
      
      ;; serialized
      (println (clojure.core/deref saying1))
      saying1)

