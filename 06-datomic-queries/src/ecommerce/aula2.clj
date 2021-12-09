(ns ecommerce.aula2
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco)
(def conn (db/abre-conexao))
(db/cria-schema conn)

; -----------------------------------------------------------------------------------------------
; Unicidade no add, o update

(def celular (model/novo-produto (model/uuid) "Celular", "/celular", 1000M))
(d/transact conn [celular])
(db/todos-os-produtos (d/db conn))

(def novo-celular (model/novo-produto (:produto/id celular) "Novo Celular", "/novo-celular", 2000M))
(d/transact conn [novo-celular])
(db/todos-os-produtos (d/db conn))

; a entidade foi sobrescrita, pois o campo id é um indetificador único.
; Quando tentamos inserir itens em um id já existente, é como se ocorre uma atualização do produto,
; isto é, acontece uma sobreposição. Dessa maneira a unicidade do elemento é mantida.

; -----------------------------------------------------------------------------------------------