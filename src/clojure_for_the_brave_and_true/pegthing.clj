;; divide the arhitecture in two layer top and bottom layer
;; top layer handles all the side effects and may use functions in bottom layer 
;; bottom layer functions doesn't use top layer functions at all. these are pure 


;; in map, sometimes key also acts as a data -- :connections {6 3, 4 2 }

(ns clojure-for-the-brave-and-true
  (:require [clojure.string :as string])
  (:gen-class))

(declare successful-move promt-move game-over prompt-rows)
;; If you’re curious about what’s going on, the short explanation is that 
;; (require [clojure.set :as set]) allows you to easily use functions in the clojure.set
;; namespace, (:gen-class) allows you to run the program from the command
;; line, and (declare successful-move prompt-move game-over query-rows) allows
;; functions to refer to those names before they’re defined.

;; start --------------------------------------------------------------------------------------------
 
(defn tri*
  ([] (tri* 0 1))

  ([sum n]
   (let [new-sum (+ sum n)]
     (cons new-sum (lazy-seq (tri* new-sum (inc n)))))))

;; tri now contains infinite T numbers
(def tri (tri*))   ;; -- (def tri (1,3,6,10 .........))

;;(take 10 tri)       ;; -- (take 10 (1, 3, 6, 10 .......)) 


(defn triangular?
  [n]
  (= n (last (take-while #(>= n %) tri))))
;; why he has not used above any membership check function, if all the numbers are in tri?
;; because list is infinite, suppose if the elememt is not in the list, it will keep checking it with next, next .... 

(defn row-tri
  "takes row number and returns triangular number at the end of that row, if n = 2 return 3, if n = 3 return 6"
  [n]
  (last (take n tri)))

;; why he has not used nth function above --(nth tri (dec n))
;; may be because - (nth tri (dec 0))
;; => Execution error (IndexOutOfBoundsException) at user/eval207 (REPL:1) .
;; => null 
;; -- find the reason - pending
 
(defn row-num
  "it takes the board position, and returns the row it belongs, eg if pos = 1 return 1, if pos = 4 return 3, if pos = 6 return 3 "
  [pos]
  (inc (count (take-while #(> pos %) tri))))

(defn connect
  "forms a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))

;; we dont have to check whether neighbor is <= max-pos, because if destination is <= maxpos, that means neighbor is also then

;; why we are checking here destination <= max-pos, -  because we dont have to create further connection, for last pos,
;; if pos = 15, connect-down-left/rigth will return its valid neighour, destination values. In such cases return back board - may be this is the reason behind it
;; but why he had not done it this way, pos == max-pos - return back ????

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? pos) (triangular? neighbor))
      (connect board max-pos pos neighbor destination)
      board)))

(defn connect-down-left
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ pos row)
        destination (+ neighbor row 1)]
    (connect board max-pos pos neighbor destination)))

(defn connect-down-right
  [board max-pos pos]
  (let [row (row-num pos)
        neighbor (+ pos row 1)
        destination (+ neighbor row 2)]
    (connect board max-pos pos neighbor destination)))

(defn add-pos
  [board max-pos pos]
  (let [pegged-board (assoc-in board [pos :pegged] true)]
    (reduce (fn [new-board connection-creation-fn]
              (connection-creation-fn new-board max-pos pos))
            pegged-board
            [connect-right connect-down-right connect-down-left])))

;; Reducing over a collection of functions is not a technique you’ll use often, 
;; but it’s occasionally useful, and it demonstrates the versatility of functional programming.

(defn new-board
  [rows]
  (let [initial-board {:rows rows}
        max-pos (row-tri rows)]
    (reduce (fn [board pos]
              (add-pos board max-pos pos))
            initial-board
            (range 1 (inc max-pos)))))

;; -- moving pegs

(defn pegged?
  [board pos]
  (get-in board [pos :pegged]))

(defn remove-peg
  [board pos]
  (assoc-in board [pos :pegged] false))

(defn place-peg
  [board pos]
  (assoc-in board [pos :pegged] true))

(defn move-peg
  "take out peg from p1 and place it in p2"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))

;; all the connections of a positions are not valid move always, because there is a constraint in the form of pegged?
;;we have to find the valid moves among :connections

;; ***
;; why we are not checking below first is pos pegged or not
;; e.g. if pos is pegged false, then what is the fun of checking for its valid moves

(defn valid-moves
  "Return a map of all valid moves for pos"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination)) (pegged? board jumped)))
                (get-in board [pos :connections]))))

;; where he is checking is p1 pegged?
(defn valid-move?
  [board p1 p2]
  (get (valid-moves board p1) p2))


(defn make-move
  "move peg from p1 to p2, removing jumped peg"
  [board p1 p2]
  (if-let [jumped (valid-move? board p1 p2)]
    (move-peg (remove-peg board jumped) p1 p2)))

;; ***
;; both partial and comp returns a function
(defn can-move?
  "Do any of the pegged position have valid moves?"
  [board]
  (some (comp not-empty (partial valid-moves board))
        (map first (filter #(get (second %) :pegged)
                           board))))
;; What’s most interesting about this bit of code is that you’re using a
;; chain of functions to derive a new function, similar to how you use chains
;; of functions to derive new data. In Chapter 3, you learned that Clojure treats
;; functions as data in that functions can receive functions as arguments and
;; return them. Hopefully, this shows why that feature is fun and useful.

