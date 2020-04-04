;; At this point, you might object that I’m just kicking the can down the road
;; because we’re still left with the problem of how a function like first, rest, cons are able
;; to work with different data structures. Clojure does this using two forms of indirection. 

;; ***
;; In programming, indirection is a generic term for the mechanisms
;; a language employs so that one name can have multiple, related meanings.

;; In this case, the name first has multiple, data structure–specific meanings.
;; Indirection is what makes abstraction possible.
;; Polymorphism is one way that Clojure provides indirection. I don’t want
;; to get lost in the details, but basically, polymorphic functions dispatch to
;; different function bodies based on the type of the argument supplied. (It’s
;; not so different from how multiple-arity functions dispatch to different
;; function bodies based on the number of arguments you provide.)

;; When it comes to sequences, Clojure also creates indirection by doing a
;; kind of lightweight type conversion, producing a data structure that works
;; with an abstraction’s functions. Whenever Clojure expects a sequence—for
;; example, when you call map, first, rest, or cons — it calls the seq function
;; on the data structure in question to obtain a data structure that allows for
;; first, rest, and cons:

;; Clojure has two constructs for defining polymorphic dispatch: the host platform’s
;; interface construct and platform-independent protocols. - ???? 

(map inc [1 2 3])
(map inc '(1 2 3))
(map inc #{1 2 3})

;; how to increment map-elements, pending 
;; (map #(..... %) {:a 1 :b 2 :c 3})



(seq '(1 2 3))
(seq [1 2 3])
(seq #{1 2 3})

(seq {:name "Mustafa Basit" :interest "Clojure"})

(into {} (seq {:name "Mustafa Basit" :interest "Clojure"}))

(into [] (seq [1 2 3 4 5 ]))


(into [] '(1 2  3 ))

(into '(2 3 ) [1])

(into '() '(1 2 3 ))
(into '() [1 2 3 ])

(into {} '({:name "Mustafa Basit" :interest "Clojure"} {:name "Zakir" :interest "Python"}))


;; Clojure’s sequence functions use seq on their arguments first

;; critter - a living creature;; an animal.
;; human & critter intake for the past four days

(def human-consumption [4 1 5 9])
(def critter-consumption [0 4 8 1 2])

;; (def human-consumption '(4 1 5 9))
;; (def critter-consumption '(0 4 8 1 2))


(defn unify-diet-data
  [human critter]
  {:human human
   :critter critter} )

(map unify-diet-data human-consumption critter-consumption)
