(ns-name *ns*)
;; => user

(in-ns 'iust.clojure)
;; => #namespace[iust.clojure]

(def clojure-books ["clojure-for-the-brave" "clojure-in-action"])
(def clojure-projects ["peg-thing" "WFPD" "mapify"])
(def clojure-conferences ["clojure-conj" "Devoxx" "lambda days" "code motion"])

;; :only
(in-ns 'offline.workspace-1)
;; => #namespace[offline.workspace-1]

(clojure.core/refer 'iust.clojure :only ['clojure-books 'clojure-conferences] )
;; => nil

clojure-books
;; => ["clojure-for-the-brave" "clojure-in-action"]

clojure-conferences
;; => ["clojure-conj" "Devoxx" "lambda days" "code motion"]

clojure-projects
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:8151).
;; => Unable to resolve symbol: clojure-projects in this context
;; => class clojure.lang.Compiler$CompilerException



;; :exclude
(in-ns 'offline.workspace-2)
;; => #namespace[offline.workspace-2]

(clojure.core/refer 'iust.clojure :exclude ['clojure-projects])

clojure-books
;; => ["clojure-for-the-brave" "clojure-in-action"]

clojure-conferences
;; => ["clojure-conj" "Devoxx" "lambda days" "code motion"]

clojure-projects
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:8151).
;; => Unable to resolve symbol: clojure-projects in this context
;; => class clojure.lang.Compiler$CompilerException


;; :rename
(in-ns 'offline.workspace-3)
;; => #namespace[offline.workspace-3]

(clojure.core/refer 'iust.clojure :rename {'clojure-projects 'clojure-for-the-brave})
;; => nil

clojure-projects
;; => Syntax error compiling at (form-init10309273857393640813.clj:1:8151).
;; => Unable to resolve symbol: clojure-projects in this context
;; => class clojure.lang.Compiler$CompilerException

clojure-for-the-brave
["peg-thing" "WFPD" "mapify"]


;; mix - can we use :exculde with rename - pending 
(in-ns 'offline.workspace-special)
;; => #namespace[offline.workspace-special]
