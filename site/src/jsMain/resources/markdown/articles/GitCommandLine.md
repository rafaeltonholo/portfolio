---
title: Git command-line
date: oct 09, 2024
description: Essential Git command-line features, tips, and workflows for everyday development, from multi-account SSH configs to log formatting and branch hygiene.
tags:
  - git
  - tooling
  - cli
readTime: 5 min
---

Git is a free, open-source distributed version control system that helps us organize and manage projects easily and efficiently.

It is by far the most widely used SCM tool for handling small and large projects alike. Because Git has so many capabilities, it can be easy to overlook or forget some of its handiest everyday features.

Here is a practical guide to commands and configurations that are especially useful to keep in your toolkit.

## Cloning into a specific folder

By default, Git creates a folder named after the remote repository:

```bash
git clone <remote-url>/repository.git
```

If you want the local folder to have a different name, pass the target folder name as the final argument:

```bash
git clone <remote-url>/repository.git my-custom-folder
```

In that case, the repository will be placed inside a folder named `my-custom-folder`.

## Using different GitHub accounts (SSH keys)

When balancing client, work, and personal projects, you often need to use separate GitHub accounts without re-authenticating on every push.

SSH host aliases make this seamless. When you generate an SSH key (for example, with `ed25519`), your default `~/.ssh/config` file usually looks like this:

```
Host github.com
  User git
  IdentityFile ~/.ssh/id_ed25519
```

To configure a second account, add an alias block pointing to the secondary SSH key:

```
# Default personal account
Host github.com
  User git
  IdentityFile ~/.ssh/id_ed25519

# Secondary / work account
Host secondary-github.com
  HostName github.com
  User git
  IdentityFile ~/.ssh/secondary_account_id_ed25519
```

With `HostName github.com`, Git routes the connection to `github.com` while using the `secondary_account_id_ed25519` key whenever the hostname `secondary-github.com` is used in a remote URL:

```bash
# Uses default SSH key:
git clone git@github.com:rafaeltonholo/portfolio.git

# Uses secondary SSH key:
git clone git@secondary-github.com:rafaeltonholo/portfolio.git
```

This trick also works for GitLab, Bitbucket, and self-hosted instances.

## Cleaning up branches deleted on remote

When pull requests merge and remote branches are deleted, your local repository retains references to them. You can prune deleted remote-tracking branches and remove the matching local branches in one command:

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

### How this works:

1. `git fetch -p`: Prunes remote-tracking references that no longer exist on the remote.
2. `git branch -vv`: Lists local branches with tracking details; deleted tracking branches are flagged with `: gone`.
3. `grep ': gone'`: Filters down to branches whose upstreams are gone.
4. `grep -v "^\*"`: Excludes the currently checked-out branch.
5. `grep -v "^\+"` and `grep -v "^- "`: Trims worktree indicator lines.
6. `awk '/: gone]/{print $1}'`: Extracts only the branch name.
7. `xargs git branch -D`: Deletes each pruned branch locally.

## Prettier, more concise git logs

Running plain `git log` can be overwhelming when you just want a quick overview of recent history:

```bash
$ git log

commit 4bb2b59ec0ac4ed853c586ba29f0c0a5d94598da (HEAD -> main, origin/main)
Author: Rafael Tonholo <rafael@tonholo.dev>
Date:   Thu Oct 3 18:20:41 2024 -0300

    ci: deploy resume changes
```

Git provides several flags to format logs more cleanly:

```bash
git log --graph --oneline --decorate --all
```

Useful flags to combine:
- `--oneline`: Condenses each commit to its abbreviated hash and title line.
- `--graph`: Draws an ASCII branch and merge tree.
- `--decorate`: Shows branch and tag pointers.
- `--stat`: Adds diffstat summaries per commit.
- `--author="Name"`: Filters commits by author.

You can alias this in `~/.gitconfig` for quick access:

```ini
[alias]
    lg = log --graph --pretty=format:'%Cred%h%Creset -%C(yellow)%d%Creset %s %Cgreen(%cr) %C(bold blue)<%an>%Creset' --abbrev-commit
```

## Upcoming topics to explore

Stay tuned for upcoming deep-dives covering:
- Amending commits cleanly without duplicate history
- Interactive rebasing and squash strategies
- When to merge vs. when to rebase
- Git submodules: setup, removal, pros, and cons
- Trunk-based development vs. Git-flow in production teams
