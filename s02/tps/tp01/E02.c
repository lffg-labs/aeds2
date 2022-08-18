#include <stdio.h>
#include <string.h>

int is_palindrome(char *str, int len) {
    for (int i = 0; i < len / 2; i++) {
        if (str[i] != str[len - (1 + i)]) {
            return 0;
        }
    }
    return 1;
}

char *read_string(char *buf, int cap, int *len) {
    char *str = fgets(buf, cap, stdin);
    if (str == NULL) {
        return NULL;
    }

    int orig_len  = strcspn(str, "\r\n");
    str[orig_len] = 0;  // Remove trailing new line.
    *len          = orig_len;

    return str;
}

int main() {
    char str[4096];
    int len = 0;

    while (read_string(str, sizeof(str), &len) != NULL) {
        if (strcmp(str, "FIM") == 0) {
            break;
        }
        if (is_palindrome(str, len)) {
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }
    }

    return 0;
}
