(ns hospital.aula4
  (:use [clojure.pprint])
  (:require [hospital.model :as model]
            [hospital.logic :as logic]))

(defn chega-em-com-atom! [hospital pessoa]
  (swap! hospital logic/chega-em :espera pessoa))

(defn simula-um-dia-em-paralelo []
  (let [hospital-atom (atom (model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "111"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "222"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "333"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "444"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "555"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "666"))))
    (.start (Thread. (fn [] (chega-em-com-atom! hospital-atom "666"))))
    (.start (Thread. (fn [] (Thread/sleep 2000)
                       (pprint hospital-atom))))))

(simula-um-dia-em-paralelo)

; -----------------------------------------------------------------------------------------------------

(defn simula-um-dia-em-paralelo-com-mapv []
  (let [hospital-atom (atom (model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]]

    (mapv #(.start (Thread. (fn [] (chega-em-com-atom! hospital-atom %)))) pessoas)

    (.start (Thread. (fn [] (Thread/sleep 2000)
                       (pprint hospital-atom))))))

(simula-um-dia-em-paralelo-com-mapv)

; -----------------------------------------------------------------------------------------------------
; REFATORANDO...

(defn simula-um-dia-em-paralelo-com-mapv []
  (let [hospital-atom (atom (model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]
        starta-thread #(.start (Thread. (fn [] (chega-em-com-atom! hospital-atom %))))]

    (mapv starta-thread pessoas)

    (.start (Thread. (fn [] (Thread/sleep 2000)
                       (pprint hospital-atom))))))

(simula-um-dia-em-paralelo-com-mapv)

; -----------------------------------------------------------------------------------------------------
; REFATORANDO+ ...

(defn starta-thread
  ([hospital]
   (fn [pessoa] (starta-thread hospital pessoa)))
  ([hospital pessoa]
   (.start (Thread. (fn [] (chega-em-com-atom! hospital pessoa))))))

(defn simula-um-dia-em-paralelo-com-mapv-extraida []
  (let [hospital (atom (model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]
        starta (starta-thread hospital)]

    (mapv starta pessoas)

    (.start (Thread. (fn [] (Thread/sleep 2000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo-com-mapv-extraida)

; -----------------------------------------------------------------------------------------------------
; PARTIAL

(defn starta-thread [hospital pessoa]
  (.start (Thread. (fn [] (chega-em-com-atom! hospital pessoa)))))

(defn simula-um-dia-em-paralelo-com-partial []
  (let [hospital (atom (model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555", "666"]
        starta (partial starta-thread hospital)]

    (mapv starta pessoas)

    (.start (Thread. (fn [] (Thread/sleep 2000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo-com-partial)

; -----------------------------------------------------------------------------------------------------
; DOSEQ

(defn simula-um-dia-em-paralelo-com-doseq []
  (let [hospital-sp (atom (model/novo-hospital))
        pessoas ["111", "222", "333", "444", "555"]]

    (doseq [pessoa pessoas]
      (starta-thread hospital-sp pessoa))

    (.start (Thread. (fn [] (Thread/sleep 1000)
                       (pprint hospital-sp))))))

(simula-um-dia-em-paralelo-com-doseq)


(defn simula-um-dia-em-paralelo-com-doseq []
  (let [hospital-sp (atom (model/novo-hospital))
        pessoas (range 6)]

    (doseq [pessoa pessoas]
      (starta-thread hospital-sp pessoa))

    (.start (Thread. (fn [] (Thread/sleep 1000)
                       (pprint hospital-sp))))))

(simula-um-dia-em-paralelo-com-doseq)

; -----------------------------------------------------------------------------------------------------
; DOTIMES

(defn simula-um-dia-em-paralelo-com-dotimes []
  (let [hospital-sp (atom (model/novo-hospital))]

    (dotimes [pessoa 6]
      (starta-thread hospital-sp pessoa))

    (.start (Thread. (fn [] (Thread/sleep 1000)
                       (pprint hospital-sp))))))

(simula-um-dia-em-paralelo-com-dotimes)

; -----------------------------------------------------------------------------------------------------