;; Clojure’s core.async library allows you to create multiple independent processes within a single program.
;; Waiting is a key aspect of working with core.async processes
(ns clojure-for-the-brave-and-true.playsync
  (:require [clojure.core.async :as a
             :refer [>! <! >!! <!! go chan buffer close! alts!! sliding-buffer dropping-buffer thread]]))

(def echo-chan (chan))
(go (println (<! echo-chan)))
(>!! echo-chan "Ketchup")
;; => true
;;    Ketchup

(>!! echo-chan "Ketchup")
;; blocked... 
;; but why?

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

(def d1 (chan) )
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
              (if (= 3 input )
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


;; pipeline of processes

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


(defn -main
  "I don't do a whole lot ... yet."
  [& args])
