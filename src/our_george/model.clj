(ns our-george.model
  (:require [schema.core :as s])
  (:use [slingshot.slingshot :only [throw+ try+]]))

(def Card s/Int)

(def Score s/Int)

(def PlayerState {:hand [Card]
                  :score Score})

(def PlayerName s/Str)

(def Story s/Str)

(def Game {:players {PlayerName PlayerState}
           :deck [Card]
           :turn-order [PlayerName]
           :storyteller PlayerName
           (s/optional-key :story) Story
           (s/optional-key :story_card) Card})

(s/defn tell-a-story :- Game
  "The storyteller tells a story"
  [game :- Game
   player :- PlayerName
   story_card :- Card
   story :- Story]
  (if-not (= player (:storyteller game))
    (throw+ {:type ::illegal-move} (str "it is not " player "'s turn")))
  (-> game
      (assoc-in [:story] story)
      (assoc-in [:story_card] story_card)))

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
  (let [players (:turn-order game)]
    (reduce deal-a-card-to game players)))

(s/defn make-game :- Game
  "Creates a new game and does initial deal"
  [players :- [PlayerName]]
  (let [game {:players (zipmap players (repeat {:hand '(), :score 0}))
              :deck (shuffle (range 52))
              :turn-order players
              :storyteller (first players)}]
    (nth (iterate deal-a-round game) 5)))
