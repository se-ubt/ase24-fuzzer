# Simple lexical fuzzer

The fuzzer generates (configurable) random input strings and sends them to command line programs,
monitoring their output and exit codes.

## Note for Windows users

If you're using WSL on Windows to work on the assignment, make sure that Git does not replace the Unix line endings with CRLF.
You can configure git to preserve the line endings from the remote repository:

```shell
git config --local core.autocrlf input
git config --local core.safecrlf true
```

Line ending normalization for text files is already enabled via [.gitattributes](https://github.com/se-ubt/ase24-fuzzer/blob/main/.gitattributes).

If you're still getting errors caused by the line endings, you can try the following (after configuring Git as outlined above):

```shell
git add --update --renormalize
git checkout .
```

If that doesn't work, you can also exclicity convert all text files to use Unix line endings:

```shell
sudo apt-get install dos2unix
find . -type f -not -path "./.git/*" -exec dos2unix {} \;
git commit -a -m 'Convert line endings dos2unix'
```

## Compile unsafe program

```shell
gcc program_unsafe.c -o program_unsafe -Wno-deprecated-declarations
```

## Run fuzzer

```shell
java Fuzzer.java "./program_unsafe"
```

## Compile coverage program

```shell
gcc program_coverage.c -o program_coverage --coverage
```

This yields a file `program_coverage.gcno`.

## Run fuzzer

```shell
java Fuzzer.java "./program_coverage"
```

This yields a file `program_coverage.gcda`

## Use gcov to create coverage report

Line coverage:

```shell
gcov program_coverage.c
```

Branch coverage:

```shell
gcov -b program_coverage.c
```

In both cases, the coverage report is then available in file `program_coverage.c.gcov`.
