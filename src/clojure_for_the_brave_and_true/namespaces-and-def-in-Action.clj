;; Your Project as a Library
;; It’s useful to imagine a similar setup in Clojure. I think of Clojure as
;; storing objects (like data structures and functions) in a vast set of numbered
;; shelves. No human being could know offhand which shelf an object is stored
;; in. Instead, we give Clojure an identifier that it uses to retrieve the object.For this to be successful, Clojure must maintain the associations
;; between our identifiers and shelf addresses. It does this by using namespaces.
;; Namespaces contain maps between human-friendly symbols and references
;; to shelf addresses, known as vars, much like a card catalog.
;; Technically, namespaces are objects of type clojure.lang.Namespace, and
;; you can interact with them just like you can with Clojure data structures.
;; For example, you can refer to the current namespace with *ns*, and you can
;; get its name with (ns-name *ns*)


;; Each namespace is known by a symbolic name and maps "symbols to vars"

(ns-name *ns*)
;; => user

*ns*
;; => #namespace[user]

;; The "in-ns" function creates a namespace and makes is the current namespace
(in-ns 'foo)
;; => #namespace[foo]


;;***
;; When you start the REPL, for example, you’re in the user namespace
;; (as you can see here) . The prompt shows the current namespace using
;; something like user=>.

;;when you write (map inc [1 2]), both map and inc are symbols. 
;;Symbols are data types within Clojure. For now, all you need to know is that when
;; you give Clojure a symbol like map, it finds the corresponding var in the
;; current namespace, gets a shelf address, and retrieves an object from that
;; shelf for you—in this case, the function that map refers to. If you want to
;; just use the symbol itself, and not the thing it refers to, you have to quote
;; it. Quoting any Clojure form tells Clojure not to evaluate it but to treat it as data.

inc
;; => #function[clojure.core/inc]

'inc
;; => inc


(map inc [1 2 3])
;; => (2 3 4)

'(map inc [1 2 3])
;; => (map inc [1 2 3])

(quote (map inc [1 2 3]))
;; => (map inc [1 2 3])

(eval (quote (map inc [1 2 3])))
;; => (2 3 4)
 
(eval 'inc)
;; => #function[clojure.core/inc]

;; ***
;; Storing Objects with def

(def great-books ["Clojure-for-the-brave-and-true" "Scala-in-Action" "The-joy-of-clojure"])
;; => #'user/great-books

great-books
;; => ["Clojure-for-the-brave-and-true" "Scala-in-Action" "The-joy-of-clojure"]

;; This code tells Clojure:
;; 1. Update the current namespace’s map with the association between great-books and the var.
;; 2. Find a free storage shelf.
;; 3. Store ["Clojure-for-the-brave-and-true" "Scala-in-Action" "The-joy-of-clojure"] on the shelf
;; 4. Write the address of the shelf on the var.
;; 5. Return the var (in this case, #'user/great-books) .

;; This process is called interning a var.

;; namespace "map"({k v, k v }) of symbols-to-interned-vars
(ns-interns *ns*)
;; => {great-books #'user/great-books}

(def a 1)
;; => #'user/a

(def b 2)
;; => #'user/b

(ns-interns *ns*)
;; => {a #'user/a, b #'user/b, great-books #'user/great-books}

;; if namespace is like a MAP, then we can use all the map functions on it like, assoc, get, seq ... - hope so

(assoc (ns-interns *ns*) :key1 "value1", "key2" 2)
;; => {a #'user/a, b #'user/b, great-books #'user/great-books, :key1 "value1", "key2" 2}


(class (ns-interns *ns*))
;; => clojure.lang.PersistentArrayMap
 
(get (ns-interns *ns*) 'great-books)
;; => #'user/great-books

(ns-map *ns*)
;; => {primitives-classnames #'clojure.core/primitives-classnames,
;;     +' #'clojure.core/+',
;;     Enum java.lang.Enum,
;;     .
;;     .
;;     .
;;     .
;;     denominator #'clojure.core/denominator,
;;     bytes #'clojure.core/bytes,
;;     refer-clojure #'clojure.core/refer-clojure}


;; you can use #'______ to grab hold(value) of the var corresponding to the symbol that follows;

(deref #'great-books)
;; => ["Clojure-for-the-brave-and-true" "Scala-in-Action" "The-joy-of-clojure"]

;; This is like telling Clojure, “Get the shelf number from the var, go to
;; that shelf number, grab what’s on it, and give it to me!”
;; But normally, you would just use the symbol:

great-books
;; => ["Clojure-for-the-brave-and-true" "Scala-in-Action" "The-joy-of-clojure"]

;;This is like telling Clojure, “Retrieve the var associated with great-books and deref that

(def msg "Hello World")
;; => #'user/msg

(deref #'user/msg)
;; => "Hello World"

(deref (def text "hello world"))
;; => "hello world"


;; ***
;; ***
(def future-programmer-names ["Wasit Shafi" "Mustafa Basit"])
future-programmer-names
;; => ["Wasit Shafi" "Mustafa Basit"]

;; So far so good, right? Well, brace yourself, because this idyllic paradise
;; of organization is about to be turned upside down! Call def again with the
;; same symbol:

(def future-programmer-names ["shin-chan" "doraemon" "kiteretsu"])
future-programmer-names
;; => ["shin-chan" "doraemon" "kiteretsu"]
;; The var has been updated with the address of the new vector. r. It’s like

;; you used white-out on the address on a card in the card catalog and then
;; wrote a new address. The result is that you can no longer ask Clojure to find
;; the first vector. This is referred to as a "name collision".

;; It’s a problem because you can unintentionally overwrite your own code, and you also 
;; have no guarantee that a third-party library won’t overwrite your code.
;; Melvil recoils in horror! Fortunately, Clojure allows you to create as many namespaces as you
;; like so you can avoid these collisions.

;; Creating and Switching to Namespaces
;; create-ns takes a symbol, creates a namespace with that name if it
;; doesn’t exist already, and returns the namespace
(create-ns 'iust.clojure)
;; => #namespace[iust.clojure]

(ns-name *ns*)
;; => user

(ns-name (create-ns 'iust.java))
;; => iust.java

(ns-name *ns*)
;; => user

;; In practice, you’ll probably never use create-ns in your code, because
;; it’s not very useful to create a namespace and not move into it. Using in-ns
;; is more common because it creates the namespace if it doesn’t exist and
;; switches to it

(in-ns 'iust.scala)
;; => #namespace[iust.scala]

(def scala-book "Scala-in-Action")
;; => #'iust.scala/scala-book

(in-ns 'iust.advancedJava) 
;; => #namespace[iust.advancedJava]

scala-book
;; => Unable to resolve symbol: scala-book in this context
;; => class clojure.lang.Compiler$CompilerException

iust.scala/scala-book
;; => "Scala-in-Action"


;; see - "refer-and-alias.clj"

;;Takeaway
;; namespaces organize maps between symbols and vars, and that vars are references to
;; Clojure objects (data structures, functions, and so on) . def stores an object
;; and updates the current namespace with a map between a symbol and a var
;; that points to the object.
;; Clojure lets you create namespaces with create-ns, but often it’s more
;; useful to use in-ns, which switches to the namespace as well

;; require and use ensure that a namespace exists and is ready to be used
;; and optionally let you refer and alias the corresponding namespaces. You
;; should use ns to call require and use in your source files.

;;  https://gist.github.com/ghoseb/287710/ is a great reference for all the vagaries of using ns.

(def width (+ 3 4))
(deref #'user/width)
;; => 7
(ns-interns *ns*)
('width (ns-interns *ns*))
(println (deref ('width (ns-interns *ns*))))
(println (deref ('width (ns-map *ns*))))

(let [width (+ 1000 9)]
  (println (ns-interns *ns*))
  (println "width:" (deref ('width (ns-interns *ns*))))
  (println "width:" (deref ('width (ns-map *ns*))))
;; where loacal binding's symbol and var is - ???
;; local binding shadows variable - but how???
;; what happens in namespace???   
  (println "width:" width))