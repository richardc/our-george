(ns our-george.model-test
  (:require [clojure.test :refer :all]
            [our-george.model :refer :all]
            [schema.test])
  (:use [slingshot.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest make-game-test
  ;; this first test is only really testing schema works, so we'll only do it once
  (testing "make-game fails when given integers"
    (is (thrown-with-msg? clojure.lang.ExceptionInfo #"does not match schema"
                          (make-game [0 1 2 3]))))
  (testing "make-game"
    (let [players ["fred" "barney" "ted"]
          game (make-game players)]
      (is (= players (:turn-order game)))
      (testing "ted will have 5 cards"
        (is (= 5 (count (get-in game [:players "ted" :hand])))))
      (testing "ted will have a score of 0"
        (is (= 0 (get-in game [:players "ted" :score]))))
      (testing "fred will be the storyteller"
        (is (= "fred" (get-in game [:storyteller])))))))

(deftest tell-a-story-test
  (let [players ["fred" "barney" "ted"]
        game (make-game players)]
    (testing "only the storyteller should be able to tell-a-story"
      (is (thrown+? #(= :our-george.model/illegal-move (:type %))
                    (tell-a-story game "barney" 5 "Legendary"))))
    (testing "fred can tell a story"
      (let [hand (get-in game [:players "fred" :hand])
            card (first hand)
            game (tell-a-story game "fred" card "Yabba Dabba")
            hand (get-in game [:players "fred" :hand])]
        (is (= "Yabba Dabba" (:story game)))
        (is (= card (:story_card game)))
        (is (= 4 (count hand)))))
    (testing "must use a card from their hand"
      (let [teds_card (first (get-in game [:players "ted" :hand]))]
        (is (thrown+? #(= :our-george.model/illegal-move (:type %))
                      (tell-a-story game "fred" teds_card "Suits")))))))
