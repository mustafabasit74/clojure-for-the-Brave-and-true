;; ;; let binds names to values

(let [x 4] 
  x)


(def product-list ["clock" "torch" "powerbank" "keyboard" "speakers"])

(let [products (take 2 product-list)]
  products )


(print-str product-list)
;; (print-str product) - it will throw - Unable to resolve symbol: product in this context



;; ;; let also introduces a new scope

(def x 200) 
(let [x 10]
  x)

;; ;; we can refer existing bindings in our let 

(def x 0)

(let [x (inc x)] 
  x)


;; ;; let + rest param
;; Note: rest param returns list of ...

(def product-list ["clock" "torch" "powerbank" "keyboard" "speakers"])

(let [[must-purchase & remaining-products] product-list]
    [must-purchase product-list] )

  
(let [item  "Power Bank"
      price 200]
     price )


(let [x 10
      _ (println "x : " x)]
      x )