(ns ecommerce.aula4
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

; -------------------------------------------------------------------------------------
; Pull e navegadores de pull

(db/todos-os-produtos (d/db conn))                          ; com-pull-especifico
;=>
;[[#:produto{:nome "Computador Novo", :preco 2500.10M, :slug "/computador-novo"}]
; [#:produto{:nome "Celular Caro", :preco 88888.10M, :slug "/celular"}]
; [#:produto{:nome "Calculadora com 4 operações"}]
; [#:produto{:nome "Celular Barato", :preco 0.1M, :slug "/celular-barato"}]]

(db/todos-os-produtos (d/db conn))        ; com-pull-generico
;=>
;[[{:db/id 17592186045418, :produto/nome "Computador Novo", :produto/slug "/computador-novo", :produto/preco 2500.10M}]
; [{:db/id 17592186045419, :produto/nome "Celular Caro", :produto/slug "/celular", :produto/preco 88888.10M}]
; [{:db/id 17592186045420, :produto/nome "Calculadora com 4 operações"}]
; [{:db/id 17592186045421, :produto/nome "Celular Barato", :produto/slug "/celular-barato", :produto/preco 0.1M}]]

; -------------------------------------------------------------------------------------