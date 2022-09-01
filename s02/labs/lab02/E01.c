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

// Returns the index of the first occurrence of `chr` in `str`, bounded by
// `len`. Otherwise, returns `-1`.
int str_index_of(char chr, char *str, int len) {
    for (int i = 0; i < len; i++) {
        if (str[i] == chr) {
            return i;
        }
    }
    return -1;
}

// Computes the bounds of the two strings separated by `sep`.
// Final arguments are "return pointers".
//
// Clearly, the returned bounds are not NULL-terminated, as they are slices of
// the original string.
//
// Returns 0 if the string is invalid. Otherwise, 1.
//
// Interval representation is a pair of the substring starting index and its
// length. (start, len)
//
// Examples:
//
// 1)
//     |ABCDE FGH|
//     |012345678|
//     |^    |^  | (as = 0, al = 5) (bs = 6, bl = 3)
//
// 2)
//     | ABC|
//     |0123|
//     ||^  | (as = 0, al = 0) (bs = 1, bl = 3)
//
// 3)
//     |ABC |
//     |0123|
//     |^  || (as = 0, al = 3) (bs = 3, bl = 0)
int str_split_once(char sep, char *str, int len,
                   // intervals
                   int *start_a, int *len_a, int *start_b, int *len_b) {
    int sep_i = str_index_of(sep, str, len);
    if (sep_i == -1) {
        return 0;
    }

    *start_a = 0;
    *len_a   = sep_i;

    int maybe_start_b = sep_i + 1;
    if (len > maybe_start_b) {
        *start_b = maybe_start_b;
        *len_b   = len - maybe_start_b;
    } else {
        *start_b = sep_i;  // We do not want to write at index `len`.
        *len_b   = 0;
    }

    return 1;
}

// Helper that prints the string bounded by the given interval.
//
// The interval is expected to be valid, otherwise this function has UB.
//
// This is not currently being used in the code, it was created for debug
// purposes.
void print_slice(char *str, int start_index, int slice_len) {
    while (slice_len-- > 0) {
        fputc(str[start_index++], stdout);
    }
}

// Print overlapping strings, given their intervals.
// A newline is printed at the end.
//
// Such intervals are expected to be valid, otherwise this function has UB.
void print_overlapping(char *str,
                       // intervals
                       int a_start, int a_len, int b_start, int b_len) {
    int total = a_len + b_len;
    while (total-- > 0) {
        if (a_len-- > 0) {
            // Could use some sort of buffered write for efficiency's sake, but
            // who cares?
            fputc(str[a_start++], stdout);
        }
        if (b_len-- > 0) {
            fputc(str[b_start++], stdout);
        }
    }
    fputc('\n', stdout);
}

int main() {
    int exit_status = 0;

    char str[4096];
    int len = 0;

    while ((read_string(str, sizeof(str), &len)) != NULL) {
        if (len <= 0) {
            continue;
        }
        int ia, len_a;
        int ib, len_b;
        if (!str_split_once(' ', str, len, &ia, &len_a, &ib, &len_b)) {
            exit_status = 1;
            printf("Invalid entry (%s).\n", str);
            continue;
        }
        print_overlapping(str, ia, len_a, ib, len_b);
    }

    return exit_status;
}
