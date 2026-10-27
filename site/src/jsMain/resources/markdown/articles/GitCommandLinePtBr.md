---
routeOverride: /articles/pt-br/git-command-line
slug: git-command-line
locale: pt-BR
title: Linha de comando do Git
date: 09 de out. de 2024
description: Recursos essenciais da linha de comando do Git, dicas e fluxos de trabalho para o desenvolvimento diário, desde configurações de SSH com múltiplas contas até formatação de logs e higiene de branches.
tags:
  - git
  - tooling
  - cli
---

O Git é um sistema de controle de versão distribuído, gratuito e open source, que nos ajuda a organizar e gerenciar projetos com facilidade e eficiência.

É de longe a ferramenta de SCM mais utilizada para lidar tanto com pequenos projetos quanto com grandes bases de código. Como o Git possui tantos recursos, pode ser fácil ignorar ou esquecer algumas das suas funcionalidades mais úteis no dia a dia.

Aqui está um guia prático de comandos e configurações que são especialmente úteis para manter no seu kit de ferramentas.

## Clonando em uma pasta específica

Por padrão, o Git cria uma pasta com o mesmo nome do repositório remoto:

```bash
git clone <remote-url>/repository.git
```

Se quiser que a pasta local tenha um nome diferente, passe o nome da pasta de destino como último argumento:

```bash
git clone <remote-url>/repository.git minha-pasta-customizada
```

Nesse caso, o repositório será clonado dentro de uma pasta chamada `minha-pasta-customizada`.

## Usando contas diferentes do GitHub (chaves SSH)

Ao conciliar projetos de clientes, trabalho e projetos pessoais, você frequentemente precisa usar contas separadas do GitHub sem precisar se autenticar novamente a cada push.

Os aliases de host SSH tornam isso transparente. Quando você gera uma chave SSH (por exemplo, com `ed25519`), seu arquivo padrão `~/.ssh/config` geralmente se parece com isto:

```
Host github.com
  User git
  IdentityFile ~/.ssh/id_ed25519
```

Para configurar uma segunda conta, adicione um bloco de alias apontando para a chave SSH secundária:

```
# Conta pessoal padrão
Host github.com
  User git
  IdentityFile ~/.ssh/id_ed25519

# Conta secundária / profissional
Host secondary-github.com
  HostName github.com
  User git
  IdentityFile ~/.ssh/secondary_account_id_ed25519
```

Com `HostName github.com`, o Git roteia a conexão para `github.com` enquanto utiliza a chave `secondary_account_id_ed25519` sempre que o hostname `secondary-github.com` for usado em uma URL remota:

```bash
# Usa a chave SSH padrão:
git clone git@github.com:rafaeltonholo/portfolio.git

# Usa a chave SSH secundária:
git clone git@secondary-github.com:rafaeltonholo/portfolio.git
```

Esse truque também funciona para o GitLab, Bitbucket e instâncias auto-hospedadas.

## Limpando branches excluídas no remoto

Quando pull requests são mergeados e branches remotas são excluídas, seu repositório local ainda mantém referências a elas. Você pode podar as branches de rastreamento remoto excluídas e remover as branches locais correspondentes em um único comando:

```bash
git fetch -p
git branch -vv | \
    grep ': gone' | \
    grep -v "^\*" | \
    grep -v "^\+" | \
    grep -v "^- " | \
    awk '/: gone]/{print $1}' | \
    xargs git branch -D
```

### Como isso funciona:

1. `git fetch -p`: Poda referências de rastreamento remoto que não existem mais no remoto.
2. `git branch -vv`: Lista as branches locais com detalhes de rastreamento; branches cujo upstream foi removido são marcadas com `: gone`.
3. `grep ': gone'`: Filtra apenas as branches cujo upstream desapareceu.
4. `grep -v "^\*"`: Exclui a branch atualmente ativa (checkout atual).
5. `grep -v "^\+"` e `grep -v "^- "`: Remove linhas indicadoras de worktrees.
6. `awk '/: gone]/{print $1}'`: Extrai apenas o nome da branch.
7. `xargs git branch -D`: Exclui localmente cada uma das branches podadas.

## Logs do Git mais bonitos e concisos

Executar apenas `git log` pode ser intimidador quando você quer apenas uma visão geral rápida do histórico recente:

```bash
$ git log

commit 4bb2b59ec0ac4ed853c586ba29f0c0a5d94598da (HEAD -> main, origin/main)
Author: Rafael Tonholo <rafael@tonholo.dev>
Date:   Thu Oct 3 18:20:41 2024 -0300

    ci: deploy resume changes
```

O Git fornece diversas flags para formatar logs de forma muito mais limpa:

```bash
git log --graph --oneline --decorate --all
```

Flags úteis para combinar:
- `--oneline`: Condensa cada commit ao seu hash abreviado e título.
- `--graph`: Desenha uma árvore ASCII de branches e merges.
- `--decorate`: Exibe ponteiros de branches e tags.
- `--stat`: Adiciona resumos do diffstat por commit.
- `--author="Nome"`: Filtra commits por autor.

Você pode criar um alias para isso no `~/.gitconfig` para acesso rápido:

```ini
[alias]
    lg = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit
```

## Próximos tópicos para explorar

Fique ligado para futuros artigos detalhados cobrindo:
- Como fazer amend de commits de forma limpa sem duplicar histórico
- Estratégias de rebase interativo e squash
- Quando fazer merge vs. quando fazer rebase
- Git submodules: configuração, remoção, prós e contras
- Trunk-based development vs. Git-flow em equipes de produção
