(and true (= 1 1) "hello" )

(and true (= 1 1) false "hello" )

(and true (= 1 1) nil  false "hello" )

(and true (= 1 1) (println "Clojurist" ) "Clojure TV"  nil  false "hello" )

;; and can be used as "if statement"
;; eg print "hello" if number is equal to 10

;; beautiful hack sometimes, it can be used with "some" to return the value (see some)
(def n 10)
(and (= n 10) (print-str "hello"))