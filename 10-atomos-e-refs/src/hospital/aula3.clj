(ns hospital.aula3
  (:use [clojure.pprint])
  (:require [hospital.model :as model]
            [hospital.logic :as logic]))

(defn testa-atomo []
  (let [hospital-silveira (atom {:espera model/fila-vazia})]
    (pprint (deref hospital-silveira))                      ; ou @hospital-silveira

    (swap! hospital-silveira assoc :laboratorio1 model/fila-vazia)
    (swap! hospital-silveira assoc :laboratorio2 model/fila-vazia)
    (pprint @hospital-silveira)

    ;(update @hospital-silveira :laboratorio1 conj "111")
    (swap! hospital-silveira update :laboratorio1 conj "111")
    (pprint @hospital-silveira)))

(testa-atomo)

; -----------------------------------------------------------------------------------------------------

(defn chega-em-mal! [hospital pessoa]
  ;(def hospital (logic/chega-em-pausado hospital :espera pessoa))
  (swap! hospital logic/chega-em :espera pessoa)
  (println "após inserir" pessoa))

(defn simula-um-dia-em-paralelo []
  (let [hospital (atom (model/novo-hospital))]
    (.start (Thread. (fn [] (chega-em-mal! hospital "111"))))
    (.start (Thread. (fn [] (chega-em-mal! hospital "222"))))
    (.start (Thread. (fn [] (chega-em-mal! hospital "333"))))
    (.start (Thread. (fn [] (chega-em-mal! hospital "444"))))
    (.start (Thread. (fn [] (chega-em-mal! hospital "555"))))
    (.start (Thread. (fn [] (chega-em-mal! hospital "666"))))
    (.start (Thread. (fn [] (Thread/sleep 8000)
                       (pprint hospital))))))

(simula-um-dia-em-paralelo)

; -----------------------------------------------------------------------------------------------------