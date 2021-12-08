(ns ecommerce.aula5
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco)
(def conn (db/abre-conexao))
(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M),
      celular (model/novo-produto "Celular Caro", "/celular", 88888.10M)]
  (pprint @(d/transact conn [computador, celular])))

(def snapshot-past (d/db conn))

(let [calculadora {:produto/nome "Calculadora com 4 operações"},
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (pprint @(d/transact conn [calculadora, celular-barato])))

; -------------------------------------------------------------------------------------
; Bancos filtrados e o as-of

(println (count (db/todos-os-produtos (d/db conn))))
;=> 4

(println (count (db/todos-os-produtos snapshot-past)))
;=> 2

(println (count (db/todos-os-produtos (d/as-of (d/db conn) #inst "2021-12-08T14:38:00.829-00:00"))))
;=> 2

; -------------------------------------------------------------------------------------