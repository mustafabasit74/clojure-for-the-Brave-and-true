;; When two  threads make uncoordinated changes to the reference cell, the result is unpredictable
;; Rich Hickey designed Clojure to specifically address the problems that 
;; develop from shared access to mutable state.

;; Object-Oriented Metaphysics
;;  cuddle zombie as an object
;;  The tricky part is that the cuddle zombie is always changing. Its body slowly deteriorates and its 
;;  hunger for cuddles grows with time.
;; In OO terms, we would say that the cuddle zombie is an object with mutable state and that its state is ever fluctuating
;; You can see that this object is just a fancy reference cell,  For example, if two threads try to increment Fred’s hunger level 
;; with something like fred.cuddle_hunger_level = fred.cuddle_hunger_level + 1, one of the increments could be lost
;; Also, in OOP, objects do things. They act on each other, changing state as the program runs. A Person
;; object pushes on a Door object and enters a House object

;; Clojure Metaphysics
;; In Clojure metaphysics, we would say that we never encounter the same cuddle zombie twice. 
;; The cuddle zombie is not a discrete thing, it’s actually a succession of values.

;;***
;; Values are atomic in the sense that they form a single irreducible unit or component in a larger system; 
;; they’re indivisible, unchanging, stable entities.
;; So a value doesn’t change, but you can apply a process to a value to produce a new value

;; there’s no such thing as mutable state. Instead, state means the value of an identity at a point in time
;; Change only occurs when 
;; a) A process generates a new value  
;; b) we choose to associate the identity with the new value.

;; Atoms
(def fred (atom {:cuddle-hunger-level 0
                 :precent-deteriorated 0}))
;; => #'user/fred

(ns-interns *ns*)
;; => {fred #'user/fred}

fred
;; => #atom[{:cuddle-hunger-level 0, :precent-deteriorated 0} 0x2a366783]

(deref #'user/fred)
;; => #atom[{:cuddle-hunger-level 0, :precent-deteriorated 0} 0x2a366783]

;; need more study - pending
(deref (deref #'user/fred))
;; => {:cuddle-hunger-level 0, :precent-deteriorated 0}

(def variable "some value")
;; => #'user/variable

variable
;; => "some value"

(deref #'user/variable)
;; => "some value"

(deref variable)
;; => Execution error (ClassCastException) at user/eval78581 (form-init10142761936973517319.clj:56).
;;    class java.lang.String cannot be cast to class java.util.concurrent.Future (java.lang.String and java.util.concurrent.Future are in module java.base of loader 'bootstrap')

@#'user/variable
;; => "some value"






;; To get an atom’s current state, you dereference it
(deref fred)
;; => {:cuddle-hunger-level 0, :precent-deteriorated 0}

@fred
;; => {:cuddle-hunger-level 0, :precent-deteriorated 0}

(atom [1 2 3 4])
;; => #atom[[1 2 3 4] 0x39696318]

(atom (def abc 3))
;; => #atom[#'user/abc 0x1f220a9c]

(atom ((def abc 2) (def cde 3)))
;; => Execution error (ClassCastException) at user/eval77469 (form-init10142761936973517319.clj:63).
;;    class java.lang.Long cannot be cast to class clojure.lang.IFn (java.lang.Long is in module java.base of loader 'bootstrap'; clojure.lang.IFn is in unnamed module of loader 'app')

(let [zombie-state @fred]  
  (if (>= (:precent-deteriorated zombie-state) 50)
    (future (println (:precent-deteriorated zombie-state)))))

;; To update the atom so that it refers to a new state, you use swap!. This might seem contradictory, 
;; because I said that atomic values are unchanging. Indeed, they are! 
;; But now we’re working with the atom reference type, a construct that refers to atomic values.
;; The atomic values don’t change, but the reference type can be updated and assigned a new value.

;; swap! receives an atom and a function as arguments. It applies the function to the atom’s current
;; state to produce a new value, and then it updates the atom to refer to this new value. 
;; The new value is also returned.
 
;; swap does not return a new atom
@fred
;; => {:cuddle-hunger-level 0, :precent-deteriorated 0}

(swap! fred
       (fn [current-state]
         (merge-with +
                     current-state
                     {:cuddle-hunger-level 1})))
;; => {:cuddle-hunger-level 1, :precent-deteriorated 0}

@fred
;; => {:cuddle-hunger-level 1, :precent-deteriorated 0}



