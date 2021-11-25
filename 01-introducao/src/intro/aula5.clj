(ns intro.aula5)

;(def estoque {"Mochila"  10, "Camiseta" 5})
; não usual usar strings nas chaves, ideal usar keywords (:item)

(def estoque {:mochila  10
              :camiseta 5})

(println "count:" (count estoque))
(println "keys:" (keys estoque))
(println "vals:" (vals estoque))
;count: 2
;keys: (:mochila :camiseta)
;vals: (10 5)

(println (assoc estoque :cadeira 3))
;{:mochila 10, :camiseta 5, :cadeira 3}

(println (update estoque :camiseta inc))
;{:mochila 10, :camiseta 6}

(defn tira-um [valor]
  (- valor 1))

(println (update estoque :mochila tira-um))
;{:mochila 9, :camiseta 5}

(println (update estoque :mochila #(- % 3)))
;{:mochila 7, :camiseta 5}

(println (dissoc estoque :mochila))
;{:camiseta 5}

;----------------------------------------------------------------------------------------------------

(def pedido {:mochila  {:quantidade 2, :preco 80}
             :camiseta {:quantidade 3, :preco 40}})

(println (pedido :mochila))
(println (get pedido :cadeira))
(println (get pedido :cadeira 0))
(println (get pedido :cadeira {}))
;{:quantidade 2, :preco 80}
;nil
;0
;{}

; keywords antes, como funcao, é mais comumente usado
(println (:mochila pedido))
(println (:quantidade (:mochila pedido)))
;{:quantidade 2, :preco 80}
;2

(println (update-in pedido [:mochila :quantidade] inc))
;{:mochila {:quantidade 3, :preco 80}, :camiseta {:quantidade 3, :preco 40}}


; THREADING - MAIOR LEGIBILIDADE (THREAD FIRST)
(println (-> pedido
             :mochila
             :quantidade))

;----------------------------------------------------------------------------------------------------
; Dado o mapa a seguir, como extrair o total de certificados que o Guilherme tem?
(def
  clientes {
            :15 {
                 :nome         "Guilherme"
                 :certificados ["Clojure" "Java" "Machine Learning"]}})

(println (-> clientes
             :15
             :certificados
             count))

;----------------------------------------------------------------------------------------------------