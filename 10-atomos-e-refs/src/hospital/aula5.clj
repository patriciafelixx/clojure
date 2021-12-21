(ns hospital.aula5
  (:use [clojure pprint])
  (:require [hospital.logic :as logic]
            [hospital.model :as model]))

(defn chega-em! [hospital pessoa]
  (swap! hospital logic/chega-em :espera pessoa))

(defn transfere! [hospital fila-de fila-para]
  (swap! hospital logic/transfere fila-de fila-para))

(defn atende! [hospital fila]
  (swap! hospital logic/atende fila))

(defn simula-um-dia []
  (let [hospital (atom (model/novo-hospital))]
    (chega-em! hospital "joão")
    (chega-em! hospital "maria")
    (chega-em! hospital "daniela")
    (chega-em! hospital "guilherme")
    (pprint hospital)
    (transfere! hospital :espera :laboratorio1)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :espera :laboratorio2)
    (transfere! hospital :laboratorio2 :laboratorio3)
    (atende! hospital :laboratorio2)
    (pprint hospital)))

(simula-um-dia)

; -----------------------------------------------------------------------------------------------------