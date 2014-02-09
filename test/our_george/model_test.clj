(ns our-george.model-test
  (:require [clojure.test :refer :all]
            [our-george.model :refer :all]
            [schema.test]))

(use-fixtures :once schema.test/validate-schemas)

(deftest make-game-test
  (testing "make-game"
    (let [game (make-game ["fred" "barney" "ted"])]
      (is map? game)
      (is (= (:turn-order game) ["fred" "barney" "ted"]))
      (is (= (count (get-in game [:players "ted" :hand])) 5)))))
