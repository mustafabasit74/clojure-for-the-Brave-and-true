;; If coll is empty, returns nil, else coll
(not-empty '(1 2 3))
(not-empty [1 2 3 ])
;; => [1 2 3]

(not-empty ())
;; => nil

(not-empty nil)
;; => nil

(not-empty true)
;; => Execution error (IllegalArgumentException) at user/eval15750 (form-init2716525182042037312.clj:11).
;;    Don't know how to create ISeq from: java.lang.Boolean


(not-empty "hi")
;; => "hi"
(not-empty "")
;; => nil

;; ***
(if ()
  1)

(filter even? [3 3 3])
;; => ()

(if (filter even? [3 3 3])
  "even"
  "all odds")
;; => "even"

(if (not-empty (filter even? [3 3 3]))
  "even"
  "all odds")
;; => "all odds"

(some even? [3 3 3])
;; => nil

(if-let [valid (filter even? [3 3 3])]
  "valid"
  "invalid")
;; => "valid"

(if-let [valid (not-empty(filter even? [3 3 3]))]
  "valid"
  "invalid")
;; => "invalid"










