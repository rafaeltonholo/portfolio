---
routeOverride: /articles/pt-br/git-time-travel-reflog-reset
slug: git-time-travel-reflog-reset
locale: pt-BR
title: 'Viagem no tempo no git: recuperando um repositório quebrado com reflog e reset'
date: 25 de out. de 2026
description: Fez besteira no rebase ou deu force-push por engano? Aprenda a usar o git reflog e reset para viajar no tempo e recuperar seu repositório sem precisar recomeçar do zero.
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
tags:
    - git
    - rebasing
    - reflog
    - version-control
---

## Contexto

É muito comum, ao trabalhar com o **git**, quebrar o repositório. Quando o repositório adota rebase, isso pode acontecer
com ainda mais frequência, o que acaba deixando muitos desenvolvedores com medo de usar rebase.

## Como as coisas podem dar errado

Existem muitos motivos possíveis para bagunçar o seu repositório git — acredite em mim, já fiz isso várias vezes — e às
vezes recuperar o estado anterior pode parecer impossível. Algumas das possibilidades são:

- Você acabou de fazer o rebase da sua feature branch na main, muitos conflitos surgiram e você acidentalmente removeu
  código que não deveria ter sido removido
- Você acidentalmente deu force-push nas suas alterações para o repositório remoto
- Ou talvez o seu agente de IA tenha saído do controle e bagunçado tudo, depois dando force-push; sim, eu sei, acontece.

E digamos que você estivesse trabalhando em uma feature muito importante ou na correção de um bug crítico. E agora,
recomeçar do zero? E se eu te dissesse que existe uma maneira de viajar no tempo no seu repositório?

## Recuperando um estado anterior do repositório

O Git possui um comando incrível chamado `reflog`, que muitas vezes é esquecido, e ele vai nos ajudar a viajar no tempo
no nosso repositório!

### Entendendo o `git reflog`

