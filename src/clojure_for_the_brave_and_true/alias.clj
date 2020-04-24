;; All it does is let you shorten a namespace name for using fully qualified symbols

(in-ns 'edu.iust.clojure.clojure-for-the-brave)
;; => #namespace[edu.iust.clojure.clojure-for-the-brave]

(def item "Speakers")


(in-ns 'offline.workspace)
edu.iust.clojure.clojure-for-the-brave/item

(clojure.core/alias 'clojure 'edu.iust.clojure.clojure-for-the-brave)
;; => nil

clojure/item
;; "Speakers"
;; refer and alias are your two basic tools for referring to objects outside your current namespace!