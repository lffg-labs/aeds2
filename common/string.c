#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// -----------------------------------------------------------------------------
// General definitions

#define DEBUG_CONTROL 1

#define DEBUG(fmt, ...)                                       \
    do {                                                      \
        if (DEBUG_CONTROL) fprintf(stderr, fmt, __VA_ARGS__); \
    } while (0)

// -----------------------------------------------------------------------------
// Dynamically allocated string.

// A null-terminated, growable "string" buffer.
//
// Yes, it could have been not null-terminated, but it would make its usage
// harder.
typedef struct String {
    int cap;
    int len;
    char *buf;
} String;

void String_internal_alloc(String *self, int cap) {
    DEBUG("string: allocating %d\n", cap);
    self->cap     = cap;
    char *new_buf = (char *)malloc(sizeof(char) * cap);
    if (new_buf == NULL || cap == 0) {
        fprintf(stderr, "fatal: couldn't allocate string\n");
        exit(1);
    }
    self->buf = new_buf;
}

void String_internal_grow_for(String *self, int needed_space) {
    int curr_cap = self->cap;
    int len = self->len + 1;  // One should account for the null terminator.
    DEBUG(
        "string: attempting to grow more %d"
        " (curr len + 1 [%d]) (current cap [%d])\n",
        needed_space, len, curr_cap);

    while (len + needed_space > curr_cap) {
        curr_cap *= 2;
    }
    if (curr_cap == self->cap) {
        return;  // Already have the capacity.
    }

    char *old = self->buf;
    String_internal_alloc(self, curr_cap);  // Allocate a new buffer.
    for (int i = 0; i < len; i++) {
        self->buf[i] = old[i];
    }
    free(old);  // Free the old buffer.
}

void String_free(String s) {
    free(s.buf);
}

// Allocates a new string, provided the given capacity (which cannot be zero).
String String_alloc(int cap) {
    String s;
    s.len    = 0;
    s.cap    = cap;
    s.buf    = malloc(sizeof(char) * cap);
    s.buf[0] = 0;
    if (s.buf == NULL) {
        fprintf(stderr, "fatal: couldn't allocate string\n");
        exit(1);
    }
    return s;
}

// Clears the given string, setting its length to zero.
void String_clear(String *self) {
    self->len    = 0;
    self->buf[0] = 0;
}

// Pushes a single character at the end of the string.
void String_push(String *self, char c) {
    String_internal_grow_for(self, 1);
    self->buf[self->len]     = c;
    self->buf[self->len + 1] = 0;
}

void String_append_char_arr(String *self, int len, char *str) {
    // Notice that one is not passing `len + 1` since the current `String *self`
    // is already null terminated. This invariant is kept by the String
    // implementation itself.
    String_internal_grow_for(self, len);

    int new_len = self->len;
    for (int i = 0; i < len; i++) {
        self->buf[new_len + i] = str[i];
    }
    new_len += len;
    self->buf[new_len] = 0;
    self->len          = new_len;
}

// Appends another String at the end of the current (self) string.
void String_append(String *self, String *other) {
    String_append_char_arr(self, other->len, other->buf);
}

// -----------------------------------------------------------------------------
// Utilities

// Reads a single line from the provided file, storing it into the given String.
// The end of line markers (LF or CRLF) are not stored.
//
// Uses the given buffer (and its length) to read until a line marker (LF or
// CRLF) is reached.
//
// Returns 0 if the operation was successful, 1 otherwise.
//
// The end-of-file condition means a failure if no bytes were read. Also, when
// the end-of-file is reached (being it a failure or not), the eof indicator is
// set in the provided file.
//
// It is not safe to assume that the provided String is valid if any error is
// encountered.
int read_line(FILE *f, int buf_cap, char *buf, String *dest) {
    String_clear(dest);
    int len;
    do {
        char *str = fgets(buf, buf_cap, f);
        if (str == NULL) {
            return 1;
        }

        len      = strcspn(str, "\r\n");
        str[len] = 0;  // Remove trailing new line, if it exists.
        DEBUG("read_line: more %d to buffer\n", len);
        String_append_char_arr(dest, len, str);
    } while (len + 1 == buf_cap);

    return 0;
}

// -----------------------------------------------------------------------------
// Main

int main() {
    char buf[8];
    String s = String_alloc(2);

    while (!feof(stdin)) {
        if (read_line(stdin, sizeof(buf), buf, &s) == 1) {
            if (feof(stdin)) {
                break;  // EOF error condition.
            }
            fprintf(stderr, "fatal: couldn't read string\n");
            return 1;
        }

        printf("[%s] (len %d)\n", s.buf, s.len);
    }

    return 0;
}
