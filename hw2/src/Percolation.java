import edu.princeton.cs.algs4.WeightedQuickUnionUF;


public class Percolation {

    boolean[][] squares;
    int length;
    int openSize;
    int scale;
    WeightedQuickUnionUF uf;
    WeightedQuickUnionUF ufFull;
    int virtualTopSite;
    int virtualBottomSite;

    public Percolation(int N) {
        squares = new boolean[N][N];
        length = N;
        openSize = 0;
        scale = N * N;
        virtualBottomSite = N * N + 1;
        virtualTopSite = N * N ;
        uf = new WeightedQuickUnionUF(scale + 2);
        ufFull = new WeightedQuickUnionUF(scale + 1);
        for (int i = 0; i < N; i += 1) {
            for (int j = 0; j < N; j += 1) {
                squares[i][j] = false;
            }
        }

        for (int i = 0; i < length; i += 1) {
            uf.union(i, virtualTopSite);
            ufFull.union(i, virtualTopSite);
            uf.union(i + scale - length, virtualBottomSite);
        }
    }

    private int XYto1D(int x, int y) {
        int D = x * length + y;
        return D;
    }

    public void open(int row, int col) {
        squares[row][col] = true;
        openSize += 1;;

        if (row + 1 < length && isOpen(row + 1, col)) {
            uf.union(XYto1D(row + 1, col), XYto1D(row, col));
            ufFull.union(XYto1D(row + 1, col), XYto1D(row, col));
        }
        if (row - 1 >= 0 && isOpen(row - 1, col)) {
            uf.union(XYto1D(row - 1, col), XYto1D(row, col));
            ufFull.union(XYto1D(row - 1, col), XYto1D(row, col));
        }
        if (col - 1 >= 0 && isOpen(row, col - 1)) {
            uf.union(XYto1D(row, col - 1), XYto1D(row, col));
            ufFull.union(XYto1D(row, col - 1), XYto1D(row, col));
        }
        if (col + 1 < length && isOpen(row, col + 1)) {
            uf.union(XYto1D(row, col + 1), XYto1D(row, col));
            ufFull.union(XYto1D(row, col + 1), XYto1D(row, col));
        }
    }

    public boolean isOpen(int row, int col) {
        return squares[row][col];
    }

    public boolean isFull(int row, int col) {
        return ufFull.connected(virtualTopSite, XYto1D(row, col));
    }

    public int numberOfOpenSites() {
        return openSize;
    }

    public boolean percolates() {
        return uf.connected(virtualTopSite, virtualBottomSite);
    }


}
