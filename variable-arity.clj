(defn codger-communication
    [whippersnapper]
    (str "Get off my lawn, " whippersnapper "!!!") )

(defn codger
  [& whippersnapper]
  (map codger-communication whippersnapper) )



(codger "Basit" "Zahid" "Usmaan" "nasir" )
(codger "seerat" "afshaan" )


(defn favorite-things
  [name & things]
  (println things )
  (str "Hi, " name ", here are my favorite things " 
       (clojure.string/join ", " things ) ) )


(favorite-things "Syed Nasir" "Apple" "orange" "grapes")


