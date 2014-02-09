(ns our-george.model
  (:require [schema.core :as s]))

(def Card s/Int)

(def PlayerName s/Str)

(def PlayerState {:hand [Card]
                  :score s/Int})

(def Game {:players {PlayerName PlayerState}
           :deck [Card]
           :turn-order [s/Str]})

;; This tweaks turn-order.  As long as you call it once per player, as
;; deal-a-round does then this isn't a problem.
(s/defn deal-a-card :- Game
  [game :- Game]
  (let [player (first (:turn-order game))]
    (-> game
        (update-in [:players player :hand] conj (first (:deck game)))
        (update-in [:deck] rest)
        (update-in [:turn-order] #(concat (rest %) [(first %)])))))

(s/defn deal-a-round :- Game
  [game :- Game]
  (nth (iterate deal-a-card game) (count (:turn-order game))))

(s/defn make-game :- Game
  [players :- [PlayerName]]
  (let [game {:players (zipmap players (repeat {:hand '(), :score 0}))
              :deck (shuffle (range 52))
              :turn-order players}]
    (nth (iterate deal-a-round game) 5)))
