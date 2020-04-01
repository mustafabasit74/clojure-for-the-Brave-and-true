;; Returns a memoized version of a referentially transparent function. The
;; memoized version of the function keeps a cache of the mapping from arguments
;; to results and, when calls with the same arguments are repeated often, has
;; higher performance at the expense of higher memory use.

(defn pure-function
  [x]
  (Thread/sleep 3000)
  (inc x))

(pure-function 3)
;; => 4       --after 3secs

(def memoized-pure-function (memoize pure-function))
(memoized-pure-function 3)
;; => 4       --after 3secs

(memoized-pure-function 3)
;; => 4       --within no timne

(memoized-pure-function 3)  
;; => 4        --within no time

(memoized-pure-function 10)
;; => 11        --after 3 secs


;; The first time we call the function with a particular argument the
;; original function is invoked and the value is returned.  The next
;; time the function is called with the same argument the cached result
;; is returned and the original function is NOT called

;; It's possible to memoize a function with multiple arguments:


;; implement fib this way -- pending

