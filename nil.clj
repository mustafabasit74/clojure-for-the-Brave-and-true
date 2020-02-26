(nil? 3)

(nil? nil)

(nil? (do (println "hello ")
          (println "world" ) ) )

(= nil false)

(= nil nil)


(if nil
    "hello"
    "hi" ) 


(nil? (if nil "hello") ) 

(= true (nil? (if nil "hello") ) ) 
