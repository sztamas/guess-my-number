(ns guess-my-number.core
  (:gen-class))

(def small (atom nil))
(def big (atom nil))

(def commands #{"smaller" "bigger" "start-over" "quit"})

(defn guess-my-number 
  []
  (quot (+ @small @big) 2))

(defn smaller
  []
  (reset! big (dec (guess-my-number)))
  (guess-my-number))

(defn bigger
  []
  (reset! small (inc (guess-my-number)))
  (guess-my-number))

(defn start-over
  []
  (reset! small 1)
  (reset! big 100)
  (guess-my-number))


(defn -main
  [& args]
  (println 
"
Welcome to Guess the Number!
============================
(An adaptation of the first game from Land of Lisp to Clojure)

Think of a number between 1 and 100 and the computer will try to guess it.
The computer will display a guess, after which you can:
  a. Exclaim: `OMG, she guessed it! How is that possible?`
  b. Type `smaller` if your number is smaller than the computer's guess
  b. Type `bigger` if your number is bigger than the computer's guess

To start a new game, think of another number and type `start-over`.

Type `quit` to quit at any time.
")
  (loop [line "start-over"]
      (when (not= "quit" line)
        (if (not (commands line))
          (println "Invalid command! Possible commands are:" (apply str (interpose ", " commands)))
          (do 
            (if (= line "start-over")
              (println "\nSo, you have a number to guess? Let's see..."))
            (let [f (ns-resolve 'guess-my-number.core (symbol line))]
              (println "My guess is:" (f)))
          ))
        (recur (read-line)))
      
  )
)
