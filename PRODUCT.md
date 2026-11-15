# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

Two audiences with equal weight:

- Senior engineers and peers evaluating craft through articles, code samples and technical depth. They come for credibility, referrals and open-source collaboration, usually on a desktop browser in the evening.
- Hiring managers and recruiters scanning for role fit, recent work, impact and a way to get in touch. They come during work hours, aiming for an intro call or an offer.

Links to the site get shared in Slack and DMs, so some visits start on a phone.

## Product Purpose

The personal site of Rafael Tonholo, a senior Android engineer based in Halifax, Nova Scotia, open to remote work. It shows who he is, the production work he has shipped, and how he thinks, through case studies, long-form articles and a resume. Success is a peer trusting the depth of the writing, and a hiring manager quickly seeing role fit and a path to contact.

## Positioning

- The site is handmade in Kotlin Multiplatform with Kobweb, so it shows the stack instead of describing it.
- Articles are long-form, tested and sourced: runnable samples, results measured on real devices, and cited sources.
- The case studies come from senior Android work in production at Thunderbird, Bleacher Report and Pinterest.
- Every page and article ships in English (en-CA) and Brazilian Portuguese (pt-BR).

## Operating Context

- Routes: home, about, projects with one page per case study, articles with one page per article, and resume.
- Articles are written in Markdown and rendered by Kobweb's markdown pipeline, with custom handlers for code, callouts, links, headings, images, footnotes and Mermaid diagrams.
- Comments and reactions on articles go through Giscus (GitHub Discussions).
- Readers can share an article or link to a single section.

## Capabilities and Constraints

- Kotlin Multiplatform, JS target only, in a single `:site` Kobweb module.
- Static export deployed to GitHub Pages; there is no server.
- Light and dark modes are both first-class and follow the OS preference; neither is the canonical look.
- Tooling stays privacy-light: Umami analytics and Giscus comments, nothing heavier.
- UI strings are localized through a hand-rolled `Strings` sealed class, one subclass per locale.

## Brand Commitments

- Name and domain: rafael.tonholo.dev.
- Personality: precise, opinionated, handmade. Speaks the language of engineers without developer clichés, and aims for trust, curiosity and respect for craft rather than delight or playfulness.
- Article voice follows the hand-written Hello World and Git Time Travel articles: casual and conversational, explains real output, uses plain English.

## Evidence on Hand

- Case studies: Thunderbird, Bleacher Report and Pinterest, each in English and pt-BR (`site/src/jsMain/resources/markdown/projects/`).
- Articles: Hello World, Git command line, Git time travel with reflog and reset, LogD composable text, counting bytes, and the A2UI on-device article in progress (`site/src/jsMain/resources/markdown/articles/`).
- A resume page.
- No testimonials, client quotes, metrics dashboards or press coverage exist; future work must not invent them.

## Product Principles

1. Show, don't claim: the site, its code and its articles are the proof of skill.
2. Serve both audiences on every page: depth for peers, fast role fit for hiring managers.
3. Every claim is sourced, and every number comes from a real run.
4. Both languages and both themes get the same care.
5. Earn every element: nothing decorative that isn't doing work.
