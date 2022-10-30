import java.util.Iterator;
import java.util.Scanner;
import java.util.function.Function;

public class E07 {
    public static void main(String[] args) {
        try (Scanner s = new Scanner(System.in)) {
            run(s);
        }
    }

    private static void run(Scanner s) {
        int cases = readInt(s);
        while (cases-- > 0) {
            IntMatrix matA = getMatrix(s);
            IntMatrix matB = getMatrix(s);

            iterSeqPrint(matA.mainDiagonal());
            iterSeqPrint(matA.secondaryDiagonal());

            printMatrix(matA.sum(matB));
            printMatrix(matA.mul(matB));
        }
    }

    private static void printMatrix(IntMatrix mat) {
        for (int r = 0; r < mat.getRows(); r++) {
            iterSeqPrint(mat.row(r));
        }
    }

    private static IntMatrix getMatrix(Scanner s) {
        int rows = readInt(s);
        int cols = readInt(s);
        IntMatrix mat = new IntMatrix(rows, cols);
        for (int i = 0; i < rows; i++) {
            String[] row = s.nextLine().trim().split("\\s");
            if (row.length != cols) {
                throw new RuntimeException(String.format(
                        "Unexpected row length, expected %d columns, got %d", cols, row.length));
            }
            for (int j = 0; j < row.length; j++) {
                int n = Integer.parseInt(row[j]);
                mat.set(n, i, j);
            }
        }
        return mat;
    }

    private static int readInt(Scanner s) {
        return Integer.parseInt(s.nextLine().trim());
    }

    private static <T> void iterSeqPrint(Iterable<T> iter) {
        boolean first = true;
        for (T el : iter) {
            if (!first) {
                System.out.print(" ");
            }
            System.out.print(el);
            first = false;
        }
        System.out.print('\n');
    }
}

class IntMatrix {
    private int rows;
    private int cols;
    private int[] buf;

    public IntMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        buf = new int[rows * cols];
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    private int realIndex(int row, int col) {
        return row * rows + col;
    }

    public int get(int row, int col) {
        return buf[realIndex(row, col)];
    }

    private int get(Pos pos) {
        return get(pos.row, pos.col);
    }

    public void set(int val, int row, int col) {
        buf[realIndex(row, col)] = val;
    }

    public IntMatrix sum(IntMatrix other) {
        assertSameSize(this, other);
        IntMatrix res = new IntMatrix(this.rows, other.cols);
        for (int r = 0; r < res.getRows(); r++) {
            for (int c = 0; c < res.getCols(); c++) {
                int cell = this.get(r, c) + other.get(r, c);
                res.set(cell, r, c);
            }
        }
        return res;
    }

    public IntMatrix mul(IntMatrix other) {
        IntMatrix res = new IntMatrix(this.rows, other.cols);
        for (int r = 0; r < res.getRows(); r++) {
            for (int c = 0; c < res.getCols(); c++) {
                int cell = multiplyMatricesCell(this, other, r, c);
                res.set(cell, r, c);
            }
        }
        return res;
    }

    private static int multiplyMatricesCell(IntMatrix a, IntMatrix b, int row, int col) {
        int res = 0;
        for (int i = 0; i < b.getRows(); i++) {
            res += a.get(row, i) * b.get(i, col);
        }
        return res;
    }

    public Iterable<Integer> row(int index) {
        return () -> new Iter(index, 0, Pos::incrCol);
    }

    public Iterable<Integer> col(int index) {
        return () -> new Iter(0, index, Pos::incrRow);
    }

    public Iterable<Integer> mainDiagonal() {
        assertSquare();
        return () -> new Iter(0, 0, (pos) -> pos.with(1, 1));
    }

    public Iterable<Integer> secondaryDiagonal() {
        assertSquare();
        return () -> new Iter(0, cols - 1, (pos) -> pos.with(1, -1));
    }

    private void assertSquare() {
        if (rows != cols) {
            throw new RuntimeException("This operation requires a square matrix");
        }
    }

    private static void assertSameSize(IntMatrix a, IntMatrix b) {
        if (a.getRows() != b.getRows()) {
            throw new RuntimeException("Different number of rows");
        }
        if (a.getCols() != b.getCols()) {
            throw new RuntimeException("Different number of cols");
        }
    }

    private class Iter implements Iterator<Integer> {
        private Pos curr;
        private Function<Pos, Pos> next;

        public Iter(int row, int col, Function<Pos, Pos> next) {
            this(new Pos(row, col), next);
        }

        public Iter(Pos init, Function<Pos, Pos> next) {
            curr = init;
            this.next = next;
        }

        @Override
        public boolean hasNext() {
            return (0 <= curr.row && curr.row < rows)
                    && (0 <= curr.col && curr.col < cols);
        }

        @Override
        public Integer next() {
            Pos tmp = curr;
            curr = next.apply(tmp);
            return get(tmp);
        }
    }

    private class Pos {
        public int row = 0;
        public int col = 0;

        public Pos(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public Pos map(Function<Pos, Pos> fn) {
            return fn.apply(this);
        }

        public Pos with(int moreRows, int moreCols) {
            return map((prev) -> new Pos(prev.row + moreRows, prev.col + moreCols));
        }

        public Pos incrRow() {
            return with(1, 0);
        }

        public Pos incrCol() {
            return with(0, 1);
        }
    }
}
