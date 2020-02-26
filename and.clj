(and true (= 1 1) "hello" )

(and true (= 1 1) false "hello" )

(and true (= 1 1) nil  false "hello" )

(and true (= 1 1) (println "Clojurist" ) "Clojure TV"  nil  false "hello" )
