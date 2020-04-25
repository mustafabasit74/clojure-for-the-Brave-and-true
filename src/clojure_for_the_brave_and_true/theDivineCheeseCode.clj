(ns clojure-for-the-brave-and-true.theDivineCheeseCode
  (:require [clojure.java.browse :as browse]
             [clojure-for-the-brave-and-true.visualization.svg :refer [xml]]))

;; The takeaway here is that require and use load files and optionally alias or refer their namespaces

;; heist - robbery. A diamond heist

(def heist [{:location "Delhi, India"
             :cheese-name  "Archbishop Hildebold's Cheese Pretzel"
             :lat 50.95
             :lng 6.97}
            {:location "Rome, Italy"
             :cheese-name   "The Standard Emmental"
             :lat 47.37
             :lng 8.55}
            {:location "Paris, France"
             :cheese-name   "Le Fromage de Cosquer"
             :lat 43.30
             :lng 5.37}
            {:location "Berlin, Germany"
             :cheese-name   "The Lesser Emmental"
             :lat 47.37
             :lng 8.55}
            {:location "Beiging, China"
             :cheese-name  "The Cheese of Turin"
             :lat 41.90
             :lng 12.45}])
          
(defn url
  [filename]
  (str "file:///"
       (System/getProperty "user.dir")
       "/"
       filename))

(defn template
  [contents]
  (str  "<style>polyline { fill:none; stroke:#5881d8; stroke-width:3}</style>"
       contents))

(defn -main
  [& args]
  (let [filename "theDivineCheeseCodeSvgMap.html"]
    (->> heist 
         (xml 400 200)
         template
         (spit filename))
    (browse/browse-url (url filename))))