Vamos ver o que a [documentação do git](https://git-scm.com/docs/git-reflog) nos diz sobre esse comando incrível:

> Registros de referência, ou "reflogs", registram quando as pontas das branches e outras referências foram atualizadas
> no repositório local. Reflogs são úteis em vários comandos do Git para especificar o valor antigo de uma referência.

Ao executar o comando `git reflog`, o git exibirá algo como:

```bash
$ git reflog

438fd80b (HEAD -> feat/274/cli-remote-flags) HEAD@{0}: rebase (finish): returning to refs/heads/feat/274/cli-remote-flags
438fd80b HEAD@{1}: rebase (pick): style(remote): apply formatter to remote bindings
1ec8e910 HEAD@{2}: rebase (continue): fix(cli): reuse cached downloads only when servers confirm they are unchanged
9da9178a HEAD@{3}: rebase (pick): fix(cli): allow cache maintenance flags to run without conversion options
d2706b60 HEAD@{4}: rebase (pick): test(cli): add remote source integration tests against a live server
d63e4f7c HEAD@{5}: rebase (continue): feat(cli): add remote source flags with cache revalidation
8556a2d6 HEAD@{6}: rebase (reword): feat(cli): add remote source flags with cache revalidation
c695fc2a HEAD@{7}: rebase: fast-forward
508d3573 HEAD@{8}: rebase (start): checkout f1534f868ae23d2309f8160c0ad43f0b12107d86
7922af70 HEAD@{9}: reset: moving to 7922af70e858c6865d4058b0c76343aff0137003
...
eec690d3 HEAD@{18}: checkout: moving from main to feat/274/cli-remote-flags
```

A saída pode ser descrita como `<commit-hash> HEAD@{n}: <comando>: <descrição>`, onde:

- `<commit-hash>` é o commit para o qual `HEAD` apontou após aquela operação
- `HEAD@{n}` representa onde `HEAD` costumava estar `n` movimentações atrás
- `<comando>` é o comando git que você executou
- `<descrição>` descreve o que o repositório está fazendo, como mudar de uma branch para outra, ou a mensagem do último
  commit presente naquele dado `HEAD`

Ok, mas isso apenas nos mostra as movimentações que o repositório fez; basicamente, é só um log. Por que isso importa e
como nos ajudaria a consertar um repositório quebrado?

## Viajando no tempo no seu repositório

Lembra de quando falei sobre viagem no tempo? Para recuperar o estado do repositório, por exemplo, após um merge
malfeito, primeiro precisamos saber para onde precisamos nos mover. É exatamente por isso que usamos o `reflog`: para
descobrir qual "portal escolher para viajar de volta no tempo".

Vamos usar o `reflog` anterior como exemplo. Digamos que, após terminar meu rebase, percebi que muitos arquivos
incluídos estavam faltando na base de código e precisei desfazer o rebase sem ter que refazer tudo do zero. Como fazemos
isso?

Primeiro, precisamos identificar onde está o "portal" — ou o ponteiro `HEAD` do repositório, caso você não tenha gostado
da analogia de viagem no tempo — onde o repositório estava antes do rebase começar:

```bash
$ git reflog
 
438fd80b (HEAD -> feat/274/cli-remote-flags) HEAD@{0}: rebase (finish): returning to refs/heads/feat/274/cli-remote-flags
...
508d3573 HEAD@{8}: rebase (start): checkout f1534f868ae23d2309f8160c0ad43f0b12107d86
7922af70 HEAD@{9}: reset: moving to 7922af70e858c6865d4058b0c76343aff0137003
...
```

É o `HEAD@{9}`!

> [!WARNING]
> Mesmo que estejamos usando para recuperar algo, `git reset --hard` é um comando destrutivo: ele descarta quaisquer
alterações não commitadas. Execute-o apenas em uma working tree LIMPA. Se você tiver alterações pendentes, use
`git stash` nelas primeiro.
>
> Caso tenha feito stash, certifique-se de verificar o `reflog` novamente, pois o comando `stash` criará uma nova
entrada no `reflog`.

Agora, simplesmente dizemos ao repositório para se mover para aquele head:

```bash
$ git reset --hard HEAD@{9}
```

Uma vez executado o comando, o git moverá seu repositório para o `HEAD` indicado, onde ele costumava estar 9
movimentações atrás, e o rebase foi desfeito. Podemos até ver essa operação no `reflog` novamente!

```bash
$ git reflog
 
7922af70 (HEAD -> feat/274/cli-remote-flags) HEAD@{0}: reset: moving to HEAD@{9}
438fd80b HEAD@{1}: rebase (finish): returning to refs/heads/feat/274/cli-remote-flags
...
508d3573 HEAD@{9}: rebase (start): checkout f1534f868ae23d2309f8160c0ad43f0b12107d86
7922af70 HEAD@{10}: reset: moving to 7922af70e858c6865d4058b0c76343aff0137003
...
```

Veja que:

- O repositório agora tem um novo `HEAD@{0}`, com o comando `reset` executado e a descrição `moving to HEAD@{9}`
- O hash do commit de `HEAD@{0}` é o mesmo do `HEAD@{9}` anterior, que agora passou a ser o `HEAD@{10}`

Agora você pode fazer o rebase mais uma vez, garantindo que todas as suas alterações sejam preservadas!

### E se eu já tiver feito force-push?

Lembra daquele cenário de force-push acidental mencionado antes? A boa notícia é que, se o seu repositório ainda tiver
os commits originais, você pode usar os mesmos passos que realizamos aqui para recuperar o estado anterior da branch!

No entanto, precisaremos de um passo extra. Como `reflog` e `reset` são operações exclusivamente locais, precisamos
sincronizar nossa branch local recém-recuperada com o repositório remoto.

Como as branches local e remota agora divergiram, precisaremos realizar uma operação de `push` forçada para garantir que
elas fiquem sincronizadas.

Se você verificou o histórico recuperado e confirmou que o trabalho de ninguém mais será sobrescrito, você pode usar:

```bash
$ git push origin <branch> --force-with-lease
```

Diferente de um push `--force` tradicional, `--force-with-lease` oferece uma proteção adicional contra a sobrescrita de
alterações remotas inesperadas.

> [!IMPORTANT]
> ANTES de executar o comando acima, certifique-se de coordenar com sua equipe. Um force push sobrescreverá a branch
remota, e recuperá-la será muito mais difícil do que recuperar sua branch local.

## Algumas coisas que valem a pena lembrar

Por mais incrível que o `reflog` seja, existem algumas limitações:

- Seus registros pertencem ao seu repositório local, o que significa que você não verá nenhum log de operações remotas.
- As entradas do `reflog` não são permanentes e podem expirar (cerca de 90 dias para entradas alcançáveis, 30 para
  inalcançáveis) e serem limpas pelo git, então não conte com "viajar longe demais no tempo".
- Não confie nele como backup. O `reflog` ajuda a recuperar commits e histórico de referências, mas não consegue
  restaurar de forma confiável alterações não commitadas que foram descartadas por operações destrutivas.

Mais uma dica: se você ainda estiver no meio de um `rebase` e quiser apenas cancelá-lo, geralmente você pode usar
`git rebase --abort`. O comando `reflog` torna-se especialmente útil quando a operação já foi finalizada.

## Conclusão

Neste artigo, você aprendeu como recuperar o estado de um repositório usando os comandos `reflog` e `reset`. Com esses
comandos, você pode facilmente desfazer operações destrutivas ou merges/rebases ruins sem precisar recomeçar do zero.

É claro que a recuperação não é garantida em todas as situações, especialmente quando alterações não commitadas foram
descartadas ou objetos antigos do git foram excluídos pelo garbage collector.

Mas da próxima vez que você se encontrar olhando para um repositório completamente bagunçado, lembre-se: antes de
desistir e recomeçar do zero, confira seu `reflog` e viaje no tempo!

Obrigado por ler até aqui. Espero que tenha gostado do artigo!
