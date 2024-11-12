#include <stdio.h>
#include <string.h>

int main(int argc, char *argv[]) {
    char array[16];
    printf("Enter a string (up to 16 characters): ");
    fgets(array, 16, stdin);
    int length = strlen(array);

    printf("Input length: %i\n", length);
    printf("You entered: %s\n", array);

    if (length > 4 && length <= 6) {
        return 1; // issue
    }

    return 0;
}
