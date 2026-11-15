import sys
draft, target = sys.argv[1], sys.argv[2]
src = open(draft).read().split('\n')
assert src[0].startswith('# ')
title = src[0][2:].strip().replace("'", "''")
body = '\n'.join(src[1:]).lstrip('\n')
old = open(target).read()
fm_end = old.index('\n---\n', 4) + 5
fm = old[:fm_end]
fm = '\n'.join((f"title: '{title}'" if l.startswith('title:') else l) for l in fm.split('\n'))
open(target, 'w').write(fm + '\n' + body)
