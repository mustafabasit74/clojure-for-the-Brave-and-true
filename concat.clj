;; (concat)(concat x)(concat x y)(concat x y & zs)
;; Returns a lazy seq representing the concatenation of the elements in the supplied colls.

(concat [1 2] 3)
;; => Error printing return value (IllegalArgumentException) at clojure.lang.RT/seqFrom (RT.java:557).
;; => Don't know how to create ISeq from: java.lang.Long

;; concat is not like conj

(concat [1 2 3] [4 5])
;; => (1 2 3 4 5)

(concat [1 2 3] '(4 5))
(concat '(1 2 3) '(4 5))

;; ***
(concat [1] [2] #{3 4 5 6 7 8 9 10})
;; => (1 2 7 4 6 3 9 5 10 8)

(concat [1 2 3] nil)
(concat nil [1 2 3] nil)
(concat nil nil)


