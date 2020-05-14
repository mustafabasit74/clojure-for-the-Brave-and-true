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

;;The overall strategy is to create a promise for each task and create a corresponding future
;;that will deliver a concurrently computed value to the promise.
;; This ensures that all of the futures are created before any of the promises are dereferenced.

;; Returning saying1 in a let block and dereferencing the let block ensures that you’ll be 
;; completely finished with saying1 before the code moves on to do anything to saying2

(time 
(let [saying3 (promise)]
  (future (deliver saying3 (wait 1000 "Cheerio!")))
  (let [saying2 (promise)]
     (future (deliver saying2 (wait 1000 "Pip Pip!")))
     @(let [saying1 (promise)]
        (future (deliver saying1 (wait 2000 "'Ello, gov'na!")))
        (println @saying1)
        saying1)
    (println @saying2))
  (println @saying3))
)