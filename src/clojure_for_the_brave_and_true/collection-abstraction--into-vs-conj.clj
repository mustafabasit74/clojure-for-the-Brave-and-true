;; The sequence abstraction is about operating on members individually,
;; whereas the collection abstraction is about the data structure as a whole.
;; For example, the collection functions count, empty?, and every? aren’t about
;; any individual element; they’re about the whole:


(empty? [1 2 3 4 5 6 7 78 3734 473 27 27 27 191 28 28 227 27 373 636 252 5276373 3 2 83 474 2993 373 373 73])
;; => false

(count [1 2 [3 3 3] nil true false 7 + - 10 cons empty? 13 14 15 16])
;; => 16


(def numbers [1 2 3 4 5])

(rest numbers)
;; => (2 3 4 5)

;; get back restlts in the original form
(into [] (rest numbers))
;; => [2 3 4 5]


;; many seq functions return a seq rather than the original data structure.
;; You’ll probably want to convert the return value back into the original
;; value, and into lets you do that:

(map identity {:sunlight-reaction "Glitter!"})
;; => ([:sunlight-reaction "Glitter!"])

(into {} (map identity {:sunlight-reaction "Glitter"})) 
;; => {:sunlight-reaction "Glitter"}

;; Here, the map function returns a sequential data structure after being
;; given a map data structure, and into converts the seq back into a map


(map identity [:garlic 1  6 2 :fried-rice :potato "succlents"])
;; => (:garlic 1 6 2 :fried-rice :potato "succlents")

(into [] (map identity [:garlic 1  6 2 :fried-rice :potato "succlents"]))
;; => [:garlic 1 6 2 :fried-rice :potato "succlents"]

;; Although we can also do it by vect, then why should i choose into 
(vec (map identity [:garlic 1  6 2 :fried-rice :potato "succlents"]))
;; => [:garlic 1 6 2 :fried-rice :potato "succlents"]

;;but we can do below thing via vec

(into [0 "cosmos" "petunia"]
      (map identity [:garlic 1  6 2 :fried-rice :potato "succlents"]))
;; => [0 "cosmos" "petunia" :garlic 1 6 2 :fried-rice :potato "succlents"]

(into #{} (map inc [3 3]))
;; => {4}

(into [] nil)
;; => []

(into {:name "Basit"} {:gender "Male" :interest-in "clojure"})

(into {:name "Danish"} [[:genter "male"] [:interest-in "clojure"]])
;; its not destructuring 

;; assoc take map, then key and value, and returns a new map 
;; assoc can also be used with vector, see assoc.clj

(assoc {:name "Nasir"} :gender "Male" :interest-in "Python")
;; => {:name "Nasir", :gender "Male", :interest-in "Python"}

(into ["cherry"] '("pine" "Apple"))
;; => ["cherry" "pine" "Apple"]

;; ***
;; If into were asked to describe its strengths at a job interview, it would
;; say, “I’m great at taking two collections and adding all the elements from
;; the second to the first.


;;conj

(conj [1 2 ] 3 4 5 )

;; ***
(conj [0] [1])
;; => [0 [1]]

(into [0] [0])
;; => [0 0]


(conj '(1 2) 3)
;; => (3 1 2)

(conj {:name "Basit"} [:gender "Male"] [:aksjdf 3])


;; You’ll often see two functions that do the same thing, except one takes a rest parameter (conj) and
;; one takes a seqable data structure (into).























