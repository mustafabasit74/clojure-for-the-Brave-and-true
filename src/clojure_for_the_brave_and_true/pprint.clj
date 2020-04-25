;; Pretty print

(def map1 (zipmap [:a :b :c :d] [1 2 3 4]))
map1
;; => {:a 1, :b 2, :c 3, :d 4}

(clojure.pprint/pprint map1)
;; => {:a 1, :b 2, :c 3, :d 4}
;; => nil

(def big-map (zipmap [:a :b :c :d :e]
                     (repeat
                      (zipmap [:a :b :c :d :e]
                              (take 5 (range))))))
big-map
;; => {:a {:a 0, :b 1, :c 2, :d 3, :e 4}, :b {:a 0, :b 1, :c 2, :d 3, :e 4}, :c {:a 0, :b 1, :c 2, :d 3, :e 4}, :d {:a 0, :b 1, :c 2, :d 3, :e 4}, :e {:a 0, :b 1, :c 2, :d 3, :e 4}}

(clojure.pprint/pprint big-map)
;; => {:a {:a 0, :b 1, :c 2, :d 3, :e 4},
;;     :b {:a 0, :b 1, :c 2, :d 3, :e 4},
;;     :c {:a 0, :b 1, :c 2, :d 3, :e 4},
;;     :d {:a 0, :b 1, :c 2, :d 3, :e 4},
;;     :e {:a 0, :b 1, :c 2, :d 3, :e 4}}
;;     nil
