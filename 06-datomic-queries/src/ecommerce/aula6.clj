(ns ecommerce.aula6
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
(def camiseta (model/novo-produto "Camiseta", "/camiseta", 30M))
(def dama (model/novo-produto "Dama", "/dama", 15M))

(def eletronicos (model/nova-categoria "Eletrônicos"))
(def esporte (model/nova-categoria "Esporte"))
(def roupas (model/nova-categoria "Roupas"))

(db/adiciona-produtos! conn [computador, celular, calculadora, celular-barato] "200.216.222.125")
(db/adiciona-produtos! conn [xadrez, camiseta, dama] "20.216.222.12")
(db/adiciona-categorias! conn [eletronicos, esporte, roupas])

(db/atribui-categorias! conn [computador, celular, celular-barato] eletronicos)
(db/atribui-categorias! conn [xadrez, dama] esporte)
(db/atribui-categorias! conn [camiseta] roupas)

; -----------------------------------------------------------------------------------------------
; nested queries

(db/todos-os-produtos (d/db conn))
(db/todos-os-produtos-mais-caros (d/db conn))
(db/todos-os-produtos-mais-baratos (d/db conn))

; -----------------------------------------------------------------------------------------------
; gravando informações em transações

(db/todos-os-produtos-do-ip (d/db conn) "200.216.222.125")
(db/todos-os-produtos-do-ip (d/db conn) "20.216.222.12")
(db/todos-os-produtos-do-ip (d/db conn) "210.216.222.12")

; -----------------------------------------------------------------------------------------------