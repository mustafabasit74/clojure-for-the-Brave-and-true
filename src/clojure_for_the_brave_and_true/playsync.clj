;; Clojure’s core.async library allows you to create multiple independent processes within a single program.
;; Waiting is a key aspect of working with core.async processes
(ns clojure-for-the-brave-and-true.playsync
  (:require [clojure.core.async :as a
             :refer [>! <! >!! <!! go chan buffer close! alts!! timeout sliding-buffer dropping-buffer thread]]))

(def echo-chan (chan))
(go (println (<! echo-chan)))
(>!! echo-chan "Ketchup")
;; => true
;;    Ketchup

(>!! echo-chan "Ketchup")
;; blocked... 
;; but why?
;; because there is no new process in thread pool to recieve data from channel  

;; when trying to put a message on channel or take a message off of it, processed wait and do nothing until the put or take succeeds

;; proof - 1
(def c1 (chan))
(def proof (atom 0))
(go
  (println (<! c1))
  (swap! proof inc))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x2d2e5a6 
;;    "clojure.core.async.impl.channels.ManyToManyChannel@2d2e5a6"]

@proof
;; => 0

(>!! c1 "ketchup")
@proof
;; => 1





(def c3 (chan))
(def c4 (chan))
(def proof (atom 0))

(go
  (println (<! c3))
  (swap! proof inc))

(go
  (println (<! c4))
  (swap! proof inc))

@proof
;; => 0


(do
  (>!! c3 "ketchup")
  (>!! c4 "ketchup"))

@proof
;; => 2

(>!! c3 "ketchup")
;;  This blocks because the channel c3 is full

;; buffering
(def capacity 3)
(def warehouse (chan capacity))

(>!! warehouse "shirts")
(>!! warehouse "shirts")
(>!! warehouse "shirts")
;; => true

;;(>!! warehouse "shirts")
;; This blocks because the channel buffer is full

;; sliding and dropping buffers
;; These buffers are particularly interesting because they never block their current thread on a put
(def fifo-channel (chan (sliding-buffer 2)))
(>!! fifo-channel 1)
;; => true

(>!! fifo-channel "two")
;; => true
;; 
(>!! fifo-channel 3)
(>!! fifo-channel 4)
(>!! fifo-channel 5)
;; => true




(def lifo-channel (chan (dropping-buffer 2)))
(>!! lifo-channel nil)
;; => Execution error (IllegalArgumentException) at clojure.core.async.impl.channels.ManyToManyChannel/put_BANG_ (channels.clj:69).
;;    Can't put nil on channel

(>!! lifo-channel "Basit")
;; => true

(>!! lifo-channel :wasit)
;; => true

(>!! lifo-channel 10.3)
(>!! lifo-channel +)
(>!! lifo-channel [])
(>!! lifo-channel true)
;; => true



(def echo-chan (chan))
(thread
  (println (<!! echo-chan))
  (println "Bye!"))

(>!! echo-chan "Wasit")


(let [th-chan (thread (println "Do Something")
                      :doSomethingElse...
                      "Thread Return a channel, and put the return value on returned channel"
                      "No blocking-*"
                      "Long Running process......"
                      "Wasit")]
  (println (<!! th-chan)))
;; => Do Something
;;    Wasit

;; ***
;; The reason you should use thread instead of a go block when you’re performing a long-running task is so you don’t clog your thread pool. Imagine
;; you’re running four processes that download humongous files, save them
;; and then put the file paths on a channel. While the processes are downloading files and saving these files, Clojure can’t park their threads. It can
;; park the thread only at the last step, when the process puts the files’ paths
;; on a channel. Therefore, if your thread pool has only four threads, all four
;; threads will be used for downloading, and no other process will be allowed
;; to run until one of the downloads finishes


;; As we know both take and put block threads untill..
;; In case of buffered channels 

(def d1 (chan))
(>!! d1 "This put will block main thread ")
;; blocked...

(def d2 (chan 5))
(>!! d2 "Does this will block main thread?")
;; ???


;; When you close a channel, you can no longer perform puts on it, and once you’ve taken all values
;;  off a closed channel, any subsequent takes will return nil.
;; example pending





(defn hot-dog-machine
  []
  (let [in (chan)
        out (chan)]
    (go
      (<! in)
      (>! out "hot dog"))
    [in out]))

(let [[in out] (hot-dog-machine)]
  (>!! in "pocket lint")
  (<!! out))
;; => "hot dog"




(defn hot-dog-machine-v2
  [hot-dog-count]
  (let [in (chan)
        out (chan)]
    (go (loop [hc hot-dog-count]
          (if (> hc 0)
            (let [input (<! in)]
              (if (= 3 input)
                (do
                  (>! out "hot dog")
                  (recur (dec hc)))
                (do
                  (>! out "Wilted lettuce")
                  (recur hc))))
            (do
              (close! in)
              (close! out)))))
    [in out]))

(let [[in out] (hot-dog-machine-v2 2)]
  (>!! in "pocket lint")
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (println (<!! out))

  (>!! in 3)
  (println (<!! out)))
