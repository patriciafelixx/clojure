(ns hospital.aula1
  (:use [clojure.pprint])
  (:require [hospital.model :as model]
            [hospital.logic :as logic]))

(defn simula-um-dia []
  (def clinica-x (model/novo-hospital))

  (def clinica-x (logic/chega-em clinica-x :espera "111"))
  (def clinica-x (logic/chega-em clinica-x :espera "222"))
  (def clinica-x (logic/chega-em clinica-x :espera "333"))
  (def clinica-x (logic/chega-em clinica-x :laboratorio1 "444"))
  (def clinica-x (logic/chega-em clinica-x :laboratorio3 "555"))

  (def clinica-x (logic/atende clinica-x :laboratorio1))
  (def clinica-x (logic/atende clinica-x :espera))

  (def clinica-x (logic/chega-em clinica-x :espera "666"))
  (def clinica-x (logic/chega-em clinica-x :espera "777"))
  (def clinica-x (logic/chega-em clinica-x :espera "888"))
  (def clinica-x (logic/chega-em clinica-x :espera "999"))

  (pprint clinica-x))

(simula-um-dia)

; -----------------------------------------------------------------------------------------------------