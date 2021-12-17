(ns ecommerce.aula4
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(db/apaga-banco!)
(def conn (db/abre-conexao))
(db/cria-schema! conn)
(db/cria-dados-de-exemplo conn)

; --------------------------------------------------------------------------------------------------------------------
; Find Specs

(def todos-os-produtos (db/todos-os-produtos (d/db conn)))
(def produtos-com-estoque (db/todos-os-produtos-com-estoque (d/db conn)))
(pprint produtos-com-estoque)

(pprint (db/um-produto-com-estoque (d/db conn) (:produto/id (first todos-os-produtos))))
(pprint (db/um-produto-com-estoque (d/db conn) (:produto/id (second todos-os-produtos))))

; --------------------------------------------------------------------------------------------------------------------