(ns ecommerce.aula5
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

; -----------------------------------------------------------------------------------------------
; db/adds com nested maps (mapas aninhados)

(db/adiciona-produtos! conn [{:produto/id        (model/uuid)
                              :produto/nome      "Camiseta"
                              :produto/slug      "/camiseta"
                              :produto/preco     30M
                              :produto/categoria {:categoria/id   (model/uuid)
                                                  :categoria/nome "Roupas"}}])

(db/adiciona-produtos! conn [{:produto/id        (model/uuid)
                              :produto/nome      "Dama"
                              :produto/slug      "/dama"
                              :produto/preco     15M
                              :produto/categoria [:categoria/id (:categoria/id esporte)]}])

(db/todos-os-produtos (d/db conn))
(db/todas-as-categorias (d/db conn))

; -----------------------------------------------------------------------------------------------
; aggregates

(db/resumo-dos-produtos (d/db conn))

(pprint (db/resumo-dos-produtos-por-categoria (d/db conn)))

; -----------------------------------------------------------------------------------------------