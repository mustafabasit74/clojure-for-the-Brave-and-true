(contains? #{:a :b}  :a)
(get #{:a :b} :a)
(:a #{:a :b})
(#{:a :b} :a)

(contains? #{:a :b}  :c)
(get #{:a :b} :c)
(:c #{:a :b})
(#{:a :b} :c)

;; using get to test whether a set contains nil will always return nil, which is confusing
(contains?  #{:a nil } nil )
(get #{:a nil } nil )


;; *************************
(contains? {nil "0" } nil )

;; *************************
(get {nil "0" } nil )
