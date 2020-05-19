;; Polymorphism
;; The main way we achieve abstraction in Clojure is by associating an
;; operation name with more than one algorithm

;; Multimethods give you a direct, flexible way to introduce polymorphism into your code.

;; ***
;; Using multimethods, you associate a name with multiple implementations by defining a dispatching function,
;; which produces dispatching values that are used to determine which method to use.

(ns were-creatures)
(defmulti full-moon-behavior (fn[were-creature]
                                (:were-type were-creature)))
;; => nil


(defmethod full-moon-behavior :wolf
  [were-creature]
  (str (:name were-creature) "will howl and murder"))
;; => #multifn[full-moon-behavior 0x1ad4aa5]

(defmethod full-moon-behavior :simmons
  [were-creature]
  (str (:name were-creature) "will encourage people and sweat to the oldies"))
;; => #multifn[full-moon-behavior 0x1ad4aa5]

(full-moon-behavior {:were-type :wolf
                     :name "Rachel from next door"})
;; => "Rachel from next doorwill howl and murder"

(full-moon-behavior {:name "Andy the baker"
                     :were-type :simmons})
;; => "Andy the bakerwill encourage people and sweat to the oldies"


;; Method definitions look a lot like function definitions, but the major difference is that the method name 
;; is immediately followed by the dispatch value. :wolf and :simmons are both dispatch values

;; This is different from a dispatching value, which is what the dispatching function returns.

(defmethod full-moon-behavior nil 
  [were-creature]  
  (str (:name were-creature) "will stay at home and eat ice cream"))

(defmethod full-moon-behavior :default
  [were-creature]
  (str (:name were-creature) "will stay up all night fantasy footballing"))

(full-moon-behavior {:were-type nil
                     :name "Martin the nurse"})
;; => "Martin the nursewill stay at home and eat ice cream"

(full-moon-behavior {:were-type :office-worker
                     :name "Jimmy from sales"})
;; => "Jimmy from saleswill stay up all night fantasy footballing"


(defmulti types (fn[x y]
                  [(class x) (class y)]))

(defmethod types [java.lang.String java.lang.String]
  [x y]
  "Two Strings")

(defmethod types [clojure.lang.PersistentVector clojure.lang.PersistentVector]
  [x y]
  "Two persistent vectors")

(types "s1" "s3")
;; => "Two Strings"

(types [1 2 3] [4 5])
;; => "Two persistent vectors"




;; One cool thing about multimethods is that you can always add
;; new methods. If you publish a library that includes the were-creatures
;; namespace, other people can continue extending the multimethod to
;; handle new dispatch values.
(ns random-namespace
  (:require [were-creatures]))
(were-creatures/full-moon-behavior {:name "Martin the nurse"
                                    :were-type nil})
;; => "Martin the nursewill stay at home and eat ice cream"

(defmethod were-creatures/full-moon-behavior :billy-murray
  [were-creature]
  (str (:name were-creature) "will be most likeable celebrity"))
;; => #multifn[full-moon-behavior 0x39348978]

(were-creatures/full-moon-behavior {:name "Laura the intern"
                                    :were-type :billy-murray})
;; => "Laura the internwill be most likeable celebrity"


;; Multimethods also allow hierarchical dispatching. Clojure lets you build custom hierarchies