(ns colecoes.aula3
  (:require [colecoes.db :as c.db]))

(println (c.db/todos-os-pedidos))

(println (group-by :usuario (c.db/todos-os-pedidos)))
;{ 15 [...],
;   1 [...],
;  10 [...],
;  20 [...] }

(defn minha-funcao-de-agrupamento [elemento]
  (:usuario elemento))

(println (group-by minha-funcao-de-agrupamento (c.db/todos-os-pedidos)))
;{ 15 [x, y, z],
;   1 [x],
;  10 [x],
;  20 [x] }

(println (count (vals (group-by :usuario (c.db/todos-os-pedidos)))))
; 4

(println (map count (vals (group-by :usuario (c.db/todos-os-pedidos)))))
;(3 1 1 1)

(->> (c.db/todos-os-pedidos)
     (group-by :usuario)
     vals
     (map count)
     println)
;(3 1 1 1)


(defn conta-total-por-usuario
  [[usuario, pedidos]]
  [usuario (count pedidos)])

(->> (c.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)
;([15 3] [1 1] [10 1] [20 1])


(defn conta-total-por-usuario
  [[usuario, pedidos]]
  {:usuario-id       usuario
   :total-de-pedidos (count pedidos)})

(->> (c.db/todos-os-pedidos)
     (group-by :usuario)
     (map conta-total-por-usuario)
     println)
;({:usuario-id 15, :total-de-pedidos 3}
; {:usuario-id 1, :total-de-pedidos 1}
; {:usuario-id 10, :total-de-pedidos 1}
; {:usuario-id 20, :total-de-pedidos 1})

;----------------------------------------------------------------------------------------------------
(println "----- PEDIDOS -----")

(defn total-do-item
  [[_ detalhes]]
  (* (get detalhes :quantidade 0) (get detalhes :preco-unit 0)))

(defn total-do-pedido
  [pedido]
  (->> pedido
       (map total-do-item)
       (reduce +)))

(defn total-dos-pedidos
  [pedidos]
  (->> pedidos
       (map :itens)
       (map total-do-pedido)
       (reduce +)))

(defn quantia-de-pedidos-e-gasto-total-por-usuario
  [[usuario, pedidos]]
  {:usuario-id       usuario
   :total-de-pedidos (count pedidos)
   :preco-total      (total-dos-pedidos pedidos)})

(->> (c.db/todos-os-pedidos)
     (group-by :usuario)
     (map quantia-de-pedidos-e-gasto-total-por-usuario)
     println)

;----- PEDIDOS --------------------------------------------------------------------------------------
;({:usuario-id 15, :total-de-pedidos 3, :preco-total 840}
; {:usuario-id 1, :total-de-pedidos 1, :preco-total 280}
; {:usuario-id 10, :total-de-pedidos 1, :preco-total 1720}
; {:usuario-id 20, :total-de-pedidos 1, :preco-total 560})

;----------------------------------------------------------------------------------------------------