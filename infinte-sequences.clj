;; lazy seqs give you is the ability to construct infinite sequences.

;; lazy sequence make things pos­si­ble that would be impos­si­ble with­out them and 
;; solve prob­lems that would oth­er­wise have to solved with state­full variables

(repeat 4)

(take 4 (repeat (rand-int 100)))
(take 4 (repeatedly #(rand-int 100)))

(concat (take  8 (repeat "na")) ["Batman"])

;; A lazy seq’s recipe doesn’t have to specify an endpoint
;; Functions like first and take, which realize the lazy seq, have no way of knowing what will
;; come next in a seq, and if the seq keeps providing elements, well, they’ll
;; just keep taking them. 


;; infinite positive numbers - without lazy sequence
(defn positive-numbers
  ([] (positive-numbers 1))
  
  ([n] (cons n (positive-numbers (+ n 1))) ))

(positive-numbers)
;; => Execution error (StackOverflowError) at user/positive-numbers (REPL:4).
;; => null

(take 4 (positive-numbers))
;; => Execution error (StackOverflowError) at user/positive-numbers (REPL:4).
;; => null

;; infinite sequences via lazy-seq
(defn positive-numbers
  ([] (positive-numbers 1))    
  ([n] (cons n (lazy-seq (positive-numbers (inc n))))) )

;; *** 
(positive-numbers)
(take 4 (positive-numbers))


;; confusion all around ---- ***
;; "cons" does not realize second parameter,
;; opening the world for recursive functions that create lazy sequences


;; just specify recipe, how to calculate new element
;; even numbers
(defn even-numbers
  ([] (even-numbers 0))

  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(take 10 (even-numbers))
;; => (0 2 4 6 8)

;; fib series

;; Lazy sequences are inter­est­ing datas­truc­tures in the sense that they aren’t really datas­truc­tures.
;; Instead they are objects which con­form to the sequence API, but which con­tain a func­tions which
;; return the next item in the sequence. So while call­ing first on a Clo­jure list, returns the item stored
;; in the first cons cell in the list, call­ing the same on a lazy sequence instead calls a func­tion which
;; returns both a value and another func­tion. The value is returned by first.

;; http://theatticlight.net/posts/Lazy-Sequences-in-Clojure/



;; It might be easier to think about the producer function as a function
;; that, given element n, produces element n+1 via a recursive call to 
;; itself, wrapped with lazy-seq to delay its execution

(defn fib 
  ([] (fib 0 1))

  ([a b]
    (cons (+ a b) (lazy-seq (fib b (+ a b))))))


(take 5 (fib))
;; => (1 2 3 5 8)






