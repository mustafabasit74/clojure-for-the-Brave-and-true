identity - returns its argument 

(identity 4)
(identity [1 2 3 4 5])

(identity nil)
(identity true)
(identity false)
(identity [])
(identity '())
(identity +)
(identity first) 

(identity [1 2 nil 3 4 true 5 false 6 7 ])

(filter identity [1 2 nil 3 4 true 5 false 6 7 ])
;; => (1 2 3 4 true 5 6 7)

