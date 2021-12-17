(ns ecommerce.aula2
  (:use clojure.pprint)
  (:require [datomic.api :as d]
            [ecommerce.db :as db]
            [ecommerce.model :as model]
            [schema.core :as s]))

(s/set-fn-validation! true)

(db/apaga-banco!)
(def conn (db/abre-conexao))
(db/cria-schema! conn)

;(db/cria-dados-de-exemplo conn)

(def dama {:produto/nome  "Dama"
           :produto/slug  "/dama"
           :produto/preco 15.5M
           :produto/id    (model/uuid)})

(db/adiciona-ou-altera-produtos! conn [dama])

; --------------------------------------------------------------------------------------------------------------------
; update/insert ===> upsert
(db/adiciona-ou-altera-produtos! conn [(assoc dama :produto/slug "/jogo-de-dama")])
(db/um-produto (d/db conn) (:produto/id dama))

(db/adiciona-ou-altera-produtos! conn [(assoc dama :produto/preco 150.5M)])
(db/um-produto (d/db conn) (:produto/id dama))

; --------------------------------------------------------------------------------------------------------------------

(defn roda-transacoes [tx]
  (let [futuros (mapv #(future (%)) tx)]
    (pprint (map deref futuros))
    (pprint "Resultado final")
    (pprint (db/um-produto (d/db conn) (:produto/id dama)))))

(defn atualiza-preco []
  (println "Atualizando preco...")
  (let [produto {:produto/id (:produto/id dama), :produto/preco 111M}]
    (db/adiciona-ou-altera-produtos! conn [produto])
    (println "Preco atualizado!")
    produto))

(defn atualiza-slug []
  (println "Atualizando slug...")
  (let [produto {:produto/id (:produto/id dama), :produto/slug "/dama-com-slug-novo"}]
    (Thread/sleep 3000)
    (db/adiciona-ou-altera-produtos! conn [produto])
    (println "Slug atualizado!")
    produto))

(roda-transacoes [atualiza-preco atualiza-slug])

; --------------------------------------------------------------------------------------------------------------------