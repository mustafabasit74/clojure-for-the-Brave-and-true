;; if-let is a nifty way to say, “If an expression evaluates to a truthy
;; value, then bind that value to a name the same way that I can in a let
;; expression. Otherwise, if I’ve provided an else clause, perform that else
;; clause; if I haven’t provided an else clause, return nil.”

(let [num 4]
  (if-let [res num]
  (str "if-let passed, res : " res)))
;; => "if-let passed, res : 4"

(let [num nil]
  (if-let [res num]
    (str "if-let passed, res : " res)))
;; => nil


(let [num nil]
  (if-let [res num]
    (str "if-let passed, res : " res) 
    "if-let failed"))
;; => "if-let failed"


;; *** 
;(let [num nil]
;  (if-let [res num]
;    (str "if-let passed, res : " res)
;    (str "if-let failed, res : " res)))
;; => Syntax error compiling at (REPL:4:5).
;; => Unable to resolve symbol: res in this context


(defn sum-even-numbers
  [coll]
  (if-let [evens (filter even? coll)]
    (str "sum of evens nums : " (reduce + evens))
    0))

(sum-even-numbers [1 1 1 1])
(sum-even-numbers [1 1 2 1])
(sum-even-numbers [1 4 2 1])

;; if-let requires exactly 2 forms in binding vector 
(if-let [x false y true]
  "hi"
  "hello")
;; => Syntax error macroexpanding clojure.core/if-let at (REPL:1:1).
;; => (y true) - failed: Extra input at: [:bindings] spec: :clojure.core.specs.alpha/binding


;; ***
;; https://clojuredocs.org/clojure.core/if-let#example-542692cdc026201cdc326cfa
;; This macro is nice when you need to calculate something big. And you need 
;; to use the result but only when it's true:

(if-let [life (meaning-of-life 12)]
  life
  (if-let [origin (origin-of-life 1)]
    origin
    (if-let [shooter (who-shot-jr 5)]
      shooter
      42)))
;; As you can see in the above example it will return the answer 
;; to the question only if the answer is not nil. If the answer
;; is nil it will move to the next question. Until finally it
;; gives up and returns 42.


;; fun of if-let 
(defn some-condition
  [data]
  true)

(let [result (some-condition "ABC")]
  (if (true? result)
    "Success"
    "Failure"))
;; => "success"

(if-let [result (some-condition "ABC")]
  "Success"
  "Failure")
;; => "success"


