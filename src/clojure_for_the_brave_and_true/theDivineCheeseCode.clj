(ns clojure-for-the-brave-and-true.theDivineCheeseCode
  (:require [clojure-for-the-brave-and-true.visualization.svg :refer :all]))
;;  (:require [clojure-for-the-brave-and-true.visualization.svg :as svg :refer [points]]))

;; (require 'clojure-for-the-brave-and-true.visualization.svg)
;; (refer 'clojure-for-the-brave-and-true.visualization.svg )
;; (alias 'svg 'clojure-for-the-brave-and-true.visualization.svg)
;; instead of using first - require, refer and then alias. we can do it in a better way

;; (use '[clojure-for-the-brave-and-true.visualization.svg :as svg])

;; The takeaway here is that require and use load files and optionally alias or refer their namespaces

;; heist - robbery. A diamond heist

(def heist[{:location "Delhi, India"
            :cheese-name  "Archbishop Hildebold's Cheese Pretzel"
            :lat 50.32
            :lng 6.20}
           {:location "Rome, Italy"
            :cheese-name   "The Standard Emmental"
            :lat 20.31
            :lng 5.27}
           {:location "Paris, France"
            :cheese-name   "Le Fromage de Cosquer"
            :lat 10.39
            :lng 30.24}
           {:location "Berlin, Germany"
            :cheese-name   "The Lesser Emmental"
            :lat 26.12
            :lng 70.70}
           {:location "Beiging, China"
            :cheese-name  "The Cheese of Turin"
            :lat 72.32
            :lng 11.21}
           {:location "Washington DC, USA"
            :cheese-name  "Le Fromage de Cosquer"
            :lat 84.25
            :lng 1.01}])

(defn -main
  [& args]
  (println (points heist)))