(ns ecommerce.aula4
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco!)
(def conn (db/abre-conexao))
(db/cria-schema! conn)

(def computador (model/novo-produto (model/uuid) "Computador Novo", "/computador-novo", 2500.10M))
(def celular (model/novo-produto (model/uuid) "Celular Caro", "/celular", 88888.10M))
(def calculadora {:produto/nome "Calculadora com 4 operações"})
(def celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M))
(def xadrez (model/novo-produto "Tabuleiro de Xadrez", "/tabuleiro-de-xadrez", 30M))

(def eletronicos (model/nova-categoria "Eletrônicos"))
(def esporte (model/nova-categoria "Esporte"))

(db/adiciona-produtos! conn [computador, celular, calculadora, celular-barato, xadrez])
(db/adiciona-categorias! conn [eletronicos, esporte])

(db/atribui-categorias! conn [computador, celular, celular-barato] eletronicos)
(db/atribui-categorias! conn [xadrez] esporte)

(def produtos (db/todos-os-produtos (d/db conn)))
(pprint produtos)

; -----------------------------------------------------------------------------------------------
; Pull e backward navigation e Pull e backward navigation

(pprint (db/todos-os-nomes-de-produtos-e-categorias (d/db conn)))
(pprint (db/todos-os-produtos-da-categoria (d/db conn) "Eletrônicos"))
(pprint (db/todos-os-produtos-da-categoria (d/db conn) "Esporte"))

; -----------------------------------------------------------------------------------------------