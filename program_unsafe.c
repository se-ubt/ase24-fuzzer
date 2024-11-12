#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    char array[16];
    printf("Enter a string (up to 16 characters): ");
    gets(array);  // WARNING: gets() is unsafe and allows buffer overflows!
    printf("You entered: %s\n", array);
    return 0;
}
