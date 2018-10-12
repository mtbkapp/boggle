(ns boggle.core
  (:require [clojure.spec.alpha :as spec]))

(spec/def ::trie (spec/map-of char? ::node))
(spec/def ::node (spec/keys :req [::word? ::trie]))
(spec/def ::word? boolean?)

; TODO: maybe refactor this a bit
(defn add-word
  [index [c & chrs]]
  (if (some? chrs)
    ; not at the end of the word, add to children of current node in index and recur
    (if (contains? index c)
      (update-in index [c ::trie] add-word chrs)
      (assoc index c {::word? false
                      ::trie (add-word {} chrs)}))
    ; last char of word, add node to children of current, no recur
    (if (contains? index c)
      (assoc-in index [c ::word?] true)
      (assoc index c {::word? true
                      ::trie {}}))))

(spec/fdef add-word
           :args (spec/cat :index ::trie :chrs (spec/coll-of char?))
           :ret ::trie)

(defn index-dict
  "Build a trie out of maps from a coll of words."
  [dict]
  (reduce add-word {} dict))

(spec/fdef index-dict
           :args (spec/cat :dict (spec/coll-of string?))
           :ret ::trie)

(defn adj-squares
  [board [x y]]
  (let [size (count board)]
    (for [dx [-1 0 1] 
          dy [-1 0 1]
          :let [nx (+ x dx)
                ny (+ y dy)]
          :when (and (< -1 nx size)
                     (< -1 ny size))]
      [nx ny])))

(defn square?
  [board]
  (let [size (count board)]
    (every? #(= size (count board)) board)))

(spec/def ::square-coord (spec/tuple nat-int? nat-int?))
(spec/def ::board (spec/and (spec/coll-of (spec/and vector? (spec/coll-of char?)))
                            vector?
                            square?))
(spec/fdef adj-squares
           :args (spec/cat :board ::board :coord ::square-coord)
           :ret (spec/coll-of ::square-cord))

(defn get-letter
  [board [x y]]
  (get-in board [y x]))

(spec/fdef get-letter
           :args (spec/cat :board ::board :coord ::square-coord)
           :ret char?)

(defn search
  ([board index square]
   (search board index square []))
  ([board index square letters]
   (let [letter (get-letter board square)]
     (if-let [{::keys [word? trie]} (get index letter)]
       ; can continue traversing
       (let [next-letters (conj letters letter) 
             next-words (into #{} 
                              (mapcat #(search board trie % next-letters))
                              (adj-squares board square))]
         (if word?
           (conj next-words (apply str (conj letters letter)))
           next-words))
       ; traversal stops here
       []))))

(spec/def ::word-set (spec/and set? (spec/coll-of string?)))
(spec/fdef search
           :args (spec/or :3 
                          (spec/cat :board ::board 
                                    :index ::word-index
                                    :square ::square-coord)
                          :4
                          (spec/cat :board ::board 
                                    :index ::trie
                                    :square ::square-coord
                                    :letters (spec/coll-of char?)))
           :ret ::word-set)

(defn find-all
  [board index]
  (let [size (count board)]
    (mapcat #(search board index %)
            (for [x (range size)
                  y (range size)]
              [x y]))))

(spec/fdef find-all
           :args (spec/cat :board ::board :index ::trie)
           :ret ::word-set)

(comment
  (def dictionary
    ["and" "ant" "are" "bike" "bug" "buggy" "colorado" "utah"])
  (clojure.pprint/pprint (index-dict dictionary))

  (adj-squares test-board [0 0])
  (adj-squares test-board [4 4])
  (def ti
    {\a {::word? false
         ::trie {\n {::word? true
                     ::trie {\t {::word? true
                                 ::trie {}
                                 }}}
                 \r {::word? false
                     ::trie {\e {::word? true
                                 ::trie {}}}}}}})

  (= ti (index-dict ["an" "ant" "are"]))

  (def test-board
    [[\z \m \l \y]
     [\t \m \q \e]
     [\n \a \r \t]
     [\p \u \n \n]])

  (def test-board2
    [[\p \p \a \w \v]
     [\o \r \e \r \e]
     [\g \y \e \s \d]
     [\a \e \p \y \c]
     [\n \s \t \r \a]])

  (search test-board ti [1 2])
  (search test-board (index-dict dictionary) [1 2])
  (find-all test-board (index-dict dictionary))
  (require '[clojure.string :as string])

  (def english
    (time (index-dict (string/split-lines (slurp "./corncob_lowercase.txt")))))
  ;about 200 to 300 ms on 2014 macbook pro quad core 8gb ram
  ;list from http://www.mieliestronk.com/wordlist.html ~58k words


  (time (find-all test-board english))
  ; 7 ms first run, after many runs / warmed(ish) up jit/cache etc ~1ms
  )


