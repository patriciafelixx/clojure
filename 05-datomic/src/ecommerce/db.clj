(ns ecommerce.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(def schema [{:db/ident       :produto/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O nome de um produto"}
             {:db/ident       :produto/slug
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one
              :db/doc         "O caminho para acessar esse produto via http"}
             {:db/ident       :produto/preco
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one
              :db/doc         "O preço de um produto com precisão monetária"}
             {:db/ident       :produto/palavra-chave
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/many}])

(defn cria-schema [conn]
  (d/transact conn schema))

(defn apaga-banco []
  (d/delete-database db-uri))

(defn todos-os-produtos [db]
  (d/q '[:find ?entidade
         :where [?entidade :produto/nome]] db))

; -------------------------------------------------------------------------------------
; Queries com parâmetros

(defn todos-os-produtos-por-slug [db slug]
  (d/q '[:find ?entidade
         :in $ ?slug-procurado
         :where [?entidade :produto/slug ?slug-procurado]]
       db, slug))

; -------------------------------------------------------------------------------------
; Buscando valores e atributos

(defn todos-os-slugs [db]
  (d/q '[:find ?slug
         :where [?e :produto/slug ?slug]], db))

; -------------------------------------------------------------------------------------
; Buscando com variáveis bound em condições anteriores

(defn todos-os-produtos-por-preco [db]
  (d/q '[:find ?nome, ?preco
         :keys produto/nome, produto/preco
         :where [?produto :produto/preco ?preco],
         [?produto :produto/nome ?nome]], db))

; -------------------------------------------------------------------------------------
; Pull e navegadores de pull

(defn todos-os-produtos [db]                                ; com-pull-especifico
  (d/q '[:find (pull ?entidade [:produto/nome, :produto/preco, :produto/slug])
         :where [?entidade :produto/nome]] db))

(defn todos-os-produtos [db]                                ; com-pull-generico
  (d/q '[:find (pull ?entidade [*])
         :where [?entidade :produto/nome]] db))

; -------------------------------------------------------------------------------------
; Plano de ação e queries com predicates

(defn todos-os-produtos-por-preco [db, preco-minimo]
  (d/q '[:find ?nome, ?preco
         :in $ ?preco-minimo
         :keys produto/nome, produto/preco
         :where [?produto :produto/preco ?preco],
         [(> ?preco ?preco-minimo)],
         [?produto :produto/nome ?nome]]
       db, preco-minimo))

; -------------------------------------------------------------------------------------
; Cardinalidade many e uma query misturando tudo

(defn todos-os-produtos-por-palavra-chave [db, palavra-chave]
  (d/q '[:find (pull ?produto [*])
         :in $ ?palavra-chave
         :where [?produto :produto/palavra-chave ?palavra-chave]]
       db, palavra-chave))

; -------------------------------------------------------------------------------------