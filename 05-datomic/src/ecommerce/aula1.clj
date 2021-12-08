(ns ecommerce.aula1
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(def conn (db/abre-conexao))

(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)]
  (d/transact conn [computador]))

; Devolve o banco no instante da execução da linha de comando.
(def db (d/db conn))

(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)

(let [celular (model/novo-produto "Celular Caro", "/celular", 88888.10M)]
  (d/transact conn [celular]))

; Devolve uma nova fotografia (SNAPSHOT) do banco.
(def db (d/db conn))

(d/q '[:find ?entidade
       :where [?entidade :produto/nome]] db)

(db/apaga-banco)

; -------------------------------------------------------------------------------------