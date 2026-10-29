---
title: 'Time travel in git: recovering a screwed-up repository with reflog and reset'
date: oct 25, 2026
description: >
    Screwed a rebase or force-pushed by mistake? Learn how to use git reflog and reset to travel 
    back in time and recover your repository without starting over.
authors:
    -   name: rafael
        avatar: https://rafael.tonholo.dev/images/writing.jpg
tags:
    - git
    - rebasing
    - reflog
    - version-control
thumbnail: https://rafael.tonholo.dev/images/articles/git-time-travel-reflog-reset/thumbnail.png
---

## Context

It is very common, when working with **git**, to screw up the repository. When the repository uses rebase, that can
happen even more frequently, which, in the end, leaves many developers afraid of rebasing.

## How things can go wrong

There are many possible reasons to screw up your git repository — believe me, I have done that multiple times — and
sometimes recovering from it can seem impossible. Some of the possibilities are:

- You have just rebased your feature branch onto main, many conflicts popped up, and you accidentally removed code that
  shouldn't be removed
- You accidentally force-pushed your changes to remote
- Or maybe your AI agent went rogue and messed up everything, then force-pushed it; yeah, I know, it happens.

And let’s say you were working on a very important feature or a bug fix. Now what, start from zero again? Well, what if
I told you there is a way to time travel in your repository?

## Recovering a previous repository state

Git has an awesome command called `reflog`, which is often forgotten, and it will help us time travel in our repository!

### Understanding `git reflog`

Let's see what the [git documentation](https://git-scm.com/docs/git-reflog) tells us about this awesome command:

> Reference logs, or "reflogs", record when the tips of branches and other references were updated in the local
> repository. Reflogs are useful in various Git commands, to specify the old value of a reference.

When executing the `git reflog` command, git will output something such as:

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

The output can be described as `<commit-hash> HEAD@{n}: <command>: <description>`, where:

- `<commit-hash>` is the commit `HEAD` pointed to after that operation
- `HEAD@{n}` represents where `HEAD` used to be `n` moves ago
- `<command>` is the git command you have executed
- `<description>` describes what the repository is doing, such as moving from a branch to another, or the message from
  the last commit present in the given `HEAD`

Ok, but that only shows us the moves the repository has made; basically, this is just a log. Why does that matter, and
how would it help us fix a screwed-up repository?

## Time-travelling your repository

Remember when I talked about time travel? To recover the repository state, for example, after a bad merge, we first need
to know where we need to move. That is why we use `reflog` at all: to learn which “portal to choose to travel back in
time”.

Let's take the previous `reflog` as an example. Let's say that after I finished my rebase, I realized that many files
I'd included were missing from the codebase, and I needed to undo the rebase without redoing everything. How do we do
that?

First, we need to identify where the "portal" is, or the repository `HEAD` pointer if you didn't like the time-travel
analogy, where the repository was before the rebase started:

```bash
$ git reflog
 
438fd80b (HEAD -> feat/274/cli-remote-flags) HEAD@{0}: rebase (finish): returning to refs/heads/feat/274/cli-remote-flags
...
508d3573 HEAD@{8}: rebase (start): checkout f1534f868ae23d2309f8160c0ad43f0b12107d86
7922af70 HEAD@{9}: reset: moving to 7922af70e858c6865d4058b0c76343aff0137003
...
```

`HEAD@{9}` it is!

> [!WARNING]
> Even though we're using it to recover something, `git reset --hard` is a destructive command: it discards any
uncommitted changes. Only run it on a CLEAN working tree. If you have uncommitted changes, `git stash` them first.
>
> In case you have stashed it, make sure to check the `reflog` again as the `stash` command will create a new `reflog`
entry.

Now, we just tell the repository to move to that head by simply:

```bash
$ git reset --hard HEAD@{9}
```

Once the command is executed, git will move your repository to the given `HEAD`, where it used to be 9 moves ago, and
the rebase was just undone. We can even see that operation in the `reflog` again!

```bash
$ git reflog
 
7922af70 (HEAD -> feat/274/cli-remote-flags) HEAD@{0}: reset: moving to HEAD@{9}
438fd80b HEAD@{1}: rebase (finish): returning to refs/heads/feat/274/cli-remote-flags
...
508d3573 HEAD@{9}: rebase (start): checkout f1534f868ae23d2309f8160c0ad43f0b12107d86
7922af70 HEAD@{10}: reset: moving to 7922af70e858c6865d4058b0c76343aff0137003
...
```

See that:

- The repository now has a new `HEAD@{0}`, with the `reset` command executed and the description of `moving to HEAD@{9}`
- The `HEAD@{0}` commit hash is the same as the previous `HEAD@{9}`, which is now the `HEAD@{10}`

Now, you can do the rebase once again, making sure all your changes are preserved!

### What if I've already force-pushed?

Remember that accidental force-push scenario from earlier? The good news is that if your repository still has the
original commits, you could use the same steps we performed here to recover the previous branch state!

However, we'll need one extra step. Since `reflog` and `reset` are local-only operations, we need to somehow sync our
newly recovered local branch with the remote.

As the local and remote branches now diverge, we will need to perform a forced `push` operation to make sure they are
synced.

If you've verified the recovered history and confirmed that nobody else's work will be overwritten, you can use:

```bash
$ git push origin <branch> --force-with-lease
```

Unlike a regular `--force` push, `--force-with-lease` provides an additional safeguard against overwriting unexpected
remote changes.

> [!IMPORTANT]
> BEFORE executing the above command, make sure you have coordinated with your team. A force push will overwrite the
remote branch, and recovering it will be much harder than recovering your local branch

## A few things worth remembering

As awesome as `reflog` is, there are some limitations:

- Its records belong to your local repository, meaning you won't see any remote operation logs.
- The `reflog`s entries are not permanent and can eventually expire (about 90 days for reachable entries, 30 for
  unreachable) and cleaned up by git, so don't count on “traveling too far back in time”.
- Don’t rely on it as a backup. The `reflog` helps to recover commits and reference history, but it can't reliably
  restore uncommitted changes discarded by destructive operations.

One more tip: if you're still in the middle of a `rebase` and just want to cancel it, you can usually use
`git rebase --abort` instead. The `reflog` command becomes particularly useful when the operation has already finished.

## Conclusion

In this article, you have learned how to recover a repository state by using both the `reflog` and `reset` commands.
With these commands, you can easily undo destructive operations or bad merges/rebases without starting over.

Of course, recovery isn't guaranteed in every situation, especially when uncommitted changes have been discarded or old
git objects have been deleted.

But the next time you find yourself staring at a completely messed-up repository, remember: before giving up and
starting over, check your `reflog` and travel back in time!

Thank you for reading this far. I hope you enjoyed this article!
