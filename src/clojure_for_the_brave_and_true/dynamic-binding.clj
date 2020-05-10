;; vars are associations between symbols and objects. You create new vars with def

;; Thread local binding
;; Dynamic vars can be useful for creating a global name that should refer to 
;; different values in different contexts.

;; In addition to refs, atoms, and agents, Clojure has a fourth way of “changing” state: thread local var bindings. 

(def name)
;; => #'user/name
name
;; => #object[clojure.lang.Var$Unbound 0x337ecf82 "Unbound: #'user/name"]

(println name)
;; => nil

(ns-interns *ns*)
;; => {name #'user/name}

(bound? #'name)
(bound? #'user/name)
;; => false

;; thread-bound? takes the var itself as an argument, not the value it refers to.

(thread-bound? #'name)
(thread-bound? #'user/name)
;; => false

;; a root is the binding that’s the same across all threads
;; By default, all threads start with the root binding, which is their associated value in the absence of a thread-bound value.
(def ^:dynamic *notification* "test@elf.org")
;; => #'user/*notification*

*notification*
;; => "test@elf.org"

(bound? #'*notification*)
(bound? #'user/*notification*)
;; => true

(thread-bound? #'*notification*)
(thread-bound? #'user/*notification*)
;; => false


(binding [*notification* "something-else.org"]
  *notification*)
;; => "something-else.org"

*notification*
;; => "test@elf.org"

(binding [*notification* "value1"]
  (println *notification*)
  (binding [*notification* "value2"]
    (println *notification*))
  (println *notification*))
;; => value1
;;    value2
;;    value1

;; dynamic binding can change the behaviour of other functions
(def ^:dynamic *salt* 12345)
;; => #'user/salt

(defn get-salt
  []
  *salt*)
(get-salt)
;; => 12345

(let [*salt* 00000]
  (get-salt))
;; => 12345

(binding [*salt* 000000]
  (get-salt))
;; => 0

(get-salt)
;; => 12345




(def ^:dynamic *counter* (atom 1))
@*counter*
;; => 1

(defn get-*counter*
  []
  @*counter*)

(swap! *counter* inc)
(get-*counter*)
;; => 2

;; another way of changing state
(binding [*counter* (atom {:name "Basit"
                           :id 201711})]
  (get-*counter*))
;; => {:name "Basit", :id 201711}

(get-*counter*)
;; => 2

;; set!
(def ^:dynamic *x* nil)

(defn get-*x*
  []
  (if (thread-bound? #'*x*)
    (println "*x* is thread bound")
    (println "*x* is not thread bound"))
  *x*)

(get-*x*)
;; => *x* is not thread bound
;;    nil

(binding [*x* 10]
  (get-*x*))
;; => *x* is thread bound
;;    10

(binding [*x* 10]
  (set! *x* 39)
  (get-*x*))
;; => *x* is thread bound
;;    39


(let [*x* 2000]
  (get-*x*))
;; => *x* is not thread bound
;;  nil


(let [*x* 2000]
  (set! *x* 85)
  (get-*x*))
;; => Syntax error (IllegalArgumentException) compiling fn* at (dynamic-binding.clj:117:1).
;;    Cannot assign to non-mutable: _STAR_x_STAR_

(defn set-*x*
  [val]
  (set! *x* val))


(let [*x* 2000]
  (set-*x* 7)
  (get-*x*))
;; => Execution error (IllegalStateException) at user/set-*x* (form-init4807563604000011587.clj:1).
;;    Can't change/establish root binding of: *x* with set

(binding [*x* 1]
  (set-*x* 200)
  (get-*x*))
;; => *x* is thread bound
;;    200





;; Dynamic var in Action
(def ^:dynamic *notification-address* "seerat@edu.iust.org")

(defn notify 
  [message]
  (str "Sending email....." "\n"
       "TO:" *notification-address* "\n"
       "MESSAGE:" message))
;; => #'user/notify

(notify "I fell")
;; => "Sending email.....\nTO:seerat@edu.iust.org\nMESSAGE:I fell"


;; Dynamic vars are most often used to name a resource that one or more functions target.
;; ???

(binding [*out* (clojure.java.io/writer "print-output")]
  (println "Clojure changes the way you think"))
;; => nil
(slurp "print-output")
;; => "Clojure changes the way you think\n"


(binding [*out* (clojure.java.io/writer "print-hello")]
  (println "hello-world"))
;; => nil

(slurp "print-hello")
;; => "hello-world\n"

(slurp "print-output")
;; => "Clojure changes the way you think\n"

;; ???
;; This is much less burdensome than passing an output destination to every invocation of println.
;; Dynamic vars are a great way to specify a common resource 

;; convey information "in" to a function without having to pass in the information as an argument

;; Dynamic vars are also used for configuration.
;; For example, the builtin var *print-length* allows you to specify how many items in a 
;; collection Clojure should print:
(println ["print" "all" "the" "things!"])
;; => [print all the things!]

(binding [*print-length* 1]
  (println ["print" "just" "one!"]))
;; => [print ...]


;; set! allows you convey information "out" of a function without having to return it as an argument
(def ^:dynamic *answer* nil)

(defn verify-answer
  [user-answer]
  (let [correct-answer "IUST"]
    (if (thread-bound? #'*answer*)
      (set! *answer* correct-answer))
    
    (if (= correct-answer user-answer)
      "You Won!"
      "You Loose")))

(binding [*answer* nil]
  (println (verify-answer "KU")))
;; => You Loose
;;    nil



(binding [*answer* nil]
  (println (verify-answer "KU"))
  (println "Answer was " *answer*))
;; => You Loose
;;    Answer was  IUST

(verify-answer "KU")
;; => You Loose



(def ^:dynamic *troll-thought* nil)

(defn troll-riddle
  [your-answer]
  (let [number "man meat"]
    (if (thread-bound? #'*troll-thought*)
      (set! *troll-thought* number))
    
    (if (= your-answer number)
      "TROLL: you can cross the bridge!"
      "TROLL: Time to eat you, succulent human" )))

(binding [*troll-thought* nil]
  (println (troll-riddle 2))
  (println "SUCCULENT HUMAN: Ooooh! The answer was" *troll-thought* ))
;; => TROLL: Time to eat you, succulent human
;;    SUCCULENT HUMAN: Ooooh! The answer was man meat

*troll-thought*
;; => nil

(let [*troll-thought* false]
  (println (troll-riddle 2))
  (println "SUCCULENT HUMAN: Ooooh! The answer was" *troll-thought*))
;; => TROLL: Time to eat you, succulent human
;;    SUCCULENT HUMAN: Ooooh! The answer was false


;; per Thread binding
;; come back to it later.


;; Altering the Var Root
(def n 10)
n
;; => 10

(alter-var-root #'n (fn[_] "hello world"))
n
;; => "hello world"


;; with-redefs - pending
