(ns ecommerce.aula3
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M),
      celular (model/novo-produto "Celular Caro", "/celular", 88888.10M),
      calculadora {:produto/nome "Calculadora com 4 operações"},
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (d/transact conn [computador, celular, calculadora, celular-barato]))
;=>
;#object[datomic.promise$settable_future$reify__7966
;        0x76039b6e
;        {:status :ready,
;         :val {:db-before datomic.db.Db,
;               @9e3fe261 :db-after,
;               datomic.db.Db @f8582ce,
;               :tx-data [#datom[13194139534313 50 #inst"2021-12-07T20:39:13.179-00:00" 13194139534313 true]
;                         #datom[17592186045418 72 "Computador Novo" 13194139534313 true]
;                         #datom[17592186045418 73 "/computador_novo" 13194139534313 true]
;                         #datom[17592186045418 74 2500.10M 13194139534313 true]
;                         #datom[17592186045419 72 "Celular Caro" 13194139534313 true]
;                         #datom[17592186045419 73 "/celular" 13194139534313 true]
;                         #datom[17592186045419 74 88888.10M 13194139534313 true]
;                         #datom[17592186045420 72 "Calculadora com 4 operações" 13194139534313 true]
;                         #datom[17592186045421 72 "Celular Barato" 13194139534313 true]
;                         #datom[17592186045421 73 "/celular-barato" 13194139534313 true]
;                         #datom[17592186045421 74 0.1M 13194139534313 true]],
;               :tempids {-9223301668109598109 17592186045418,
;                         -9223301668109598108 17592186045419,
;                         -9223301668109598107 17592186045420,
;                         -9223301668109598106 17592186045421}}}]

(println (db/todos-os-produtos (d/db conn)))

(pprint (db/todos-os-produtos-por-slug (d/db conn) "/computador-novo"))

(pprint (db/todos-os-slugs (d/db conn)))

(pprint (db/todos-os-produtos-por-preco (d/db conn)))

; -------------------------------------------------------------------------------------