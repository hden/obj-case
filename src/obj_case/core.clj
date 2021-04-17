(ns obj-case.core
  (:refer-clojure :exclude [get get-in])
  (:require [clojure.string :as string]))

(defn default-normalize
  "AHELL(!*&#(!)O_WOR   LD.bar => ahelloworldbar"
  [x]
  (-> (str x)
    (string/replace #"[^a-zA-Z0-9]+" "")
    (string/lower-case)))

(defn get
  "Returns the value mapped to key, not-found or nil if key not present."
  ([x key]
   (get x key nil))
  ([x key not-found]
   (get x key not-found {}))
  ([x key not-found {:keys [normalize]
                     :or {normalize default-normalize}}]
   (when x
     (if (map? x)
       (let [key (normalize key)
             k (->> (keys x)
                 (filter #(= key (normalize %)))
                 (first))]
         (clojure.core/get x k not-found))
       (clojure.core/get x key not-found)))))

(defn get-in
  "Returns the value in a nested associative structure,
  where ks is a sequence of keys. Returns nil if the key
  is not present, or the not-found value if supplied."
  ([m ks]
   (reduce get m ks))
  ([m ks not-found]
   (get-in m ks not-found {}))
  ([m ks not-found options]
   (loop [sentinel (Object.)
          m m
          ks (seq ks)]
     (if ks
       (let [m (get m (first ks) sentinel options)]
         (if (identical? sentinel m)
           not-found
           (recur sentinel m (next ks))))
       m))))
