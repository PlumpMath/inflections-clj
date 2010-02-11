(ns inflections.rules
  (:refer-clojure :exclude (replace))
  (:use [clojure.contrib.str-utils2 :only (replace)]
        clojure.contrib.seq-utils
        inflections.helper))

(defstruct rule :pattern :replacement)

(defn add-rule! [rules rule]
  (if-not (includes? (deref rules) rule)
    (swap! rules conj rule)))


;; (defn match-rules [rules word]
;;   (first (remove nil? (map #(apply-rule % word) rules))))

(defn match-rules [rules word]
  (for [{:keys [pattern replacement]} rules
        :let [result (replace word pattern replacement)]
        :when (not (= word result))]
    result))

(defn make-rule [pattern replacement]
  (struct rule pattern replacement))

(defn map-rules
  "Returns a seq of rules, where the pattern and replacement must be
  given in pairs of two elements."
  [& patterns-and-replacements]  
  (assert-even-args patterns-and-replacements)
  (map #(apply make-rule %) (partition 2 patterns-and-replacements)))

(defn match-rule [rule word]  
  (let [inflection (replace word (:pattern rule) (:replacement rule))]
    (if-not (= inflection word)
      inflection)))

(defn reset-rules!
  "Resets the list of plural rules."
  [rules] (reset! rules []))


