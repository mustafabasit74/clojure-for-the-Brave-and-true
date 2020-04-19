(defn foo 
  "Return foo msg"
  [name]
  (str "OH. MY. GOD! " name  " YOU ARE MOST "
       "DIFINITLY LIKE THE BEST" ) )  

(foo "Basit" )


;; get doc string
(doc foo)
(doc map)
(doc hash-map)
(doc + )
(doc *)


;; function arity	
(defn no-params 
  "0 arity function"
  [] 
  "I take no params" ) 



(defn one-param
 "1 arity function" 
 [x]
 (str "I take one parameter: " x) )

(one-param "hello" )



(defn two-param
 "2 arity function" 
 [x y]
 (str "I take two parameters: " x " " y) )

(two-param "hello" "world" )


