(ns ecommerce.aula2
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [celular (model/novo-produto "Celular Caro", "/celular", 88888.10M)]
  (d/transact conn [celular]))

(let [calculadora {:produto/nome "Calculadora com 4 operações"}]
  (d/transact conn [calculadora]))

(let [celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 888.00M)
      resultado @(d/transact conn [celular-barato])
      ;id-entidade (first (vals (:tempids resultado)))
      id-entidade (-> resultado :tempids vals first)]
  (pprint (d/transact conn [[:db/add id-entidade :produto/preco 0.1M]]))
  (pprint (d/transact conn [[:db/retract id-entidade :produto/slug "/celular-barato"]])))

; -------------------------------------------------------------------------------------