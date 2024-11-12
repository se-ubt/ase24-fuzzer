# Simple lexical fuzzer

The fuzzer generates (configurable) random input strings and sends them to command line programs,
monitoring their output and exit codes.

## Compile unsafe program

```shell
gcc unsafeprogram.c -o unsafeprogram -Wno-deprecated-declarations
```

## Run fuzzer

```shell
java --enable-preview --source 21 Fuzzer.java
```
