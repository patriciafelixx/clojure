(ns ecommerce.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.model :as model]
            [schema.core :as s]
            [clojure.walk :as walk]))

(def db-uri "datomic:dev://localhost:4334/ecommerce")

(defn abre-conexao []
  (d/create-database db-uri)
  (d/connect db-uri))

(def schema [
             ; PRODUTOS
             {:db/ident       :produto/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/slug
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/preco
              :db/valueType   :db.type/bigdec
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/palavra-chave
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/many}
             {:db/ident       :produto/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}
             {:db/ident       :produto/categoria
              :db/valueType   :db.type/ref
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/estoque
              :db/valueType   :db.type/long
              :db/cardinality :db.cardinality/one}
             {:db/ident       :produto/digital
              :db/valueType   :db.type/boolean
              :db/cardinality :db.cardinality/one}

             ; CATEGORIAS
             {:db/ident       :categoria/nome
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}
             {:db/ident       :categoria/id
              :db/valueType   :db.type/uuid
              :db/cardinality :db.cardinality/one
              :db/unique      :db.unique/identity}

             ;TRANSAÇÕES
             {:db/ident       :tx-data/ip
              :db/valueType   :db.type/string
              :db/cardinality :db.cardinality/one}])

(defn cria-schema! [conn]
  (d/transact conn schema))

(defn apaga-banco! []
  (d/delete-database db-uri))

(defn dissoc-db-id [entidade]
  (if (map? entidade)
    (dissoc entidade :db/id)
    entidade))

;(defn datomic-para-entidade [entidades]
;  (map #(dissoc % :db/id) entidades))
(defn datomic-para-entidade [entidades]
  (walk/prewalk dissoc-db-id entidades))

(s/defn adiciona-categorias! [conn categorias :- [model/Categoria]]
  (d/transact conn categorias))

(s/defn adiciona-ou-altera-produtos!
  ([conn produtos :- [model/Produto]]
   (d/transact conn produtos))
  ([conn produtos :- [model/Produto] ip]
   (let [db-add-ip [:db/add "datomic.tx" :tx-data/ip ip]]
     (d/transact conn (conj produtos db-add-ip)))))

(s/defn um-produto :- (s/maybe model/Produto) [db produto-id :- java.util.UUID]
  (let [resultado (d/pull db '[* {:produto/categoria [*]}] [:produto/id produto-id])
        produto (datomic-para-entidade resultado)]
    (if (:produto/id produto)
      produto
      nil)))

(s/defn um-produto! :- model/Produto [db, produto-id :- java.util.UUID]
  (let [produto (um-produto db produto-id)]
    (when (nil? produto)
      (throw (ex-info "Não encontrei uma entidade"
                      {:type :errors/not-found, :id produto-id})))
    produto))

(defn db-adds-de-atribuicao-de-categorias
  [produtos categoria]
  (reduce (fn [db-adds produto]
            (conj db-adds
                  [:db/add [:produto/id (:produto/id produto)]
                   :produto/categoria [:categoria/id (:categoria/id categoria)]])) []
          produtos))

(defn atribui-categorias! [conn produtos categoria]
  (let [a-transacionar (db-adds-de-atribuicao-de-categorias produtos categoria)]
    (d/transact conn a-transacionar)))


(s/defn todos-os-produtos :- [model/Produto] [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?entidade [* {:produto/categoria [*]}]) ...]
           :where [?entidade :produto/nome]] db)))

(s/defn todas-as-categorias :- [model/Categoria] [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?categoria [*]) ...]
           :where [?categoria :categoria/id]] db)))

(def regras
  '[
    [(estoque ?produto ?estoque)
     [?produto :produto/estoque ?estoque]]
    [(estoque ?produto ?estoque)
     [?produto :produto/digital true]
     [(ground 100) ?estoque]]
    [(pode-vender? ?produto)
     (estoque ?produto ?estoque)
     [(> ?estoque 0)]]
    ])

(s/defn todos-os-produtos-vendaveis :- [model/Produto] [db]
  (datomic-para-entidade
    (d/q '[:find [(pull ?produto [* {:produto/categoria [*]}]) ...]
           :in $ %
           :where (pode-vender? ?produto)]
         db regras)))

(s/defn um-produto-vendavel :- (s/maybe model/Produto) [db, produto-id :- java.util.UUID]
  (let [query '[:find (pull ?produto [* {:produto/categoria [*]}]) .
                :in $ % ?id
                :where [?produto :produto/id ?id]
                (pode-vender? ?produto)]
        resultado (d/q query db regras produto-id)
        produto (datomic-para-entidade resultado)]
    (if (:produto/id produto)
      produto
      nil)))

(defn cria-dados-de-exemplo [conn]
  (def computador (model/novo-produto "Computador Novo", "/computador-novo", 2500.10M, 10))
  (def celular (model/novo-produto "Celular Caro", "/celular", 88888.10M))
  (def celular-barato (model/novo-produto "Celular Barato", "/celular-barato", 0.1M))
  (def xadrez (model/novo-produto "Tabuleiro de Xadrez", "/tabuleiro-de-xadrez", 30M, 5))
  (def jogo (assoc (model/novo-produto "Jogo Online", "/jogo-online", 20M) :produto/digital true))
  (adiciona-ou-altera-produtos! conn [computador, celular, celular-barato, xadrez, jogo])

  (def eletronicos (model/nova-categoria "Eletrônicos"))
  (def esporte (model/nova-categoria "Esporte"))
  (adiciona-categorias! conn [eletronicos, esporte])

  (atribui-categorias! conn [computador, celular, celular-barato] eletronicos)
  (atribui-categorias! conn [xadrez] esporte))
