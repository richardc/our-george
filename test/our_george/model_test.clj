(ns our-george.model-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [our-george.model :refer :all]
            [schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest make-game-test
  (testing "make-game"
    (let [game (make-game ["fred" "barney" "ted"])]
      (is map? game)
      (is (= (:turn-order game) ["fred" "barney" "ted"]))
      (is (= (count (get-in game [:players "ted" :hand])) 5)))))

(fact "make-game expects a vector of PlayerName"
  (make-game [0 1 2 3]) => throws)

(fact "make-game"
  (let [players ["fred" "barney" "ted"]
        game (make-game players)]
    (fact "it will be a map"
      game => map?)
    (fact "it will have a deck"
      game => (contains {:deck list})
      game => (contains {:deck (has every? number?)}))
    (fact "it will have a turn-order of the original players"
      game => (contains {:turn-order players}))
    (fact "it will have a map player states"
      game => (contains {:players map?}))
    (fact "ted will be a player"
      (let [ted (get-in game [:players "ted"])
            hand (:hand ted)
            score (:score ted)]
        (fact "with 5 cards"
          hand => (n-of number? 5))
        (fact "with a score of 0"
          score => 0)))))
