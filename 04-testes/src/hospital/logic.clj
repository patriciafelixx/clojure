(ns hospital.logic
  (:require [hospital.model :as h.model]
            [schema.core :as s]))

;(defn cabe-na-fila?
;  ; existe um problema de condicional quando o departamento não existe
;  [hospital departamento]
;  (-> hospital
;      departamento
;      count
;      (< 5)))
;
;(defn cabe-na-fila?
;  ; funciona para o caso de não ter departamento
;  [hospital departamento]
;  (if-let [fila (get hospital departamento)]
;    (-> fila
;        count
;        (< 5))))

;(defn cabe-na-fila?
;  ; também funciona, porém utilizando some threading
;  ; menos explicito. desvantagem: qualquer um der nil, devolve nil.
;  [hospital departamento]
;  (some-> hospital
;          departamento
;          count
;          (< 5)))
;
;; --------------------------------------------------------------------------------
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (update hospital departamento conj pessoa))
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    ;(throw (ex-info "Não cabe ninguém na fila deste departamento, ou departamento não existe." {:paciente pessoa}))
;    ;(throw (IllegalStateException. "Não cabe ninguém na fila deste departamento, ou departamento não existe."))
;    ))
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)
;    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa, :tipo :impossivel-colocar-pessoa-na-fila}))))
;
;; --------------------------------------------------------------------------------
;
;(defn- tenta-colocar-na-fila
;  [hospital departamento pessoa]
;  (if (cabe-na-fila? hospital departamento)
;    (update hospital departamento conj pessoa)))
;
;(defn chega-em
;  [hospital departamento pessoa]
;  (if-let [novo-hospital (tenta-colocar-na-fila hospital departamento pessoa)]
;    { :hospital novo-hospital, :resultado :sucesso}
;    { :hospital hospital, :resultado :impossivel-colocar-pessoa-na-fila}))
;
;; --------------------------------------------------------------------------------

(defn cabe-na-fila?
  [hospital departamento]
  (if-let [fila (get hospital departamento)]
    (-> fila
        count
        (< 5))))

(defn chega-em
  [hospital departamento pessoa]
  (if (cabe-na-fila? hospital departamento)
    (update hospital departamento conj pessoa)
    (throw (ex-info "Não cabe ninguém neste departamento" {:paciente pessoa}))))

(s/defn atende :- h.model/Hospital
  [hospital :- h.model/Hospital departamento :- s/Keyword]
  (update hospital departamento pop))

(s/defn proxima :- h.model/Paciente
  "Retorna o próximo paciente da fila"
  [hospital :- h.model/Hospital departamento :- s/Keyword]
  (-> hospital
      departamento
      peek))

(defn mesmo-tamanho? [hospital, outro-hospital, de, para]
  (= (+ (count (get outro-hospital de)) (count (get outro-hospital para)))
     (+ (count (get hospital de)) (count (get hospital para)))))

(s/defn transfere :- h.model/Hospital
  "Transfere o próximo paciente da fila 'de' para a fila 'para'"
  [hospital :- h.model/Hospital, de :- s/Keyword, para :- s/Keyword]
  {:pre  [(contains? hospital de), (contains? hospital para)]
   :post [(mesmo-tamanho? hospital % de para)]}
  (let [pessoa (proxima hospital de)]
    (-> hospital
        (atende de)
        (chega-em para pessoa))))

; ---------------------------------------------------------------------------------