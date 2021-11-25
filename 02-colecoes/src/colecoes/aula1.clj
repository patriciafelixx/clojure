(ns colecoes.aula1)

;----------------------------------------------------------------------------------------------------
; FIRST, REST & NEXT

(map println ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"])

(println (first ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;daniela
;guilherme
;carlos
;paulo
;lucia
;ana

(println (rest ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;(guilherme carlos paulo lucia ana)

(println (next ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"]))
;(guilherme carlos paulo lucia ana)

(println (rest []))
;()

(println (next []))
;nil

;----------------------------------------------------------------------------------------------------
; IMPLEMENTANDO MEU MAP

(defn meu-map [funcao sequencia]
  (let [primeiro (first sequencia)]
    ;(if primeiro
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (meu-map funcao (rest sequencia))))))

(meu-map println ["daniela", "guilherme", "carlos", "paulo", "lucia", "ana"])

;----------------------------------------------------------------------------------------------------
; TAIL RECURSION (RECURSÃO DE CAUDA)

;(meu-map println (range 100000)) - erro devido "pilha de execução"
; entao altera-se 'meu-map' por 'recur', informando a recursividade ao programa
; recur só pode aparecer como retorno da função, isto é, na cauda

(defn meu-map [funcao sequencia]
  (let [primeiro (first sequencia)]
    (if (not (nil? primeiro))
      (do
        (funcao primeiro)
        (recur funcao (rest sequencia))))))

(meu-map println (range 10000))

;;----------------------------------------------------------------------------------------------------