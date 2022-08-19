#include <ctype.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>

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

bool is_vowel(char c) {
    char cl = tolower(c);
    return cl == 'a' || cl == 'e' || cl == 'i' || cl == 'o' || cl == 'u';
}

bool is_vowel_str(char *str, int len) {
    for (int i = 0; i < len; i++) {
        if (!is_vowel(str[i])) {
            return false;
        }
    }
    return true;
}

int is_consonant(char c) {
    char cl = tolower(c);
    return 'a' <= cl && cl <= 'z' && !is_vowel(cl);
}

int is_consonant_str(char *str, int len) {
    for (int i = 0; i < len; i++) {
        if (!is_consonant(str[i])) {
            return false;
        }
    }
    return true;
}

bool is_int(char *str, int len) {
    for (int i = 0; i < len; i++) {
        if (!isdigit(str[i])) {
            return false;
        }
    }
    return true;
}

bool is_float(char *str, int len) {
    bool sep = false;
    for (int i = 0; i < len; i++) {
        char c = str[i];
        if (!sep && (c == '.' || c == ',')) {
            sep = true;
            continue;
        }
        if (!isdigit(c)) {
            return false;
        }
    }
    return true;
}

const char *yes_no(bool b) {
    return b ? "SIM" : "NAO";
}

#define yn(fn) yes_no(fn(str, len))

int main() {
    char str[4096];
    int len = 0;

    while (read_string(str, sizeof(str), &len) != NULL) {
        if (strcmp(str, "FIM") == 0) {
            break;
        }
        printf("%s %s %s %s\n",
               // Cursed.
               yn(is_vowel_str), yn(is_consonant_str), yn(is_int),
               yn(is_float));
    }

    return 0;
}
