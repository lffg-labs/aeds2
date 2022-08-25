#include <stdio.h>
#include <string.h>

int is_palindrome(char *str, int len) {
    if (len <= 1) {
        return 1;
    }
    return str[0] == str[len - 1] && is_palindrome(str + 1, len - 2);
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
    while (read_string(str, sizeof(str), &len)) {
        if (strcmp(str, "FIM") == 0) {
            break;
        }
        int a = is_palindrome(str, len);
        printf("%s\n", a ? "SIM" : "NAO");
    }

    return 0;
}
