#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// -----------------------------------------------------------------------------
// General definitions

#define DEBUG_CONTROL 1

#define DEBUG(...)                        \
    do {                                  \
        if (DEBUG_CONTROL) {              \
            fprintf(stdout, __VA_ARGS__); \
        }                                 \
    } while (0)

#define ASSERT(cond, ...)                 \
    do {                                  \
        if (!(cond)) {                    \
            fprintf(stdout, __VA_ARGS__); \
            exit(1);                      \
        }                                 \
    } while (0)

// -----------------------------------------------------------------------------
// Dynamically allocated string

// A null-terminated, growable "string" buffer.
//
// Yes, it could have been not null-terminated, but it would make its usage
// harder.
typedef struct String {
    char *buf;
    int len;
    int cap;
} String;

static void String_internal_alloc(String *self, int cap) {
    ASSERT(cap != 0, "fatal: can't allocate zero-sized string\n");
    DEBUG("string: allocating %d\n", cap);

    char *new_buf = (char *)calloc(cap, sizeof(char));
    ASSERT(new_buf != NULL, "fatal: couldn't allocate string\n");

    new_buf[0] = 0;
    self->buf  = new_buf;
    self->cap  = cap;
}

static void String_internal_grow_for(String *self, int needed_space) {
    int curr_cap = self->cap;
    int len = self->len + 1;  // One should account for the null terminator.
    DEBUG("attempting to allocate +%d (real len %d) (cap %d) ", needed_space,
          len, curr_cap);

    while (len + needed_space > curr_cap) {
        curr_cap *= 2;  // TODO: Make this O(1).
    }
    if (curr_cap == self->cap) {
        DEBUG("<fit>\n");
        return;  // Already have the capacity.
    }

    DEBUG("<will realloc>\n");
    char *old = self->buf;
    String_internal_alloc(self, curr_cap);  // Allocate a new buffer.
    memcpy(self->buf, old, len);
    free(old);  // Free the old buffer.
}

void String_free(String s) {
    ASSERT(s.buf != NULL, "fatal: double free\n");
    free(s.buf);
    s.buf = NULL;
}

// Allocates a new string, provided the given capacity (which cannot be zero).
String String_alloc(int cap) {
    String s;
    String_internal_alloc(&s, cap);
    s.len = 0;
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
    self->buf[self->len++] = c;
    self->buf[self->len]   = 0;
}

void String_append_char_arr(String *self, int len, const char *str) {
    // Notice that one is not passing `len + 1` since the current `String *self`
    // is already null terminated. This invariant is kept by the String
    // implementation itself.
    String_internal_grow_for(self, len);

    int new_len = self->len;
    memcpy(&self->buf[new_len], str, len);
    new_len += len;
    self->buf[new_len] = 0;
    self->len          = new_len;
}

// Appends another String at the end of the current (self) string.
void String_append(String *self, const String *other) {
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
        // Since one utilizes fgets here, this implementation can not left
        // "forgotten" characters which may apper after a new line.
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

void print_debug_string(String *str) {
    printf("[%s] (len %d) (cap %d)\n", str->buf, str->len, str->cap);
}

int main() {
    char buf[8];
    String s1 = String_alloc(2);

    while (!feof(stdin)) {
        if (read_line(stdin, sizeof(buf), buf, &s1) == 1) {
            if (feof(stdin)) {
                break;  // EOF error condition.
            }
            ASSERT(1, "fatal: couldn't read string");
        }

        print_debug_string(&s1);
        printf("\n");
    }

    printf("---\n");

    String s2 = String_alloc(1);
    String_append_char_arr(&s2, 3, "abc");
    print_debug_string(&s2);

    String_push(&s2, 'd');
    print_debug_string(&s2);

    printf("\n");
    String s3 = String_alloc(4);
    String_append_char_arr(&s3, 3, "efg");
    printf("\n");

    String_append(&s2, &s3);
    print_debug_string(&s2);

    return 0;
}
