(ns colecoes.aula4
  (:require [colecoes.db :as c.db]
            [colecoes.logic :as c.logic]))

(println (c.logic/resumo-por-usuario (c.db/todos-os-pedidos)))
;({:usuario-id 15, :total-de-pedidos 3, :preco-total 840}
; {:usuario-id 1, :total-de-pedidos 1, :preco-total 280}
; {:usuario-id 10, :total-de-pedidos 1, :preco-total 1720}
; {:usuario-id 20, :total-de-pedidos 1, :preco-total 560})

(let [pedidos (c.db/todos-os-pedidos)
      resumo (c.logic/resumo-por-usuario pedidos)]
  (println "resumo:" resumo)
  (println "ordenado preco:" (sort-by :preco-total resumo))
  (println "ordenado preco rev:" (reverse (sort-by :preco-total resumo)))
  (println "ordenado id:" (sort-by :usuario-id resumo))

  (println (get-in pedidos [0 :itens :mochila :quantidade]))) ;; 2


(defn resumo-por-usuario-ordenado [pedidos]
  (->> pedidos
       c.logic/resumo-por-usuario
       (sort-by :preco-total)
       reverse))

(let [pedidos (c.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println "resumo:" resumo))

;----------------------------------------------------------------------------------------------------
(let [pedidos (c.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println "resumo =>" resumo)
  (println "first =>" (first resumo))
  (println "second =>" (second resumo))
  (println "rest =>" (rest resumo))
  (println "count =>" (count resumo))
  (println "class =>" (class resumo))
  (println "nth =>" (nth resumo 2))
  (println "get =>" (get resumo 2))
  (println "take =>" (take 2 resumo)))

;----------------------------------------------------------------------------------------------------
(defn top-2 [resumo]
  (take 2 resumo))

(let [pedidos (c.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println "TOP 2" (top-2 resumo)))

;----------------------------------------------------------------------------------------------------
(let [pedidos (c.db/todos-os-pedidos)
      resumo (resumo-por-usuario-ordenado pedidos)]
  (println "filter 500+ =>" (filter #(> (:preco-total %) 500) resumo))
  (println "some 500+ =>" (some #(> (:preco-total %) 500) resumo)))

;----------------------------------------------------------------------------------------------------