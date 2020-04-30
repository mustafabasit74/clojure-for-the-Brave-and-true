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

;; This gensym’d symbol is distinct from any symbols within
;; stuff-to-do, so you avoid variable capture. Because this is such a common
;; pattern, you can use an auto-gensy. Auto-gensyms are more concise and convenient ways to use gensyms:

`(message#)
;; => (message__17648__auto__)

`(let [name# "Basit"]
   name#)
;; => (clojure.core/let [name__18127__auto__ "Basit"] name__18127__auto__)

;; In this example, you create an auto-gensym by appending a hash
;; mark (or hashtag, if you must insist) to a symbol within a syntax-quoted
;; list. Clojure automatically ensures that each instance of x# resolves to the
;; same symbol within the same syntax-quoted list, that each instance of y#
;; resolves similarly, and so on.
;; gensym and auto-gensym are both used all the time when writing macros
;; and they allow you to avoid variable capture.


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

;; using gensym
(defmacro simple
  []
  (def x (gensym))
  `(let [~x 10]
     (println "X: " ~x)))

(simple)
;; => X:  10
;;    nil

;; ***
;; using auto-gensym
(defmacro simple
  []
  `(let [x# 10]
     (println "X: " x#)))

(simple)
;; => X:  10
;;    nil


;; Double Evaluation
;; Another gotcha to watch out for when writing macros is double evaluation
;; which occurs when a form passed to a macro as an argument gets evaluated
;; more than once
(boolean :key)
;; => true

(boolean (println "Hi!"))
;; => false

(boolean (Thread/sleep 3000))
;; => false

(if (or (println "Hello!") (Thread/sleep 4000) (println "Bye!") true)  
  "Thread")
;; => Hello!
;;    Bye!
;;    "Thread"


;; ***
(defmacro report
  [to-try]
  `(if ~to-try
     (println (quote ~to-try) "was successful:" ~to-try)
     (println (quote ~to-try) "was not successful" ~to-try )))

(report (do (Thread/sleep 1000) (+ 1 1 )))
;; *** 
;; This code is meant to ***test its argument for truthiness. If the argument
;; is truthy, it’s considered successful; if it’s falsey, it’s unsuccessful. The macro
;; prints whether or not its argument was successful.
;; In this case, you would actually sleep for two seconds because (Thread/sleep 1000) gets 
;; evaluated twice:

;; Here’s how you could avoid this problem:
(defmacro report
  [to-try]
  `(let [result# ~to-try]
     (if result#
       (println (quote ~to-try) "was successful:" result#)
       (println (quote ~to-try) "was not successful" result#))))

(report (do (Thread/sleep 1000) (+ 1 1)))

;; By placing to-try in a let expression, you only evaluate that code once
;; and bind the result to an auto-gensym’d symbol, result#, which you can now
;; reference without reevaluating the to-try code.


;; Macros All the Way Down
;; One subtle pitfall of using macros is that you can end up having to write
;; more and more of them to get anything done. This is a consequence of the
;; fact that macro expansion happens before evaluation

(doseq [x [1 2 3 4 5]]
  (println x))
;; => 1
;;    2
;;    3
;;    4
;;    5
;;    nil

(doseq [x [1 2 3 ]
        y [10 20]]
  (println x y))
;; => 1 10
;;    1 20
;;    2 10
;;    2 20
;;    3 10
;;    3 20
;;    nil

(report (= 1 1))
;; => (= 1 1) was successful: true
;;    nil

(report (= 1 2))
;; => (= 1 2) was not successful false
;;    nil

;; ***
(doseq [code ['(= 1 1) '(= 1 2 ) '(false) '(nil)]]
        (report code))
;; => code was successful: (= 1 1)
;;    code was successful: (= 1 2)
;;    code was successful: (false)
;;    code was successful: (nil)
;;    nil

;; The report macro works fine when we pass it functions individually, but
;; when we use doseq to iterate report over multiple functions, it’s a worthless failure
;; report receives the unevaluated symbol code in each iteration; however, we want it to receive
;;  whatever code is bound to at evaluation time. 

;;  ***
;;  But report, operating at macro expansion time, just can’t access those values.
;; To resolve this situation, we might write another macro, like this:


; (defmacro doseq-macro
; [macroname  args]
; `(do (~macroname  ~args)))

;(doseq [code ['(= 1 1) '(= 1 2) '(false) '(nil)]]
;  (doseq-macro report code))


(defmacro doseq-macro 
  [macroname & args]
  `(do 
     ~@(map (fn [arg]
              (list macroname arg) )
          args )))

(doseq-macro report (= 1 1) (= 1 2) false true)
;; => (= 1 1) was successful: true
;;    (= 1 2) was not successful false
;;    false was not successful false
;;    true was successful: true
;;    nil

(def user "khadim Ali")

(defmacro foo
  []
  (def text "strange")
  `(println user))

(foo)
;; => Khadim Ali

text
;; => "strange"


