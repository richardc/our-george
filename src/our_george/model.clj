(ns our-george.model
  (:require [schema.core :as s]))

(def Card s/Int)

(def Score s/Int)

(def PlayerState {:hand [Card]
                  :score Score})

(def PlayerName s/Str)

(def Game {:players {PlayerName PlayerState}
           :deck [Card]
           :turn-order [PlayerName]})

(s/defn deal-a-card-to :- Game
  "Consumes a card from the deck, puts it into a players hand"
  [game :- Game
   player :- PlayerName]
  (-> game
      (update-in [:players player :hand] conj (first (:deck game)))
      (update-in [:deck] rest)))

(s/defn deal-a-round :- Game
  "Deals a new card into all players hands"
  [game :- Game]
  (loop [players (:turn-order game)
         game game]
    (if (empty? players) game
        (recur (rest players)
               (deal-a-card-to game (first players))))))

(s/defn make-game :- Game
  "Creates a new game and does initial deal"
  [players :- [PlayerName]]
  (let [game {:players (zipmap players (repeat {:hand '(), :score 0}))
              :deck (shuffle (range 52))
              :turn-order players}]
    (nth (iterate deal-a-round game) 5)))
