;; Stateless Concurrency and Parallelism with pmap
;; serial mapping v/s parallel mapping

(def alphabet-length 26)

(def letters (mapv 
              (comp str char (partial + 65))
              (range alphabet-length)))

(defn random-string
  [lenght]
  (apply str (take lenght (repeatedly #(rand-nth letters)))))

(defn random-string-list
  [list-length string-length]
  (doall (take list-length (repeatedly (partial random-string string-length)))))

(def orc-names (random-string-list 3000 7000))

(time (dorun (map clojure.string/lower-case orc-names)))
;; => "Elapsed time: 234.982125 msecs"

(time (dorun (pmap clojure.string/lower-case orc-names)))
;; => "Elapsed time: 169.331137 msecs"

;; Sometimes, pmap can actually take longer than map (due to overhead)

(def orc-names-abbrevs (random-string-list 30000 100))

(time (dorun (map clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 41.745823 msecs"

(time (dorun (pmap clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 123.95189 msecs"


;; pmap will take more time as compared to serial map if we will reduce the grain size
(def orc-names-abbrevs2 (random-string-list 50000 5))

(time (dorun (map clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 38.745823 msecs"

(time (dorun (pmap clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 161.95189 msecs"

;; The solution to this problem is to increase the grain size, or the
;; amount of work done by each parallelized task

;; To actually accomplish this in Clojure, you can increase the grain size
;; by making each thread apply clojure.string/lower-case to multiple elements
;; instead of just one, using partition-all.

(partition 2 [1 2 3 4 5 6 7])
;; => ((1 2) (3 4) (5 6))

(partition-all 2 [1 2 3 4 5 6 7])
;; => ((1 2) (3 4) (5 6) (7))

(def numbers [1 2 3 4 5 6 7 8 9 10])
(map inc numbers)

(pmap inc numbers)
;; => (2 3 4 5 6 7 8 9 10 11)
;; grain size = 1 


(pmap (fn [number-group]
        (doall (map inc number-group)))
        (partition-all 3 numbers))
;; => ((2 3 4) (5 6 7) (8 9 10) (11))
;; serial map on different cores, grain size increase to 3 (3 task on each core)

(apply concat 
 (pmap (fn [number-group]
         (doall (map inc number-group)))
       (partition-all 3 numbers)))
;; => (2 3 4 5 6 7 8 9 10 11)


;; Using this technique, we can increase the grain size of the orc name

;; Grain size isn’t measured in any standard unit, but you’d say that the grain size of
;; pmap is one by default.

;; grain size  = 1 (each core has to generate random string of 100 char length long)
(def orc-names-abbrevs (random-string-list 30000 100))

(time (dorun (map clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 41.745823 msecs"

(time (dorun (pmap clojure.string/lower-case orc-names-abbrevs)))
;; => "Elapsed time: 123.95189 msecs"

;; pmap with increased grain size
;; grain size  = 1000 (each core has to generate 1000 random string of 100 char length long)
 
(time 
 (apply concat
        (dorun 
         (pmap (fn [string-group]
                 (doall (map clojure.string/lower-case string-group)))
               (partition-all 1000 orc-names-abbrevs)))))
;; => "Elapsed time: 30.308703 msecs"

;; pmap - grain size 1             V/S  pmap - grain size 1000
;; "Elapsed time: 123.95189 msecs" V/S "Elapsed time: 30.308703 msecs"

;; we can generalize this technique into a function called ppmap, 
;; for partitioned pmap. It can receive more than one collection, just like map:
(defn ppmap
  [grain-size f & colls]
  (apply concat
         (apply pmap
                ????? - pending
                )

;; check out the clojure.core.reducers library (http://clojure.org/reducers/).
;; This library provides alternative implementations of seq functions like map
;; and reduce that are usually speedier than their cousins in clojure.core 