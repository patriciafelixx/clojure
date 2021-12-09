(ns ecommerce.aula1
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco)
(def conn (db/abre-conexao))
(db/cria-schema conn)

(let [computador (model/novo-produto (model/uuid) "Computador Novo", "/computador-novo", 2500.10M),
      celular (model/novo-produto (model/uuid) "Celular Caro", "/celular", 88888.10M),
      calculadora {:produto/nome "Calculadora com 4 operações"},
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (d/transact conn [computador, celular, calculadora, celular-barato]))

(db/todos-os-produtos (d/db conn))

; -----------------------------------------------------------------------------------------------
; Pull puro, identificadores únicos e UUIDs

(db/um-produto-por-dbid (d/db conn) 17592186045419)

(def produtos (db/todos-os-produtos (d/db conn)))
(def primeiro-dbid (-> produtos ffirst :db/id))
(def primeiro-produto-dbid (db/um-produto-por-dbid (d/db conn) primeiro-dbid))
(println "Primeiro produto por db/id:" primeiro-produto-dbid)

(def primeiro-uuid (-> produtos ffirst :produto/id))
(println primeiro-uuid)
(def primeiro-produto-uuid (db/um-produto (d/db conn) primeiro-uuid))
(println "Primeiro produto por uuid:" primeiro-produto-uuid)

; -----------------------------------------------------------------------------------------------