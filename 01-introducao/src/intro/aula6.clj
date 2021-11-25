(ns intro.aula6)

(def pedido {:mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}})

(defn imprime [[chave valor]]
  (println chave "<e>" valor)
  valor)

(println (map imprime pedido))
; (:mochila <e> {:quantidade 2, :preco 80}
;  :camiseta <e> {:quantidade 3, :preco 40}
;  {:quantidade 2, :preco 80} {:quantidade 3, :preco 40})

(defn preco-dos-produtos [[_ valor]]
  (* (:quantidade valor) (:preco valor)))

(println (map preco-dos-produtos pedido))
;(160 120)

(println (reduce + (map preco-dos-produtos pedido)))
;280

(defn total-do-pedido
  [pedido]
  (reduce + (map preco-dos-produtos pedido)))

(total-do-pedido pedido)
;280

(defn preco-total-do-produto [produto]
  (* (:quantidade produto) (:preco produto)))


;----------------------------------------------------------------------------------------------------
; UTILIZANDO TREADING (THREAD LAST)

(defn total-do-pedido
  [pedido]
  (->> pedido
       (map preco-dos-produtos)
       (reduce +)))

(total-do-pedido pedido)
;280

; ou melhor
(defn total-do-pedido2 [pedido]
  (->> pedido
       vals
       (map preco-total-do-produto)
       (reduce +)))

(total-do-pedido2 pedido)
;280

;----------------------------------------------------------------------------------------------------
(def pedido {:mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}
             :chaveiro {:quantidade 1}})

(defn gratuito?
  [[_ item]]
  (<= (get item :preco 0) 0))

(println (filter gratuito? pedido))
;([:chaveiro {:quantidade 1}])

(defn pago?
  [pedido]
  (not (gratuito? pedido)))

; ou melhor
(def pago? (comp not gratuito?))

(println (filter pago? pedido))
;([:mochila {:quantidade 2, :preco 80}] [:camiseta {:quantidade 3, :preco 40}])

;----------------------------------------------------------------------------------------------------
;Dado o vetor a seguir, como calcular o total de certificados de todos os clientes?

(def clientes [
               {:nome         "Guilherme"
                :certificados ["Clojure" "Java" "Machine Learning"]}
               {:nome         "Paulo"
                :certificados ["Java" "Ciência da Computação"]}
               {:nome         "Daniela"
                :certificados ["Arquitetura" "Gastronomia"]}])

(println (->> clientes
             (map :certificados)
              (map count)
              (reduce +)))

;----------------------------------------------------------------------------------------------------