(ns guess-my-number.core
  [:use [clojure.string :only [trim]]]
  (:gen-class))

(def small (atom nil))
(def big (atom nil))


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


;; From here down we have UI functions
;; The original game had only the functions above allowing to play from the
;; REPL only

(declare print-welcome-message process-command)

(def commands #{"smaller" "bigger" "start-over" "quit"})

(defn -main
  [& args]
  (print-welcome-message)
  (process-command "start-over") 

  (doseq [line (->> (line-seq (java.io.BufferedReader. *in*))
                    (map trim)
                    (take-while (partial not= "quit")))]
    (if (commands line)
      (process-command line)
      (println "Invalid command! Possible commands are:" (apply str (interpose ", " commands))))
  )

)


(defn call-and-print
  [f]
  (println (format "My guess is: %d" (f))))


;; one ui function for each user command

(defn ui-smaller [] (call-and-print smaller))

(defn ui-bigger [] (call-and-print bigger))

(defn ui-start-over
   []
   (println "\nSo, you have a number to guess? Let's see ...")
   (call-and-print start-over))

(defn process-command
  "Calls the functions corresponding to the user command `ui-smaller` for `smaller` etc."
  [command]
  (let [f (ns-resolve 'guess-my-number.core (symbol (str "ui-" command)))]
    (f)))


(defn print-welcome-message
  []
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
"))
