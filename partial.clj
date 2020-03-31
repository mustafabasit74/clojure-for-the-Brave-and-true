(def life-saving-kit (partial (fn
                                [first second third name  & rest ]
                                (str "Hi! " name ", your kit includes : " first ", " second ", " third ", "
                                     (clojure.string/join ", " rest))) 
                              "radio" "matchstick" "torch"))


;; different people will put diff. things in  kit according to their needs, and  suppose government is providing three things already with the kit free 
(life-saving-kit "Zakir" "food" "water" "clothes")
(life-saving-kit "Basit" "phone" "medicines" "laptop" "knife")
;; => "Hi! Basit, your kit includes : radio, matchstick, torch, phone, medicines, laptop, knife"

(life-saving-kit "Iqra" "mirror" "nail-polish" "eye-liner" "lip-stick" "kajal" "mascara" "talc" "food" "phone" "powerbank")
;; => "Hi! Iqra , your kit includes : radio, matchstick, torch, mirror, nail-polish, eye-liner, lip-stick, kajal, mascara, talc, food, phone, powerbank"
(life-saving-kit "seerat")
;; => "Hi! seerat, your kit includes : radio, matchstick, torch, "


(def add10 (partial + 10))
(add10 3)
;; => 13

(= (add10 ) (+ 10))
;; => true

(= (add10 3) (+ 10 3))
;; => true


(def add-missing-elements
  (partial conj ["laptop" "phone" "mouse"]))

(add-missing-elements "laptop-charge" "phone-charger" "data-cable" "powerbank" "speaker")



;; ??? litle bit confusion, clear it 
;; In general, you want to use partials when you find you’re repeating the
;; same combination of function and arguments in many different contexts. 