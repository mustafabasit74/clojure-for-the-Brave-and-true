;; vars are associations between symbols and objects. You create new vars with def

;; Dynamic vars can be useful for creating a global name that should refer to 
;; different values in different contexts.

;; In addition to refs, atoms, and agents, Clojure has a fourth way of “changing” state: thread local var bindings. 

(def ^:dynamic *notification* "test@elf.org")
;; => #'user/*notification*

*notification*
;; => "test@elf.org"

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

(defn get-counter
  []
  @*counter*)

(swap! *counter* inc)
(get-counter)
;; => 2

(binding [*counter* (atom {:name "Basit"
                           :id 201711})]
  (get-counter))
;; => {:name "Basit", :id 201711}




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


;; Dynamic vars are also used for configuration.
;; For example, the builtin var *print-length* allows you to specify how many items in a 
;; collection Clojure should print:
(println ["print" "all" "the" "things!"])
;; => [print all the things!]

(binding [*print-length* 1]
  (println ["print" "just" "one!"]))
;; => [print ...]

