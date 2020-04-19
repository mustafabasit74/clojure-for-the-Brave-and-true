;; you can replace an expression with the result of that expression 
(* (+ 1 3) 5 )



;; it should have to be pure function in order to be referentially transparent 

;; deleting or reading a file, getting date or random number, web requests  .... are not referentially transparent

;; ***
;; sometimes we dont need to run the function if it is pure, compiler can just replace 
;; whole function with its output(optimization)

;; we can replace pure functions with its output at compile time - refrential transparency

(+ 2 3 ) ;; we dont need to run this line of code, as it will always result in 5, we can optimize the code by replacing
         ;; it with 5, as simple as it can be.
         
(def num (+ accumulator (+ 5 6))) ;; this experssion can be replaced by (def num (+ accumulator 11)) 

;; ***
;; pure function,so always referentially transparent
(defn sum
  [x y]
  (+ x y))

(def result (sum 2 5)) ;; the function call here ...(sum 2 5) can be replaced by 7, as it is a pure function. (def result 7)
                       ;; in this case the expression (def result (sum 2 5)) is equivalent to (def result 7)  


;; ***
;; impure function,so may be referentially opaque
(defn sum 
  [x y]
  (do (println "sum of x and y : " (+ x y))
  (+ x y)))

(def result (sum 2 5)) ;; In this case the function call ...(sum 2 5) can't be replaced with 7, Although it will always return 7
                       ;; but it has a side effect of printing something else
                       ;; here (def result (sum 2 5 ))  != (def result 7)
                       ;; that means we can't optimize a referentially opaque code, we have to run it first (see lazy evaluation also) 


;; ---^^^ youtube/ Eric Normand 

;; ***
;; “Why do they call it: Referentially transparent?”
;; http://www.nobugs.org/blog/archives/2008/11/12/why-do-they-call-it-referentially-transparent/



;; It always returns the same result if given the same arguments. This is
;; called referential transparency

;; When using a referentially transparent function, you never have to
;; consider what possible external conditions could affect the return value of
;; the function. This is especially important if your function is used multiple
;; places or if it’s nested deeply in a chain of function calls. In both cases, you
;; can rest easy knowing that changes to external conditions won’t cause your
;; code to break