;; collections of functions be like [+ - inc dec], '(first second last)....

(def sum #(reduce + %));; 
(def avg #(/ (sum %) (count %)))

;; stats - short for statistics
(defn stats
  [numbers]
  (map #(% numbers) [sum count avg] ))

(stats [1 2 3])
(stats [10 20 40 59 49 30 201 92 93 39])



