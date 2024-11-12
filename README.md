# Simple lexical fuzzer

The fuzzer generates (configurable) random input strings and sends them to command line programs,
monitoring their output and exit codes.

## Compile unsafe program

```shell
gcc program_unsafe.c -o program_unsafe -Wno-deprecated-declarations
```

## Run fuzzer

```shell
java Fuzzer.java
```

## Compile coverage program

```shell
gcc program_coverage.c -o program_coverage --coverage
```

This yields a file `program_coverage.gcno`.

## Run/fuzz program

```shell
java Fuzzer.java
```

This yields a file `program_coverage.gcda`

## Use gcov to create coverage report

```shell
gcov program_coverage.c
```

The coverage report is then available in file `program_coverage.c.gcov`.
