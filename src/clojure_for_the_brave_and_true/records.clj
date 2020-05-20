(ns were-records)
(defrecord WereWolf [name title])

(WereWolf. "David" "London Tourist")
;; => {:name "David", :title "London Tourist"}

(new WereWolf "David" "London Tourist")
;; => {:name "David", :title "London Tourist"}

(WereWolf. "David")
;; => Syntax error (IllegalArgumentException) compiling new at (records.clj:10:1).
;;    No matching ctor found for class were_records.WereWolf

;; records are actually Java classes under the covers.
;; When you create a record, the factory functions
;;  ->RecordName and map->RecordName are created automatically.

(->WereWolf "David" "London Tourist")
;; => {:name "David", :title "London Tourist"}

(->WereWolf "David")
;; => Execution error (ArityException) at were-records/eval33670 (form-init217092896733732408.clj:19).
;;    Wrong number of args (1) passed to: were-records/eval32071/->WereWolf--32089

(map->WereWolf {:name "David" :title "London Tourist"})
 
(map->WereWolf {:name "David" })
;; => {:name "David", :title nil}

(map->WereWolf {})
;; => {:name nil, :title nil}


(map->WereWolf {:name "David" :id 100 :age 20 :title "London Tourist"})
;; => {:name "David", :title "London Tourist", :id 100, :age 20}

(ns monster-mash
  (:import [were-records WereWolf]))
;; => Execution error (ClassNotFoundException) at java.net.URLClassLoader/findClass (URLClassLoader.java:471).
;;    were-record.WereWolf

;; were-records V/S were_records

(ns monster-mash
  (:import [were_records WereWolf]))
;; => nil

(WereWolf. "David" "London Tourist")
;; => {:name "David", :title "London Tourist"}

(def jacob (->WereWolf "Jacob" "Lead Shirt Discarder"))
;; => #'were-records/jacob

(.name jacob)
;; => "Jacob"

(:name jacob)
;; => "Jacob"

(get jacob :name)
;; => "Jacob"

(= jacob (->WereWolf "Jacob" "Lead Shirt Discarder"))
;; => true

(= jacob (->WereWolf "David" "London Tourist"))
;; => false

(= jacob {:name "Jacob" :title "Lead Shirt Discarder"})
;; => false
;; because they don't have same type

(type jacob)
;; => were_records.WereWolf

(type {})
;; => clojure.lang.PersistentArrayMap


(println jacob)
;; => #were_records.WereWolf{:name Jacob, :title Lead Shirt Discarder}

(assoc jacob :new-key "value")
;; => {:name "Jacob", :title "Lead Shirt Discarder", :new-key "value"}

(assoc jacob :name "Zakir")
;; => {:name "Zakir", :title "Lead Shirt Discarder"}

(type (assoc jacob :name "Zakir"))
;; => were_records.WereWolf

;; if you dissoc a field, the result’s type will be a plain ol’ Clojure map
(dissoc jacob :name)
;; => {:title "Lead Shirt Discarder"}

(type (dissoc jacob :name))
;; => clojure.lang.PersistentArrayMap

(defprotocol WereCreature
  "Doc String..."
  (full-moon-behavior [x] "Doc String"))
;; => WereCreature


(extend-type java.lang.String
  WereCreature
  (full-moon-behavior [x]
                      "Bye!"))
;; => nil

(full-moon-behavior "")
;; => "Bye!"

(defrecord WereWolf [name title]
  WereCreature
  (full-moon-behavior [x]
                      (str name " will how and murder")))
;; => were_records.WereWolf

(full-moon-behavior (->WereWolf "Lucian"  "CEO of Melodrama"))
;; => "Lucian will how and murder"

(full-moon-behavior (map->WereWolf {:name "Lucian" :title "CEO of Melodrama"}))
;; => "Lucian will how and murder"

(full-moon-behavior (WereWolf. "Lucian" "CEO of Melodrama"))
;; => "Lucian will how and murder"

(full-moon-behavior (map->WereWolf {:first-name "Lucian" :title "CEO of Melodrama"}))
;; => " will how and murder"

