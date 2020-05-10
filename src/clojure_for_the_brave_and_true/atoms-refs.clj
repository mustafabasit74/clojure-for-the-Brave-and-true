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
;; Atoms are ideal for managing the state of independent identities
;; In Clojure metaphysics, state is the value of an identity at a point in time, and identity is a handy way 
;; to refer to a succession of values produced by some process
(def fred (atom {:cuddle-hunger-level 0
                 :percent-deteriorated 0}))
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
  (if (>= (:percent-deteriorated zombie-state) 50)
    (future (println (:percent-deteriorated zombie-state)))))

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

;; Find Answer - pending
;; swap! receives an atom and a function as arguments. 
;; It applies the function to the atom’s current state to produce a new value,
;; ????????? and then it updates the atom to refer to this new value. ?????????????? 

;; how it is possible, what about mutability?


;; Unlike Ruby, it’s not possible for fred to be in an inconsistent state
;; because you can update the hunger level and deterioration percentage at the same time

(swap! fred       
       (fn [current-state]
         (merge-with +
                     current-state 
                     {:cuddle-hunger-level 1
                      :percent-deteriorated 1})))
;; => {:cuddle-hunger-level 2, :precent-deteriorated 1}
;; 
@fred
;; => {:cuddle-hunger-level 2, :precent-deteriorated 1}


;; You can also pass swap! a function that takes multiple arguments.
(defn increase-cuddle-hunger-level
  [current-state increase-by]
  (merge-with + 
              current-state 
              {:cuddle-hunger-level increase-by}))

;; test above function
(increase-cuddle-hunger-level @fred 10)
;; => {:cuddle-hunger-level 12, :precent-deteriorated 1}

@fred
;; => {:cuddle-hunger-level 2, :precent-deteriorated 1}

(swap! fred 
       increase-cuddle-hunger-level  10)
;; => {:cuddle-hunger-level 12, :precent-deteriorated 1}

@fred
;; => {:cuddle-hunger-level 12, :precent-deteriorated 1}

(swap! fred
       increase-cuddle-hunger-level  3)
;; => {:cuddle-hunger-level 15, :precent-deteriorated 1}

@fred
;; => {:cuddle-hunger-level 15, :precent-deteriorated 1}

;; Or you could express the whole thing using Clojure’s built-in functions. (update-in)

(swap! fred
       update-in [:cuddle-hunger-level] + 5)
;; => {:cuddle-hunger-level 20, :precent-deteriorated 1}


;; By using atoms, you can retain past state.
(let [num (atom 1)
      s1 @num]
  (swap! num inc)
  (println "State 1: " s1)
  (println "Current State: " @num))
;; => State 1:  1
;;    Current State:  2

;; if two separate threads call (swap! fred increase-cuddle-hunger-level 1) ?
;; Is it possible for one of the increments to get lost
;; The answer is no! swap! implements compare-and-set semantics
;; One detail to note about swap! is that atom updates happen synchronously

;; Sometimes you’ll want to update an atom without checking its current value.

(reset! fred {:cuddle-hunger-level 0
              :percent-deteriorated 0})
;; => {:cuddle-hunger-level 0, :percent-deteriorated 0}

(def balance (atom {:x 10
                    :y 20}))
(let [x 3
      y 4]
  (future
    (swap! balance
           (fn [current-state]
             (merge-with +
                         current-state
                         {:x x}))))   
  (future
    (swap! balance
           #(merge-with +
                        % 
                        {:y y}))))
;; => #future[{:status :ready, :val {:x 13, :y 24}} 0x76f46331]

(reset! balance {:y 0})
;; => {:y 0}

@balance
;; => {:y 0}

