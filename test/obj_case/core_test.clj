(ns obj-case.core-test
  (:require [clojure.test :refer [deftest testing are]]
            [obj-case.core :as core]))

(deftest default-normalize-test
  (testing "hello world"
    (are [x y] (= y (core/default-normalize x))
      "AHELL(!*&#(!)O_WOR   LD.bar" "ahelloworldbar")))

(deftest get-in-test
  (testing "it should find simple keys"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a" "b"} ["a"] "b"
      {"first_name" "Calvin"} ["firstName"] "Calvin"
      {"first name" "Calvin"} ["first_name"] "Calvin"
      {:a "b"} [:a] "b"
      {:first_name "Calvin"} [:firstName] "Calvin"
      {"first name" "Calvin"} [:first_name] "Calvin"))

  (testing "it should find nested keys"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a" {"b" {"c" "d"}}} ["a" "b" "c"] "d"
      {"A bird" {"flew_under" {"theTrain" 4}}} ["a bird" "flew_under" "the_train"] 4
      {:a {:b {:c "d"}}} [:a :b :c] "d"
      {:A-bird {:flew_under {:theTrain 4}}} ["a bird" :flew_under :the_train] 4))

  (testing "it should find falsey keys"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a" {"b" false}} ["a" "b"] false
      {"a" {"b" nil}} ["a" "b"] nil
      {:a {:b false}} [:a :b] false
      {:a {:b nil}} [:a :b] nil))

  (testing "it should find non-uniform cased keys"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"camel_Case" true} ["camel_Case"] true
      {:camel_Case true} [:camel_Case] true))

  (testing "it should find dot-separated paths as object key"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a.b" 10} ["a.b"] 10
      {:a/b 10} [:a/b] 10))

  (testing "it should find dot-separated paths in a nested object"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a" {"b.c" 10}} ["a" "b.c"] 10
      {:a {:b/c 10}} [:a :b/c] 10))

  (testing "it should work on blank objects"
    (are [m keys expected] (= expected (core/get-in m keys))
      {} ["a" "b" "c"] nil
      {} [:a :b :c] nil))

  (testing "it should work with properties with same prefix"
    ;; Nested keys
    (let [m {:website {:left "aaaa"}
             :websites {:left "bbbb"}}]
      (are [keys expected] (= expected (core/get-in m keys))
        [:website :left] "aaaa"
        [:websites :left] "bbbb"
        ["website" "left"] "aaaa"
        ["websites" "left"] "bbbb"))
    ;; Simple key
    (let [m {:website "aaaa"
             :websites "bbbb"}]
      (are [keys expected] (= expected (core/get-in m keys))
        [:website] "aaaa"
        [:websites] "bbbb"
        ["website"] "aaaa"
        ["websites"] "bbbb"))
    ;; Reversed key
    (let [m {:websites "bbbb"
             :website "aaaa"}]
      (are [keys expected] (= expected (core/get-in m keys))
        [:website] "aaaa"
        [:websites] "bbbb"
        ["website"] "aaaa"
        ["websites"] "bbbb"))
    ;; Missing key
    (let [m {:websites "bbbb"}]
      (are [keys expected] (= expected (core/get-in m keys))
        [:website] nil
        [:websites] "bbbb"
        ["website"] nil
        ["websites"] "bbbb")))

  (testing "it should work with a terminal value of nil"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"traits" {"email" nil}} ["traits" "email"] nil)))

(deftest casing-test
  (testing "it should find crazy looking paths"
    (are [m keys expected] (= expected (core/get-in m keys))
      {"a" {"HelloWorld.BAR" 10}} ["a" "HELLO_WORLD.bar"] 10)

    (are [m keys expected] (= expected (core/get-in m keys))
      {"HELLO_WORLD.a.B" 10} ["helloWorld.a.B"] 10)

    (are [m keys expected] (= expected (core/get-in m keys))
      {"woodyallen" 10} ["woodyAllen"] 10)

    (are [m keys expected] (= expected (core/get-in m keys))
      {"some-crazy.PROBABLY_POSSIBLE.NestedProperty" 10} ["SOME_CRAZY.ProbablyPossible.nested_property"] 10)

    (are [m keys expected] (= expected (core/get-in m keys))
      {"some-crazy.PROBABLY_MISSPELLED.NestedProperty" 10} ["SOME_CRAZY.ProbablyMssplld.nested_property"] nil)))
