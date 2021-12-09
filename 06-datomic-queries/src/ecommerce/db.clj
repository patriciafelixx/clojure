(ns ecommerce.db
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

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

(defn todos-os-produtos [db]
  (d/q '[:find (pull ?entidade [*])
         :where [?entidade :produto/nome]] db))

(defn todas-as-categorias [db]
  (d/q '[:find (pull ?categoria [*])
         :where [?categoria :categoria/id]] db))

; ----------------------------------------------------------------------------------
; Pull puro e identificadores

(defn um-produto-por-dbid [db produto-id]
  (d/pull db '[*] produto-id))

(defn um-produto [db produto-id]
  (d/pull db '[*] [:produto/id produto-id]))

; ----------------------------------------------------------------------------------

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

(defn adiciona-produtos!
  ([conn produtos]
   (d/transact conn produtos))
  ([conn produtos ip]
   (let [db-add-ip [:db/add "datomic.tx" :tx-data/ip ip]]
     (d/transact conn (conj produtos db-add-ip)))))

(defn adiciona-categorias! [conn categorias]
  (d/transact conn categorias))

; ----------------------------------------------------------------------------------
; Juntando duas entidades em uma query

(defn todos-os-nomes-de-produtos-e-categorias [db]
  (d/q '[:find ?produto-nome ?categoria-nome
         :where [?produto :produto/nome ?produto-nome]
         [?produto :produto/categoria ?produto-categoria]
         [?produto-categoria :categoria/nome ?categoria-nome]] db))

; ----------------------------------------------------------------------------------
; Pull e forward navigation

(defn todos-os-produtos-da-categoria [db categoria-nome]
  (d/q '[:find (pull ?produto [:produto/nome {:produto/categoria [:categoria/nome]}])
         :in $ ?categoria-nome
         :where [?categoria :categoria/nome ?categoria-nome]
         [?produto :produto/categoria ?categoria]
         [?produto :produto/nome ?produto-nome]]
       db, categoria-nome))

; ----------------------------------------------------------------------------------
; Pull e backward navigation

(defn todos-os-produtos-da-categoria [db categoria-nome]
  (d/q '[:find (pull ?categoria [:categoria/nome {:produto/_categoria [:produto/nome :produto/slug]}])
         :in $ ?categoria-nome
         :where [?categoria :categoria/nome ?categoria-nome]]
       db, categoria-nome))


; DICA: Diferença entre forward navigation e backward navigation
; forward navigation        ?produto :produto/categoria >>>>>>>>>>>
; backward navigation       >>>>>>>> :produto/_categoria ?categoria

; -----------------------------------------------------------------------------------------------
; aggregates (https://docs.datomic.com/cloud/query/query-data-reference.html#aggregates)

(defn resumo-dos-produtos [db]
  (d/q '[:find (min ?preco) ?preco (count ?preco) (sum ?preco)
         :keys minimo maximo quantidade soma-tds-precos
         :with ?produto
         :where [?produto :produto/preco ?preco]] db))

(defn resumo-dos-produtos-por-categoria [db]
  (d/q '[:find ?nome (min ?preco) (max ?preco) (count ?preco) (sum ?preco)
         :keys categoria minimo maximo quantidade soma-tds-precos
         :with ?produto
         :where [?produto :produto/preco ?preco]
         [?produto :produto/categoria ?categoria]
         [?categoria :categoria/nome ?nome]] db))

; -----------------------------------------------------------------------------------------------
; nested queries (https://docs.datomic.com/cloud/query/query-data-reference.html#q)

;(defn todos-os-produtos-mais-caros [db]
;  (let [preco-mais-alto (ffirst (d/q '[:find (max ?preco)
;                                       :where [_ :produto/preco ?preco]] db))]
;    (d/q '[:find (pull ?produto [*])
;           :in $ ?preco-mais-alto
;           :where [?produto :produto/preco ?preco-mais-alto]]
;         db preco-mais-alto)))

(defn todos-os-produtos-mais-caros [db]
  (d/q '[:find (pull ?produto [*])
         :where [(q '[:find (max ?preco)
                      :where [_ :produto/preco ?preco]]
                    $) [[?preco]]]
         [?produto :produto/preco ?preco]] db))

(defn todos-os-produtos-mais-baratos [db]
  (d/q '[:find (pull ?produto [*])
         :where [(q '[:find (min ?preco)
                      :where [_ :produto/preco ?preco]]
                    $) [[?preco]]]
         [?produto :produto/preco ?preco]] db))

; -----------------------------------------------------------------------------------------------
; gravando informações em transações

(defn todos-os-produtos-do-ip [db ip]
  (d/q '[:find (pull ?produto [*])
         :in $ ?ip-buscado
         :where [?transacao :tx-data/ip ?ip-buscado]
         [?produto :produto/id _ ?transacao]]
       db ip))

; -----------------------------------------------------------------------------------------------