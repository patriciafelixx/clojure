(ns hospital.aula2
  (:use [clojure.pprint])
  (:require [hospital.model :as model]
            [hospital.logic :as logic]))

(defn chega-em-teste [pessoa]
  (def clinica-z (logic/chega-em clinica-z :espera pessoa)))

(defn simula-um-dia-em-paralelo []
  (def clinica-z (model/novo-hospital))
  (.start (Thread. (fn [] (chega-em-teste "111"))))
  (.start (Thread. (fn [] (chega-em-teste "222"))))
  (.start (Thread. (fn [] (chega-em-teste "333"))))
  (.start (Thread. (fn [] (chega-em-teste "444"))))
  (.start (Thread. (fn [] (chega-em-teste "555"))))
  (.start (Thread. (fn [] (chega-em-teste "666"))))
  (.start (Thread. (fn [] (Thread/sleep 4000)
                     (pprint clinica-z)))))

(simula-um-dia-em-paralelo)

; -----------------------------------------------------------------------------------------------------