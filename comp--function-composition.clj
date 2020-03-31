;; Combining functions like this—so that the return value of one function is passed
;; as an argument to another—is called function composition. 


(inc (+ 1 2))
((comp inc +) 1 2)

;; Using math notation, you’d say that, in general, using comp on the
;; functions f1, f2, ... fn, creates a new function g such that g(x1, x2, ... xn) equals
;; f1( f2( fn(x1, x2, ... xn))).One detail to note here is that the first function
;; applied—* in the code shown here—can take any number of arguments,
;; whereas the remaining functions must be able to take only one argument.


;; dexterity - skill in performing tasks, especially with the hands.
(def character
  {:name "Mustafa Basit"
  :attributes {:intelligence 9
               :strength 8
               :dexterity 9}})

(def c-int (comp :intelligence :attributes))
(def c-str (comp :strength :attributes))
(def c-dex (comp :dexterity :attributes))

(c-int character)
;; => 10
(:intelligence (:attributes character))
;; => 10


;; divide intelligence value by 2 and increment

((comp inc int #(/ % 2) c-int) character)

;; comp is more elegant because it uses less code to convey more meaning.
;; When you see comp, you immediately know that the resulting function’s
;; purpose is to combine existing functions in a well-known way

(c-str character)
;; => 8
(c-dex character)
;; => 9

(def x 3)
(def y 1)
(inc (* (+ x y) 2))
;; => 9
((comp inc #(* % 2) +) x y) 
;; => 9


(defn two-comp
  [f1 f2]
  (fn [& args]
      (f1 (apply f2 args))) )

((two-comp inc +) 2 4)
;; => 7

((comp inc + ) 2 4)
;; => 7

;; -----------
(defn my-comp
  [& funs]
  (fn [& args]
    (loop [remaining-functions (butlast funs)
           res (apply (last funs)  args)]
          (if (empty? remaining-functions)
               res
               (recur (butlast remaining-functions) ((last remaining-functions) res))))))


((my-comp - identity int dec inc +)  2 3 1)
;;; => 6
((comp - identity int dec inc +)  2 3 1)
;; => -6


;; via reduce/without loop - pending

(defn my-comp
  [& funs]
  (fn [& args]
   ..
