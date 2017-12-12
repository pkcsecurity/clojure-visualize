(ns visualize.core
  (:require [clojure.java.io :as io]
            [clojure.tools.reader.reader-types :as t]
            [clojure.tools.namespace.parse :as p]
            [cheshire.core :as json])
  (:gen-class))

(def ext "clj")
(def project-path "") ;;EX: "/Users/joe/Desktop/Projects/FooBar/src")
(def output-file "visualize.json")
(def file-map (atom {}))

(defn remove-ext [s]
  (clojure.string/replace s (str "." ext) ""))

(defn rm-lead-sep [s]
  (if (clojure.string/starts-with? s java.io.File/separator)
    (subs s 1)
    s))

(defn sep->per [s]
  (clojure.string/replace (rm-lead-sep s) java.io.File/separator "."))

(defn file->key [f]
  (let [base (rm-lead-sep project-path)]
    (-> f
      str
      rm-lead-sep
      (clojure.string/replace base "")
      rm-lead-sep
      remove-ext
      sep->per
      keyword)))

(defn valid-file [f]
  (and
    (.exists f)
    (not (.isDirectory f))
    (clojure.string/ends-with? f ext)))

(defn get-valid-files [dir]
  (if (and
        (.exists dir)
        (.isDirectory dir))
    (filter valid-file (file-seq dir))))

(defn files->map [f-coll]
  (into {}
    (for [f f-coll]
      [(file->key f)
       {:name (file->key f)
        :location (str f)
        :size 0}])))

(defn get-file-ns [f]
  (with-open [rdr (io/reader f)]
    (-> rdr
      t/push-back-reader
      p/read-ns-decl
      p/name-from-ns-decl
      )))

(defn get-file-deps [f]
  (with-open [rdr (io/reader f)]
    (-> rdr
      t/push-back-reader
      p/read-ns-decl
      p/deps-from-ns-decl
      )))

(defn get-all-deps [mp]
  (into []
    (apply clojure.set/union
      (for [f mp]
        (:imports (last f))))))

(defn add-external-deps []
  (loop [deps (get-all-deps @file-map)]
    (let [key (keyword (first deps))]
      (when (seq deps)
        (if (not (key @file-map))
          (swap! file-map assoc key {:name (name key) :location "External" :size 0 :imports []}))
        (recur (rest deps))))))

(defn export-to-json [coll]
  (let [out-file (io/file (str project-path java.io.File/separator output-file))]
    (spit out-file (json/generate-string coll))))

(defn -main []
  (let [base-dir (clojure.java.io/file project-path)
        files (get-valid-files base-dir)]
    (reset! file-map (files->map files))
    (loop [files files]
      (when (seq files)
        (let [f (first files)
              key (keyword (get-file-ns f))
              deps (get-file-deps f)]
          (swap! file-map assoc key (assoc (@file-map key) :imports deps)))
        (recur (rest files))))
    (add-external-deps)
    (export-to-json (vals @file-map))))