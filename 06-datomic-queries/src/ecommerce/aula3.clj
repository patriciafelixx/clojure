(ns ecommerce.aula3
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]))

(db/apaga-banco!)
(def conn (db/abre-conexao))
(db/cria-schema! conn)

; -----------------------------------------------------------------------------------------------
(def computador (model/novo-produto (model/uuid) "Computador Novo", "/computador-novo", 2500.10M))
(def celular (model/novo-produto (model/uuid) "Celular Caro", "/celular", 88888.10M))
(def calculadora {:produto/nome "Calculadora com 4 operações"})
(def celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M))
(def xadrez (model/novo-produto "Tabuleiro de Xadrez", "/tabuleiro-de-xadrez", 30M))

;(pprint @(d/transact conn [computador, celular, calculadora, celular-barato, xadrez]))
(db/adiciona-produtos! conn [computador, celular, calculadora, celular-barato, xadrez])

; -----------------------------------------------------------------------------------------------
(def eletronicos (model/nova-categoria "Eletrônicos"))
(def esporte (model/nova-categoria "Esporte"))

;(pprint @(d/transact conn [eletronicos, esporte]))
(db/adiciona-categorias! conn [eletronicos, esporte])

; -----------------------------------------------------------------------------------------------
;(d/transact conn [[:db/add [:produto/id (:produto/id computador)]
;                   :produto/categoria [:categoria/id (:categoria/id eletronicos)]]])
;
;(d/transact conn [[:db/add [:produto/id (:produto/id celular)]
;                   :produto/categoria [:categoria/id (:categoria/id eletronicos)]]])
;
;(d/transact conn [[:db/add [:produto/id (:produto/id celular-barato)]
;                   :produto/categoria [:categoria/id (:categoria/id eletronicos)]]])
;
;(d/transact conn [[:db/add [:produto/id (:produto/id xadrez)]
;                   :produto/categoria [:categoria/id (:categoria/id esporte)]]])

(db/atribui-categorias! conn [computador, celular, celular-barato] eletronicos)
(db/atribui-categorias! conn [xadrez] esporte)

; -----------------------------------------------------------------------------------------------
(pprint (db/todas-as-categorias (d/db conn)))
(pprint (db/todos-os-produtos (d/db conn)))

(db/um-produto (d/db conn) (:produto/id celular-barato))

; -----------------------------------------------------------------------------------------------