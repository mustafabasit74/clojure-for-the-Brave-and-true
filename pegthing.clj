;; divide the arhitecture in two layer top and bottom layer
;; top layer handles all the side effects and may use functions in bottom layer 
;; bottom layer functions doesn't use top layer functions at all. these are pure 


;; half done, rest pending  -  switched to clojure in action , will be back 

;; (time (last (take   1000000 even )))

;; (time (nth even 1000000))

;; in map, sometimes key also acts as a data -- :connections {6 3, 4 2 }

;; know more regarding triangular numbers - pending 

;; this function genrates infinte  triangle numbers
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

;; we dont have to check whether neighbor is <= max-pos, because if destination is <= maxpos, that means neighbor is also then
(defn connect
  "forms a mutual connection between two positions"
  [board max-pos pos neighbor destination]
  ;; why we are checking here destination <= max-pos, -  because we dont have to create further connection, for last pos,
  ;; if pos = 15, connect-down-left/rigth will return its valid neighour, destination values. In such cases return back board - may be this is the reason behind it
  ;; but why he had not done it this way, pos == max-pos - return back ????
  (if (<= destination max-pos)
    (reduce (fn [new-board [p1 p2]]
              (assoc-in new-board [p1 :connections p2] neighbor))
            board
            [[pos destination] [destination pos]])
    board))
;;(connect {} 15 1 2 4)

(defn connect-right
  [board max-pos pos]
  (let [neighbor (inc pos)
        destination (inc neighbor)]
    (if-not (or (triangular? pos) (triangular? neighbor))
      (connect board max-pos pos neighbor destination)
      board)))
;; why not to confirm is pos < max-pos. may be pos is validated below

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

;; (add-pos {} 15 1)



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
  (assoc-in [pos :pegged] false))

(defn place-peg
  [board pos]
  (assoc-in [pos :pegged] true))

(defn move-peg
  "take out peg from p1 and place it in p2"
  [board p1 p2]
  (place-peg (remove-peg board p1) p2))


(defn valid-moves
  "Return a map of all valid moves for pos"
  [board pos]
  (into {}
        (filter (fn [[destination jumped]]
                  (and (not (pegged? board destination)) (:pegged? board jumped)))
                (get-in board [pos :connections]))))

(defn valid-move?
  [board p1 p2]
  (get (valid-move? board p1) p2))