;; => Wilted lettuce
;;    hot dog
;;    hot dog
;;    nil

;; ***
;; hot dog machine doesn’t accept more money until you’ve dealt with whatever it’s dispensed.
;; This allows you to model state-machine-like behavior, where the completion of channel operations triggers state transitions. For example, you can
;; think of the vending machine as having two states: ready to receive money
;; and dspensed item. Inserting money and taking the item trigger transitions between the two.
(let [[in out] (hot-dog-machine-v2 4)]
  (>!! in 3)
  (>!! in 3)
  (println (<!! out))
  (println (<!! out)))
;; blocked...




;; pipeline of processes

(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  (go (>! c2 (<! c1)))
  (go (>! c3 (<! c2)))
  (go (println (<! c3)))
  (>!! c1 "Hello World"))
;; => true
;;    Hello World




(def e1 (chan))
;; => #'clojure-for-the-brave-and-true.playsync/e1

(def e2 (chan))
;; => #'clojure-for-the-brave-and-true.playsync/e2


(go
  (>! e2 (<! e1))
  (println "Data on e2 channel is:" (<! e2))
  (println "Bye!"))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x18269c41 "clojure.core.async.impl.channels.ManyToManyChannel@18269c41"]

(go (>! e1 "Tomato"))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x3e3f0c6e "clojure.core.async.impl.channels.ManyToManyChannel@3e3f0c6e"]

;; ***
;; still nothing is printed

;; ***
(<!! e2)
;; => "Tomato"
;; ***
;; but why "data on e2 channel is : ..." & "Bye" not got printed ?????
;; because on this line "(println "Data on e2 channel is:" (<! e2))" we are again taking data from the channel, its waiting for put on e2 channel
;; how to unblock? - do this
(go (>! e2 "unblock"))
;; => Data on e2 channel: unblock
;;    Bye!





;; pipeline of processes: just make the in channel of one process the out channel of another

(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  ;; from c1 to c2
  (go (>! c2 (clojure.string/reverse (<! c1))))

  ;; from c2 to c3
  (go (>! c3 (clojure.string/upper-case (<! c2))))

  ;; take result from c3
  (go (println (<! c3)))

  ;; put data on c1 
  (>!! c1 "redrum"))
;; => true
;;    MURDER


;; alts!!

(defn upload
  [headshot c]
  (go (Thread/sleep (rand 100))
      (>! c headshot)))

(let [c1 (chan)
      c2 (chan)
      c3 (chan)]
  (upload "playful.png" c3)
  (upload "smile.png" c1)
  (upload "fun.png" c2)
  (let [[headshot channel] (alts!! [c1 c2 c3])]
    (println "Sending headshot notification for " headshot)))
;; => Sending headshot notification for  playful.png

;; => Sending headshot notification for  smile.png



(let [c1 (chan)]
  (upload "playful.png" c1)
  (let [[headshot channel] (alts!! [c1 (timeout 10)])]
    (if headshot
      (println "Sending headshot notification for " headshot)
      (println "Timed out"))))
;; => Timed out


(let [c1 (chan)
      c2 (chan)]
  (upload "playful.png" c1)
  (let [[value channel] (alts!! [c1 [c2 "put!"]])]
    (println value)
    (= channel c1)))
;; => playful.png
;;    true


(let [c1 (chan)
      c2 (chan)]
  (upload "playful.png" c1)
  (go (<! c2))
  (let [[value channel] (alts!! [c1 [c2 "put!"]])]
    (println value)
    (= channel c2)))
;; => true
;;    true


;; Rolling your own Queue - 2
(defn append-to-file
  [filename s]
  (spit filename s :append true))

(defn format-quote
  [quote]
  (str "=== BEGIN QUOTE ===\n" quote "=== END QUOTE ===\n\n"))

(defn random-quote
  []
  (format-quote (slurp "https://www.braveclojure.com/random-quote")))

(defn snag-quotes
  [filename num-quotes]
  (let [c (chan)]
    (go (while true (append-to-file filename (<! c))))
    (dotimes [n num-quotes]
      (go (>! c (random-quote))))))
;; Here, each quote-retrieving task is handled in the order that it finishes.

(snag-quotes "quotes" 10)
;; => nil

;; Escape Callback Hell with Process Pipelines

(defn upper-caser
  [in]
  (let [out (chan)]
    (go (while true (>! out (clojure.string/upper-case (<! in)))))
    out))

(defn reverser
  [in]
  (let [out (chan)]
    (go (while true (>! out (clojure.string/reverse (<! in)))))
    out))

(defn printer
  [in]
  (go (while true (println (<! in)))))

(def in-chan (chan))
(def upper-caser-out (upper-caser in-chan))
(def reverser-out (reverser upper-caser-out))
(printer reverser-out)

(>!! in-chan "redrum")
;; => MURDER

(>!! in-chan "repaid")
;; => DIAPER


(let [in-c (chan)
      rev-out (reverser in-c)
      result (<!! rev-out)]
  (>!! in-chan "Hello World")
  (println result))
;; blocked - fix it, come back later


(defn -main
  "I don't do a whole lot ... yet."
  [& args])
