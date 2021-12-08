# Datomic

A licença Datomic Starter, ou Datomic Pro, requer registro de conta em [My Datomic](https://my.datomic.com/account).

Depois de ter uma chave de licença, você precisa configurar um arquivo transactor.properties:

Copie config/samples/dev-transactor.properties em algum lugar localmente
(em ```/datomic-pro-1.06344/config/```, por exemplo), e edite-o com a chave de licença recebida via email:
```
license-key=<INSERT HERE>
```
Inicie o transactor localmente:
```
cd datomic-pro-1.0.6344
bin/transactor config/dev-transactor.properties
=> System started
```

### Banco de Dados

Em um novo projeto, configure :repositories no arquivo ```project.clj```:
```
:repositories {"my.datomic.com" {:url "https://my.datomic.com/repo"
                                 :username "nome@email.com.br"
                                 :password "146asdjft-1489d-sod5-fsd5-14fjar4784td"}}
```

Criando um banco de dados local, a partir do Clojure REPL, usando a API Datomic Peer:
```
bin/repl

(require '[datomic.api :as d])
(def db-uri "datomic:dev://localhost:4334/hello")
(d/create-database db-uri)
```

Code demo:
```clojure
(ns ecommerce.core
  (:use clojure.pprint)
  (:require [datomic.api :as d]))

(def db-uri "datomic:dev://localhost:4334/hello")

(pprint (d/create-database db-uri))
;true

(def conn (d/connect db-uri))
(pprint conn) 
; #object[datomic.peer.Connection 0x4dae2cb6 "{
; :unsent-updates-queue 0,
; :pending-txes 0,
; :next-t 1000,
; :basis-t 66,
; :index-rev 0,
; :db-id \"hello-28659635-2298-48b1-b49a-e848922834d9\
; "}"]

(pprint (d/delete-database db-uri))
;true
```

### Documentação completa
https://docs.datomic.com/on-prem/getting-started/dev-setup.html