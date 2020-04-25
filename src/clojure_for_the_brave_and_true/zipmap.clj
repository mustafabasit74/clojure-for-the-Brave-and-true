;; Returns a map with the keys mapped to the corresponding vals

(zipmap [:a :b :c] [1 2 3])
;; => {:a 1, :b 2, :c 3}

(zipmap '(:a :b) [1 2 3])
;; => {:a 1, :b 2}

(zipmap [1 2 3 4 5 6]
        (repeat 
         (zipmap [:pegged :connections]
                 [true  (zipmap  
                         (repeatedly 5 #(rand-int 15))
                         (repeatedly 5 #(rand-int 15)))])))
;; => {1 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}},
;;     2 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}},
;;     3 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}},
;;     4 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}},
;;     5 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}},
;;     6 {:pegged true, :connections {6 8, 8 14, 7 6, 13 2, 12 14}}}

(zipmap [:a :b :c :d :e :e :e :e :e]
        (repeat 0))
;; => {:a 0, :b 0, :c 0, :d 0, :e 0}


