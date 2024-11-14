# Simple lexical fuzzer

The fuzzer generates (configurable) random input strings and sends them to command line programs,
monitoring their output and exit codes.

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
