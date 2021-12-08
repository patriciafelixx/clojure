# Material Complementar

- [Entidades pré-transacionadas do schema](#entidades-pré-transacionadas-do-schema)
- [Transações Atômicas com Datomic](#transações-atômicas-com-datomic)


## Entidades pré-transacionadas do Schema

Ao transacionar o Schema com (d/transact conn db/schema) obtivemos:

```
#datom [72 10 :produto/nome 13194139534312 true]
#datom [72 40 23 13194139534312 true]
#datom [72 41 35 13194139534312 true]
#datom [72 62 "O nome de um produto" 13194139534312 true]
#datom [73 10 :produto/slug 13194139534312 true]
#datom [73 40 23 13194139534312 true]
#datom [73 41 35 13194139534312 true]
#datom [73 62 "O caminho para acessar esse produto via http 13194139534312 true]
```

Vamos lembrar da estrutura de um datom no Datomic:

```
#datom [id-da-entidade atributo valor id-da-tx added?]
```

Note no primeiro datom que, ao transacionarmos o atributo :db/ident com o valor :produto/nome, o atributo aparece como

10.

Esse valor 10 é o ID da entidade que representa o atributo pré-definido :db/ident no Datomic.

Os atributos que utilizamos para definir um Schema no Datomic são eles próprios entidades que já vem transacionadas no
próprio Datomic.

Analisando a resposta que obtivemos do d/transact, podemos verificar os IDs das entidades de algumas dessas entidades
pré-transacionadas:

* :db/ident é 10
* :db/valueType é 40 e :db.type/string é 23
* :db/cardinality é 41 e :db.cardinality/one é 35
* :db/doc é 62

Para mais informações, acesse: https://docs.datomic.com/cloud/schema/schema-reference.html

## Transações Atômicas com Datomic

Vamos dizer que temos o seguinte código:

```clojure
(let [computador (model/novo-produto "Computador Novo", "/computador_novo", 2500.10M)
      celular (model/novo-produto "Celular Caro", "/celular", 888888.10M)
      calculadora {:produto/nome "Calculadora com 4 operações"}
      celular-barato (model/novo-produto "Celular Barato", "/celular-barato", nil)]
  (d/transact conn [computador celular calculadora celular-barato]))
```

Estamos transacionando para o Datomic 4 produtos ao mesmo tempo.

Note que for o celular-barato está com o preço nulo.

Ao executarmos o código anterior, obtemos o seguinte erro:

```
Execution error (Exceptions$IllegalArgumentExceptionInfo) at datomic.error/arg (error.clj:79).
:db.error/nil-value Nil is not a legal value
```

O Datomic não aceita valores nulos. Por isso obtivemos um erro.

OK, mas apenas o último produto, o celular-barato, teve um valor nulo. O que será que aconteceu com os outros produtos?
Foram inseridos ou não?

Considerando que o Datomic não tinha nenhum produto antes dessa transação, podemos verificar os produtos depois da
execução com a consulta:

```clojure
(pprint (db/todos-os-produtos) (d/db conn))
```

O resultado seria um conjunto vazio:

```
#{}
```

Ou seja, ao transacionarmos 4 produtos ao mesmo tempo, se ocorrer algum erro no último, a transação toda é cancelada.

Essa característica transacional de fazer "ou tudo ou nada" é chamada de Atomicidade: se uma parte da transação falhar,
a transação toda falha e nenhuma mudança é feita no BD.

Saiba mais sobre outras características transacionais do Datomic em: https://docs.datomic.com/on-prem/acid.html