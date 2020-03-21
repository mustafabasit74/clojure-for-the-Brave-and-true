;To understand programming to abstractions, let’s compare Clojure to a
;language that wasn’t built with that principle in mind: Emacs Lisp (elisp).
;In elisp, you can use the mapcar function to derive a new list, which is similar
;to how you use map in Clojure. However, if you want to map over a hash map
;(similar to Clojure’s map data structure) in elisp, you’ll need to use the
;maphash function, whereas in Clojure you can still just use map. In other words,
;elisp uses two different, data structure–specific functions to implement the
;map operation, but Clojure uses only one. You can also call reduce on a map in
;Clojure, whereas elisp doesn’t provide a function for reducing a hash map.

;***
;The reason is that Clojure defines map and reduce functions in terms of
;the sequence abstraction, not in terms of specific data structures. As long as
;a data structure responds to the core sequence operations (the functions
;first, rest, and cons, which we’ll look at more closely in a moment), it will
;work with map, reduce, and oodles of other sequence functions for free.
 
;***
;This is what Clojurists mean by programming to abstractions, and it’s a central
;tenet of Clojure philosophy


(defn titleize
  [topic]
  (str topic " for the brave and true"))

(map titleize ["Clojure" "LISP" "Scala"])

(map titleize '("Hamsters" "Ragnarok") )

(map titleize #{"Elbows" "Soap Carving"})



(map #(titleize (second %)) {:uncomfortable-thing "Winking"} )


(map (fn [[key value]]
       (titleize value) ) {:uncomfortable-thing "Winking"} )


