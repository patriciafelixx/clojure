(ns ecommerce.aula6
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco)
(def conn (db/abre-conexao))
(db/cria-schema conn)

(let [computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M),
      celular (model/novo-produto "Celular Caro", "/celular", 88888.10M),
      calculadora {:produto/nome "Calculadora com 4 operações"},
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M)]
  (d/transact conn [computador, celular, calculadora, celular-barato]))

; -------------------------------------------------------------------------------------
; Plano de ação e queries com predicates
(db/todos-os-produtos-por-preco (d/db conn) 1000)

; -------------------------------------------------------------------------------------
; Cardinalidade many e uma query misturando tudo

(d/transact conn [[:db/add 17592186045418 :produto/palavra-chave "desktop"]
                  [:db/add 17592186045418 :produto/palavra-chave "computador"]])

(d/transact conn [[:db/retract 17592186045418 :produto/palavra-chave "computador"]])

(d/transact conn [[:db/add 17592186045418 :produto/palavra-chave "monitor 20"]])

(d/transact conn [[:db/add 17592186045419 :produto/palavra-chave "celular"]
                  [:db/add 17592186045421 :produto/palavra-chave "celular"]])

(db/todos-os-produtos (d/db conn))

(db/todos-os-produtos-por-palavra-chave (d/db conn) "celular")

; -------------------------------------------------------------------------------------

