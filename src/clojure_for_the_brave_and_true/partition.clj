;; (partition n coll) (partition n step coll) (partition n step pad coll)

(partition 3 (range  1 20))
;; => ((1 2 3) (4 5 6) (7 8 9) (10 11 12) (13 14 15) (16 17 18))

;; step 
(partition 3 5 (range 1 20))
;; => ((1 2 3) (6 7 8) (11 12 13) (16 17 18))

(partition 3 5 (range 20))
;; => ((0 1 2) (5 6 7) (10 11 12) (15 16 17))

(partition 4 2 (range  20))
;; => ((0 1 2 3) (2 3 4 5) (4 5 6 7) (6 7 8 9) (8 9 10 11) (10 11 12 13) (12 13 14 15) (14 15 16 17) (16 17 18 19))

(partition 4 2 (range 1 20 ))
;; => ((1 2 3 4) (3 4 5 6) (5 6 7 8) (7 8 9 10) (9 10 11 12) (11 12 13 14) (13 14 15 16) (15 16 17 18))

;; pad
(partition 5 (range 1 24))
;; => ((1 2 3 4 5) (6 7 8 9 10) (11 12 13 14 15) (16 17 18 19 20))

;; when there are not enough items to fill the last partition, a pad can be supplied.
;; when a pad is supplied, the last partition may not be of the same size as the rest

(partition 5 5 [:a] (range 1 22))
;; => ((1 2 3 4 5) (6 7 8 9 10) (11 12 13 14 15) (16 17 18 19 20) (21 :a))

;; but only as many pad elements are used as necessary to fill the final partition.
(partition 5 5 [:a :b :c :d :e :f :g] (range 1 22))
;; => ((1 2 3 4 5) (6 7 8 9 10) (11 12 13 14 15) (16 17 18 19 20) (21 :a :b :c :d))


