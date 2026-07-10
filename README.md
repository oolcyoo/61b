# CS 61B Coursework Archive

This repository is a cleaned archive of my CS 61B coursework. I keep it as a reference for reviewing data structures, Java implementation patterns, tests, and the larger course projects.

The original local folder also contained course software and review videos. Those files are intentionally not part of this GitHub version because they are not my coursework and made the folder several gigabytes larger than the actual source code.

## What's Included

- `hw0`-`hw7`: homework exercises covering Java basics, arrays/lists, inheritance, regex, packed integers, sets, sorting, and algorithm practice.
- `lab1`-`lab13`: lab exercises covering debugging, lists, table filters, maps, heaps, persistence, and project design notes.
- `proj0`: Blocks puzzle project.
- `proj1`: Enigma simulator.
- `proj2`: Ataxx game project.
- `proj3`: Gitlet, a small Git-like version-control system.

## Cleanup Notes

For the public archive, I removed local-only course metadata and generated state:

- beacon token files
- partner files
- local IDE/build output
- generated `.capers` and `.gitlet` runtime state
- lecture/review videos from the outer `MT2` folder
- Berkeley course tooling from the outer `cs61b-software` folder

I also finished two small incomplete spots I found while preparing the archive:

- `hw5/Nybbles.java`
- `hw7/Sum.java`

## Running Code

Most folders have their own `Makefile`. From an assignment folder, try:

```sh
make
make check
```

Some tests depend on the CS 61B Java libraries and course tooling. On macOS, Java compilation may also require accepting the Xcode command line tools license first.

## Status

This is an archive of coursework, not a polished library. Some files still contain starter-code comments from the course, and a few assignments may be partial. I keep those parts visible because they show the real shape of the class work and make it easier to revisit later.

Please use this repository for study, comparison, and review. Do not submit this work as your own coursework.
