;; A Vampire Data Analysis Program for the FWP
;; last Exercise of Chapter 4

(def filename "suspects.csv")
(def map-keys [:id :name :glitter-index])
(defn str->int
  [str]  
  (Integer. str))

(def conversions {:id str->int
                  :name identity
                  :glitter-index str->int})
                  
(defn convert
  [key val]
  ((get conversions key) val))

(defn parse
  [string]
  (map #(clojure.string/split % #",")
        (clojure.string/split string #"\n")))


(defn mapify
  [rows]
  (map (fn [unmapped-row]
        (reduce (fn [new-map [vamp-key value]]
                  (assoc new-map vamp-key (convert vamp-key value)))
                  {}
                  (map vector map-keys unmapped-row)))
        rows))

;(first (mapify (parse (slurp filename))))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))


(glitter-filter 3 (mapify (parse (slurp filename))))