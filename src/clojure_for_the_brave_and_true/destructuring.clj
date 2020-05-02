(defn my-first
  [[first-thing]]
  (str "First element of vector is  : "  first-thing ) )
  
(my-first ["pendrive" "mouse" "keyboard" "speaker"] )

;; Make my life easier by taking apart the argument’s structure for me and
;; associating meaningful names with different parts of the argument!

;; destructuring  + rest param

(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "your first choice is : * " first-choice) )
  (println (str "your second choice is : * " second-choice) )
  (println (str "we are ignoring the rest of your choices."
                "Here they are in case you need to cry over them: "
                 (clojure.string/join ", " unimportant-choices) ) ) )
                                        
(chooser ["phone" "powerbank" "laptop" "speaker" "pendrive" "harddisk" ])

;; you can also destructure maps. In the same way that you tell Clojure
;; to destructure a vector or list by providing a vector as a parameter, you
;; destructure maps by providing a map as a parameter:

(defn announce-treasure-location
  [{lat :lat lng :lng}]
  (println (str "Treasure lat : ") lat)
  (println (str "Treasure lng : ") lng))

(announce-treasure-location {:lat 201.3902 :lng 137.912 } )



;; We often want to just break keywords out of a map, so there’s a shorter
;; syntax for that
;; **********************************
;; still confusion, clear

(defn announce-treasure-location
  [{:keys [lat lng]}]
  (println (str "Treasure lat : ") lat)
  (println (str "Treasure lng : ") lng))

(announce-treasure-location {:lat 201.3902 :lng 137.912 } )

;; In general, you can think of destructuring as instructing Clojure on
;; how to associate names with values in a list, map, set, or vector. Now, on to
;; the part of the function that actually does something: the function body!



;; Associative Destructuring

(def details {:name "Basit"
              :interest ["clojure" "Scala" "Elixir"]
              :qualification "MCA"})

(let [{name :name
       interest :interest
       qualification :qualification} details]
  
  (println "Name:" name)
  (println "Interest:" interest)
  (println "Qualification:" qualification))

;; => Name: Basit
;;    Interest: [clojure Scala Elixir]
;;    Qualification: MCA
