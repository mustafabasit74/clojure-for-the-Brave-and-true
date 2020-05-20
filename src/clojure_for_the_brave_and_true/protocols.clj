;; protocols are optimized for type dispatch

;; *** 
;; A multimethod is just one polymorphic operation, whereas a protocol is
;; a collection of one or more polymorphic operation (can't use multi-arity multimethods)

(ns data-psychology)
(defprotocol Psychodynamics
  "Doc string..."
  (thoughts [x] "Doc string...")
  (feelings-about [x] [x y] "Doc string..."))

(extend-type java.lang.String
  Psychodynamics
  (thoughts [x] (str x " thinks, 'Truly, the character defines the data type")))
;; => nil

(thoughts "Zakir")
;; => "Zakir thinks, 'Truly, the character defines the data type"

(feelings-about "Seerat")
;; => Execution error (IllegalArgumentException) at data-psychology/eval26738$fn$G (form-init217092896733732408.clj:1).
;;    No implementation of method: :feelings-about of protocol: #'data-psychology/Psychodynamics found for class: java.lang.String

(extend-type java.lang.String
  Psychodynamics
  (thoughts [x] (str x " thinks, 'Truly, the character defines the data type"))
  (feelings-about 
    ([x] (str x " is longing for a simpler way of life"))
    ([x y] (str x " is envious of " y "'s simpler way of life"))))
;; => nil

(thoughts "Zakir")
;; => "Zakir thinks, 'Truly, the character defines the data type"

(feelings-about "Seerat")
;; => "Seerat is longing for a simpler way of life"

(feelings-about "Basit" 2)
;; => "Basit is envious of 2's simpler way of life"



;; provide a default implementation
(extend-type java.lang.Object
  Psychodynamics
  (thoughts [x] "object.....  - Default Implementation")
  (feelings-about
    ([x] "meh... - Default Implementation")
    ([x y] "meh about... - Default Implemenatation")))

(thoughts 1)
;; => "object.....  - Default Implementation"

(thoughts "message")
;; => "message thinks, 'Truly, the character defines the data type"

(thoughts true)
(thoughts 10.3)
(thoughts clojure.lang.PersistentList)
;; => "object.....  - Default Implementation"


(thoughts nil)
;; => Execution error (IllegalArgumentException) at data-psychology/eval26738$fn$G (form-init217092896733732408.clj:1).
;;    No implementation of method: :thoughts of protocol: #'data-psychology/Psychodynamics found for class: nil

(feelings-about 1)
;; => "meh... - Default Implementation"

(feelings-about 0 9 )
;; => "meh about... - Default Implemenatation"

(feelings-about [1 2 3] "Seerat")
;; => "meh about... - Default Implemenatation"

(feelings-about "Seerat" 7)
;; => "Seerat is envious of 7's simpler way of life"




(extend-protocol Psychodynamics
  
  java.lang.String
  (thoughts [x] (str x " thinks, 'Truly, the character defines the data type"))
  (feelings-about 
    ([x] (str x " is longing for a simpler way of life"))
    ([x y] (str x " is envious of " y "'s simpler way of life")))
  
  java.lang.Object  
  (thoughts [x] "object.....  - Default Implementation")
  (feelings-about
    ([x] "meh... - Default Implementation")
    ([x y] "meh about... - Default Implemenatation")))

(thoughts 1)
;; => "object.....  - Default Implementation"

(thoughts "Danish")
;; => "Danish thinks, 'Truly, the character defines the data type"

(feelings-about 1)
;; => "meh... - Default Implementation"

(feelings-about "Seerat")
;; => "Seerat is longing for a simpler way of life"

(feelings-about "Basit" 2)
;; => "Basit is envious of 2's simpler way of life"

(feelings-about 2 "Basit")
;; => "meh about... - Default Implemenatation"



;; ***
;; It’s important to note that a protocol’s methods “belong” to the
;; namespace that they’re defined in
(data-psychology/feelings-about "Seerat")
;; => "Seerat is longing for a simpler way of life"

;; One consequence of this fact is that, if you want two different protocols to include methods with the same name, you’ll need to put
;; the protocols in different namespaces.
(defprotocol foo 
  "Doc String..."
  (thoughts [x] "foo"))
;; => Warning: protocol #'data-psychology/foo is overwriting method thoughts of protocol Psychodynamics
;;    nil

(extend-type  java.lang.String
  foo
  (foo [x] "foo"))

(foo 1)
;; => nil

(foo "Hi")
;; => nil

;; why nil?


(thoughts "Danish")
;; => Execution error (IllegalArgumentException) at data-psychology/eval31236$fn$G (form-init217092896733732408.clj:1).
;;    No implementation of method: :thoughts of protocol: #'data-psychology/foo found for class: java.lang.String

(thoughts 1)
;; => Execution error (IllegalArgumentException) at data-psychology/eval31236$fn$G (form-init217092896733732408.clj:1).
;;    No implementation of method: :thoughts of protocol: #'data-psychology/foo found for class: java.lang.Long
