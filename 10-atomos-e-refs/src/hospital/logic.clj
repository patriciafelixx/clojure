(ns hospital.logic
  (:use [clojure.pprint]))

(defn cabe-na-fila? [hospital fila]
  ;(let [tamanho-atual (count (get hospital fila))]
  ;  (< tamanho-atual 5))
  (-> hospital
      (get fila)
      count
      (< 5)))

(defn chega-em [hospital fila pessoa]
  (if (cabe-na-fila? hospital fila)
    (update hospital fila conj pessoa)
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

(defn chega-em-pausado [hospital fila pessoa]
  (println "tentando adicionar a pessoa" pessoa)
  (Thread/sleep (* (rand) 2000))
  (if (cabe-na-fila? hospital fila)
    (do (println "efetuando update..." pessoa)
        (update hospital fila conj pessoa))
    (throw (ex-info "Fila já está cheia!" {:tentando-adicionar pessoa}))))

(defn atende [hospital fila]
  (update hospital fila pop))

(defn proxima [hospital fila]
  (-> hospital
      fila
      peek))

(defn transfere [hospital fila-de fila-para]
  (let [pessoa (proxima hospital fila-de)]
    (-> hospital
        (atende fila-de)
        (chega-em fila-para pessoa))))

; -----------------------------------------------------------------------------------------------------
(defn atende-completo [hospital fila]
  {:paciente (update hospital fila peek)
   :hospital (update hospital fila pop)})

(defn atende-completo-que-chama-ambos [hospital fila]
  (let [get-fila (get hospital fila)
        peek-pop (juxt peek pop)
        [pessoa fila-atualizada] (peek-pop get-fila)
        hospital-atualizado (update hospital assoc fila fila-atualizada)]
    {:paciente pessoa
     :hospital hospital-atualizado}))

; -----------------------------------------------------------------------------------------------------