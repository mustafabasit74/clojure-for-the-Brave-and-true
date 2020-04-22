;; lazy evaluation ensure that you get the information you want at the time you want it 

;; A lazy seq is a seq whose members aren’t computed until you try to access them.
;; Computing a seq’s members is called realizing the seq




;; To see lazy seqs in action, pretend that you’re part of a modern-day task
;; force whose purpose is to identify vampires. Your intelligence agents tell
;; you that there is only one active vampire in your city, and they’ve helpfully 
;; narrowed down the list of suspects to a million people. Your boss gives
;; you a list of one million Social Security numbers and shouts, “Get it done,McFishwich!”
;; Thankfully, you are in possession of a Vampmatic 3000 computifier,
;; the state-of-the-art device for vampire identification. Because the source
;; code for this vampire-hunting technology is proprietary, I’ve stubbed it out
;; to simulate the time it would take to perform this task. Here is a subset of a
;; vampire database: 

(def vampire-database
  {0 {:make-blood-puns? false :has-pulse? true :name "Syed Nasir"}
   1 {:make-blood-puns? false :has-pulse? true :name "Usmaan Bhat"}
   2 {:make-blood-puns? true :has-pulse? false :name "Zakir Gluzar"}
   3 {:make-blood-puns? true :has-pulse? true :name "Aarsh"} })

;; divide and conquer
;; make a function that reterive the data from db
;; make another function that compares reterived data with set of rules

;; make another function that will act as an interface
(defn vampire-related-details
  [social-security-number]
  (Thread/sleep 1000)
  (get vampire-database social-security-number))

(defn vampire?
  [record]
  (and (:make-blood-puns? record) (not (:has-pulse? record)) record ))

(defn identify-vampire
  [social-security-numbers]
  (first 
    (filter vampire?
            (map vampire-related-details social-security-numbers))))

;; map returns a lazy sequence

(time (vampire-related-details 0))
;; => "Elapsed time: 1000.237112 msecs"

;; A nonlazy implementation of map would first have to apply vampirerelated-details 
;; to every member of social-security-numbers before passing the result

;; map is lazy, it doesn’t actually apply vampire-related-details to Social Security numbers
;; until you try to access the mapped element. In fact, map returns a value almost instantly

(time (def mapped-details (map vampire-related-details (range 0 9999999999999999))))
;; => "Elapsed time: 0.070972 msecs"
;; => #'user/mapped-details

;; ***
;; You can think of a lazy seq as consisting of two parts: a recipe for how
;; to realize the elements of a sequence and the elements that have been
;; realized so far. When you use map, the lazy seq it returns doesn’t include
;; any realized elements yet, but it does have the recipe for generating its elements.
;; Every time you try to access an unrealized element, the lazy seq will
;; use its recipe to generate the requested element

(time (first mapped-details))
;; => "Elapsed time: 32005.818846 msecs"
;; => {:make-blood-puns? false, :has-pulse? true, :name "Syed Nasir"}

;; This operation took about 32 seconds. That’s much better than one
;; million seconds, but it’s still 31 seconds more

;; ***
;; The reason it took 32 seconds is that Clojure chunks its lazy sequences,
;; which just means that whenever Clojure has to realize an element, it preemptively 
;; realizes some of the next elements as well

;; Clojure went ahead and prepared the next 31 as well. Clojure does this because it almost always
;; results in better performance

(time (first mapped-details))
;; "Elapsed time: 0.024835 msecs"
;; {:make-blood-puns? false, :has-pulse? true, :name "Syed Nasir"}

;; Thankfully, lazy seq elements need to be realized only once. Accessing
;; the first element of mapped-details again takes almost no time


(time (identify-vampire (range 0 99999999999999999)))