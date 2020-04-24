(ns clojure-for-the-brave-and-true.visualization.svg) 

(defn latlng->point
  [latlng]
  (str (:lat latlng) "," (:lng latlng)))

(defn points
  [locations]
  (clojure.string/join " " (map latlng->point
                                locations)))