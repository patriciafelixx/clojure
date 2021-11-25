(ns colecoes.aula5
  (:require [colecoes.db :as c.db]
            [colecoes.logic :as c.logic]))

;(defn gastou-bastante? [info-do-usuario]
;  (> (:preco-total info-do-usuario) 500))
;
;(let [pedidos (c.db/todos-os-pedidos)
;      resumo (c.logic/resumo-por-usuario pedidos)]
;  (println "keep =>" (keep gastou-bastante? resumo))
;  (println "filter =>" (filter gastou-bastante? resumo)))

;----------------------------------------------------------------------------------------------------
; Tentando entender dentro do keep

(defn gastou-bastante? [info-do-usuario]
  (println "gastou-bastante?" (:usuario-id info-do-usuario))
  (> (:preco-total info-do-usuario) 500))

(let [pedidos (c.db/todos-os-pedidos)
      resumo (c.logic/resumo-por-usuario pedidos)]
  (println "keep =>" (keep gastou-bastante? resumo)))

;keep => (
;gastou-bastante? 15
;gastou-bastante? 1
;true gastou-bastante? 10
;false gastou-bastante? 20
;true true)
; => res estranho, fora de ordem.

;----------------------------------------------------------------------------------------------------
; Investigaçoes

(println (take 2 (range 100)))
;(0 1)

(let [sequencia (range 1000000)]
  (println (take 2 sequencia))
  (println (take 2 sequencia)))
;(0 1)
;(0 1)
; => a sequencia não está sendo gulosa - EAGER
; => está sendo preguiçosa - LAZY

;----------------------------------------------------------------------------------------------------

(defn filtro1 [x]
  (println "filtro1" x)
  x)

(defn filtro2 [x]
  (println "filtro2" x)
  x)

(->> (range 50)
     (map filtro1)
     (map filtro2)
     println)

; => RES c/ CHUNKS DE 32 - MAP é LAZY (c/ uma mescla de EAGER)

;----------------------------------------------------------------------------------------------------