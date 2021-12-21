(ns hospital.aula6
  (:use [clojure pprint])
  (:require [hospital.model :as model]))

(defn cabe-na-fila? [fila]
  (< (count fila) 5))

(defn chega-em [fila pessoa]
  (if (cabe-na-fila? fila)
    (conj fila pessoa)
    (throw (ex-info "Fila cheia" {:tentando-adicionar pessoa}))))

(defn chega-em!
  "Troca de referencia via ref-set"
  [hospital pessoa]
  (let [fila-espera (get hospital :espera)]
    (ref-set fila-espera (chega-em @fila-espera pessoa))))

(defn chega-em!
  "Troca de referencia via alter"
  [hospital pessoa]
  (let [fila-espera (get hospital :espera)]
    (alter fila-espera chega-em pessoa)))

(defn simula-um-dia []
  (let [hospital {:espera       (ref model/fila-vazia)
                  :laboratorio1 (ref model/fila-vazia)
                  :laboratorio2 (ref model/fila-vazia)
                  :laboratorio3 (ref model/fila-vazia)}]
    (dosync
      (chega-em! hospital "guilherme")
      (chega-em! hospital "maria")
      (chega-em! hospital "lucia")
      (chega-em! hospital "daniela")
      ;(chega-em! hospital "paulo")
      (chega-em! hospital "ana"))
    (pprint hospital)))

(simula-um-dia)

; -----------------------------------------------------------------------------------------------------

(defn async-chega-em! [hospital pessoa]
  (future
    (Thread/sleep (rand 5000))
    (dosync
      (println "Tentando o codigo sincronizado" pessoa)
      (chega-em! hospital pessoa))))

(defn simula-um-dia-async []
  (let [hospital {:espera       (ref model/fila-vazia)
                  :laboratorio1 (ref model/fila-vazia)
                  :laboratorio2 (ref model/fila-vazia)
                  :laboratorio3 (ref model/fila-vazia)}]

    (def futures (mapv #(async-chega-em! hospital %) (range 10)))
    (future
      (dotimes [n 4]
        (Thread/sleep 2000)
        (pprint hospital)
        (pprint futures)))))

(simula-um-dia-async)

; -----------------------------------------------------------------------------------------------------