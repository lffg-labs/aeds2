#include <stdio.h>

union {
    float as_float;
    char as_byte_arr[4];
} conv;

void write(char *filename) {
    FILE *f = fopen(filename, "wb");

    int count = 0;
    scanf("%d", &count);
    while (count-- > 0) {
        float current;
        scanf("%f", &current);

        conv.as_float = current;
        fwrite(conv.as_byte_arr, 1, 4, f);
    }

    fclose(f);
}

void read(char *filename) {
    FILE *f = fopen(filename, "rb");

    fseek(f, 0, SEEK_END);
    int byte_count  = ftell(f);
    int float_count = byte_count / 4;

    for (int i = 1; i <= float_count; i++) {
        int cursor = byte_count - i * 4;
        fseek(f, cursor, SEEK_SET);

        fread(&conv.as_byte_arr, 4, 1, f);
        float num = conv.as_float;
        printf("%g\n", num);  // n.b. `g` is decimal formatting.
    }

    fclose(f);
}

int main() {
    char name[] = "e10-store";
    write(name);
    read(name);

    return 0;
}
