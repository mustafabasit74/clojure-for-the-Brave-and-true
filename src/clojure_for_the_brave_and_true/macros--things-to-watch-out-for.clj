;; see - macros in action

;; gotchas - a sudden unforeseen problem
;; Macros have a couple of sneaky gotchas that you should be aware of. 
;; some macro pitfalls and how to avoid them.


;; concat 
;; concat basically converts two lists into one

;; list 1
(let [x 10])

;; list 2
(println "x:" x)

(concat (list 'let ['x 10])
        (list (list 'println "x:" 'x)
              (list 'println "Bye!")))
;; => (let [x 10] (println "x:" x) (println "Bye!"))


(eval (concat (list 'let ['x 10])
              (list (list 'println "x:" 'x)
                    (list 'println "Bye!"))))
;; => x: 10
;;    Bye!
;;    nil


;; ***
;; Variable Capture
;; Shadow Effect

(def num 100)

(defmacro show 
  [form]
  form)

(show (println "num: " num))
;; => num:  100
;;    nil

;; let's introduce loacal binding

(defmacro show
  [form]
  (list 'let ['num 0]
        form))

(show (println "num: " num))
;; => num:  0
;;    nil

;; example 2

(def message "Good Job!")
(defmacro with-mischief
  [& stuff-to-do]
  (concat 
   (list 'let ['message "Oh, big deal"])
   stuff-to-do))

(with-mischief  (println "Here's how i feel about that thing you did: " message))
;; => Here's how i feel about that thing you did:  Oh, big deal
;;    nil
;; ***
;; Notice that this macro didn’t use syntax quoting. Doing so would result in an exception:

(defmacro with-mischief
  [& stuff-to-do]
  `(let [message "Oh, big deal!"]
     ~@stuff-to-do))

(with-mischief  (println "Here's how i feel about that thing you did: " message))
;; => Syntax error macroexpanding clojure.core/let at (macros--things-to-watch-out-for.clj:77:1).
;;    user/message - failed: simple-symbol? at: [:bindings :form :local-symbol] spec: :clojure.core.specs.alpha/local-name
;;    user/message - failed: vector? at: [:bindings :form :seq-destructure] spec: :clojure.core.specs.alpha/seq-binding-form
;;    user/message - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-bindings
;;    user/message - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-special-binding

;; ***
;; This exception is for your own good: "syntax quoting" is designed to prevent you from accidentally capturing variables within macros

(defmacro foo
  []
  `(let [x 10]
     (println "X: " x)))
;; => #'user/foo

(foo )
;; => Syntax error macroexpanding clojure.core/let at (macros--things-to-watch-out-for.clj:92:1).
;;    user/x - failed: simple-symbol? at: [:bindings :form :local-symbol] spec: :clojure.core.specs.alpha/local-name
;;    user/x - failed: vector? at: [:bindings :form :seq-destructure] spec: :clojure.core.specs.alpha/seq-binding-form
;;    user/x - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-bindings
;;    user/x - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-special-binding

;; ***
;; how to introduce local binding then, if we are using "Syntax Quoting"?
;; If you want to introduce let bindings in your macro, you can use a gensym. The gensym
;; function produces unique symbols on each successive call:

(gensym)
;; => G__29050
;; => G__28980

;; pass a symbol prefix:
(gensym 'id-)
;; => id-29369
;; => id-29360

(gensym 'message)
;; => message29508
;; => message29499

(gensym (str (rand-int 100) "-"))
;; => 69-29862
;; => 29-29853


(defmacro without-mischief
  [& stuff-to-do]
  (let [macro-message (gensym)]
    `(let [~macro-message "Oh, big deal!"]
       ~@stuff-to-do
       (println "I shall need to say: " ~macro-message))))

(without-mischief
 (println "Here's how I feel about that thing you did: " message))
;; => Here's how I feel about that thing you did:  Good Job!
;;    I shall need to say:  Oh, big deal!
;;   nil


(defmacro simple
  []
  `(let [x 10]
     (println "X: " x)))

(simple )
;; => Syntax error macroexpanding clojure.core/let at (macros--things-to-watch-out-for.clj:141:1).
;;    user/x - failed: simple-symbol? at: [:bindings :form :local-symbol] spec: :clojure.core.specs.alpha/local-name
;;    user/x - failed: vector? at: [:bindings :form :seq-destructure] spec: :clojure.core.specs.alpha/seq-binding-form
;;    user/x - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-bindings
;;    user/x - failed: map? at: [:bindings :form :map-destructure] spec: :clojure.core.specs.alpha/map-special-binding

(defmacro simple
  []
  (def x (gensym))
  `(let [~x 10]
     (println "X: " ~x)))

(simple)
;; => X:  10
;;    nil