;; Rendering and Printing the Board

(def alpha-start 97)
(def alpha-end 123)
(def letters (map (comp str char)
                  (range alpha-start alpha-end)))
(def pos-chars 3)

;; ---------------------------------------------------------
(def ansi-styles
  {:red   "[31m"
   :green "[32m"
   :blue  "[34m"
   :reset "[0m"})

(defn ansi
  "Produce a string which will apply an ansi style"
  [style]
  (str \u001b (style ansi-styles)))

(defn colorize
  "Apply ansi color to text"
  [text color]
  (str (ansi color) text (ansi :reset)))
;; ---------------------------------------------------------


(defn render-pos
  [board pos]
  (str (nth letters (dec pos))
       (if (get-in board [pos :pegged])
         (colorize "O" :blue)
         (colorize "_" :red))))

(defn row-positions
  "returns all the positions in the given row"
  [row-num]
  (range (inc (or (row-tri (dec row-num)) 0))
         (inc (row-tri row-num))))

(defn row-padding
  "String of spaces to add to the beginning of a row to center it"
  [row-num rows]
  (let [pad-length  (/ (* (- rows row-num) pos-chars) 2)]
    (apply str (take pad-length (repeat " ")))))

(defn render-row
  [board row-num]
  (str (row-padding row-num (:rows board))
       (string/join " " (map (partial render-pos board)
                                     (row-positions row-num)))))
(defn print-board
  [board]
  (doseq [row-num (range 1 (inc (:rows board)))]
    (println (render-row board row-num))))

;; You use doseq when you want to perform side-effecting operations (like
;; printing to a terminal) on the elements of a collection. The vector that
;; immediately follows the name doseq describes how to bind all the elements
;; in a collection to a name one at a time so you can operate on them. In this
;; instance, you’re assigning the numbers 1 through 5 (assuming there are
;; five rows) to the name row-num so you can print each row.

;; ***
;; Although printing the board technically falls under interaction, I wanted
;; to show it here with the rendering functions. When I first started writing
;; this game, the print-board function also generated the board’s string representation. 
;; However, now print-board defers all rendering to pure functions
;; which makes the code easier to understand and decreases the surface are a of our impure functions.


;; Player Interaction

(defn letter->pos
  [letter]
  (inc (- (int (first letter)) alpha-start)))

(defn get-input
  "waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input nil))
  ([default]
   (let [input (string/trim (read-line))]
     (if (empty? input)
       default
       (string/lower-case input)))))

(defn characters-as-strings
  "Given a string, return a collection consisting of each individual character"
  [string]
  (re-seq #"[a-zA-Z]" string))

(defn prompt-move
  [board]
  (println "\nHere's your board: ")
  (print-board board)
  (println "Move from Where to Where? Enter two letters: ")
  (let [input (map letter->pos (characters-as-strings (get-input)))]
    (if-let [new-board (make-move board (first input) (second input))]
      (successful-move new-board)
      (do 
        (println "\n!!! That was an invalid move :(\n")
        (prompt-move board)))))

(defn successful-move
  "Here we are recieving new board(after moving pegs), now we have to check is there any possible move left or not"
  [board]
  (if (can-move? board)
    (prompt-move board)
    (game-over board)))

;; In our board creation functions, we saw how recursion was used to
;; build up a value using immutable data structures. The same thing is happening here, only it involves two mutually recursive functions and some
;; user input. No mutable attributes in sight!

(defn game-over
  "Announce game over, and prompt to play again"
  [board]
  (let [remaining-pegs (count (filter :pegged 
                                      (vals board)))]
    (println "Game over! You had" remaining-pegs "pegs left:")
    (print-board board)
    (println "Want to Play again? y/n [y]")
    (let [input (get-input "y")]
      (if (= "y" input)
        (prompt-rows )
        (do
          (println "Bye!")
          (System/exit 0))))))

(defn prompt-empty-peg
  [board]
  (println "Here's your Board: ")
  (print-board board)
  (println "Remove which peg? [e]")
  (prompt-move (remove-peg board (letter->pos (get-input "e")))))

(defn prompt-rows
  []
  (println "How many rows? [5]")
  (let [rows (Integer. (get-input 5))
        board (new-board rows)]
    (prompt-empty-peg board)))

(prompt-rows )

;; (new-board rows) will return a new board, all having :pegged? true.
;; after that we have to remove a peg first, so (prompt-empty-peg board)

;; *** - words of wisdom
;; Even though all of this program’s side effects are relatively harmless
;; (all you’re doing is prompting and printing), sequestering them in their
;; own functions like this is a best practice for functional programming. In
;; general, you will reap more benefits from functional programming if you
;; identify the bits of functionality that are referentially transparent and sideeffect free,
;; and place those bits in their own functions. These functions are
;; not capable of causing bizarre bugs in unrelated parts of your program.
;; They’re easier to test and develop in the REPL because they rely only on the
;; arguments you pass them, not on some complicated hidden state object



;; while playing the game I got this error - fix pending 
;; => Execution error (NullPointerException) at java.util.regex.Matcher/getTextLength (Matcher.java:1770) .
;; => null
;; => class java.lang.NullPointerException