(ns ecommerce.aula5
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
; Rules

(def todos-os-produtos (db/todos-os-produtos (d/db conn)))
;(pprint todos-os-produtos)

(def produtos-vendaveis (db/todos-os-produtos-vendaveis (d/db conn)))
;(pprint produtos-vendaveis)

(pprint (db/um-produto-vendavel (d/db conn) (:produto/id (first todos-os-produtos))))
(pprint (db/um-produto-vendavel (d/db conn) (:produto/id (second todos-os-produtos))))

; --------------------------------------------------------------------------------------------------------------------