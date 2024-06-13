import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    // make a defensive copy of the picture for seam carving
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = picture;
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // helper method to compute the square of difference Δx^2 and Δy^2
    private double difference(int color1, int color2) {
        int r1 = convertRGB(color1)[0];
        int g1 = convertRGB(color1)[1];
        int b1 = convertRGB(color1)[2];
        int r2 = convertRGB(color2)[0];
        int g2 = convertRGB(color2)[1];
        int b2 = convertRGB(color2)[2];
        return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) + Math.pow(b1 - b2, 2);
    }

    // method to convert a getRGB to red, green and blue
    private int[] convertRGB(int color) {
        // from https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;
        int[] result = { red, green, blue };
        return result;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= picture.width() || y >= picture.height()) {
            throw new IndexOutOfBoundsException();
        }
        int top, bottom, left, right;
        // if (x, y) is in the first row
        if (y > 0) {
            top = picture.getRGB(x, y - 1);
        }
        else {
            top = picture.getRGB(x, height() - 1);
        }

        // if (x, y) is in the last row
        if (y < height() - 1) {
            bottom = picture.getRGB(x, y + 1);
        }
        else {
            bottom = picture.getRGB(x, 0);
        }

        // if (x, y) is in the first column
        if (x > 0) {
            left = picture.getRGB(x - 1, y);
        }
        else {
            left = picture.getRGB(width() - 1, y);
        }

        // if (x, y) is in the last column
        if (x < width() - 1) {
            right = picture.getRGB(x + 1, y);
        }
        else {
            right = picture.getRGB(0, y);
        }

        double diffx = difference(left, right);
        double diffy = difference(top, bottom);
        return Math.sqrt(diffx + diffy);
    }

    // transpose the picture
    private Picture transpose(Picture pic) {
        Picture result = new Picture(height(), width());
        for (int col = 0; col < pic.width(); col++) {
            for (int row = 0; row < pic.height(); row++) {
                Color color = pic.get(col, row);
                result.set(row, col, color);
            }
        }
        return result;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        Picture transpose = transpose(picture);
        SeamCarver seamCarver2 = new SeamCarver(transpose);
        int[] result = seamCarver2.findVerticalSeam();
        return result;
    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // energy function of each pixel
        double[][] energy = new double[height()][width()];
        // shortest path from pixel in the top row to each pixel
        double[][] distTo = new double[height()][width()];
        // last pixel in the shortest path before this pixel
        int[][] edgeFrom = new int[height()][width()];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[row][col] = energy(col, row);
                // initialize the shortest path matrix and edgeFrom
                if (row == 0) {
                    distTo[row][col] = energy[row][col];
                    edgeFrom[row][col] = col;
                }
            }
        }
        for (int row = 1; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                double minDist = Double.POSITIVE_INFINITY;
                double value = energy[row][col];  // energy of the given pixel
                // up pixel
                if (value + distTo[row - 1][col] < minDist) {
                    minDist = value + distTo[row - 1][col];
                    edgeFrom[row][col] = col;
                    distTo[row][col] = minDist;
                }
                if (col == 0) {
                    // right up pixel
                    if (value + distTo[row - 1][col + 1] < minDist) {
                        minDist = value + distTo[row - 1][col + 1];
                        edgeFrom[row][col] = col + 1;
                    }
                }
                else if (col == width() - 1) {
                    // left up pixel
                    if (value + distTo[row - 1][col - 1] < minDist) {
                        minDist = value + distTo[row - 1][col - 1];
                        edgeFrom[row][col] = col - 1;
                    }
                }
                else {
                    // right up pixel
                    if (value + distTo[row - 1][col + 1] < minDist) {
                        minDist = value + distTo[row - 1][col + 1];
                        edgeFrom[row][col] = col + 1;
                    }
                    // left up pixel
                    if (value + distTo[row - 1][col - 1] < minDist) {
                        minDist = value + distTo[row - 1][col - 1];
                        edgeFrom[row][col] = col - 1;
                    }
                }
                distTo[row][col] = minDist;
            }
        }
        int[] colNum = new int[height()];
        double minVal = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width(); col++) {
            if (distTo[height() - 1][col] < minVal) {
                minVal = distTo[height() - 1][col];
                colNum[height() - 1] = col;
            }
        }
        for (int row = height() - 2; row >= 0; row--) {
            colNum[row] = edgeFrom[row + 1][colNum[row + 1]];
        }
        return colNum;
    }

    // method to test if a given seam is valid
    private boolean validSeam(int[] seam, boolean vertical) {
        if (seam == null) return false;
        if (vertical && seam.length != height()) return false;
        if (vertical && width() == 1) return false;
        if (!vertical && height() == 1) return false;
        if (!vertical && seam.length != width()) return false;
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0) return false;
            if (vertical && seam[i] >= width()) return false;
            if (!vertical && seam[i] >= height()) return false;
            if (i < seam.length - 1 && Math.abs(seam[i + 1] - seam[i]) > 1) return false;
        }
        return true;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!validSeam(seam, false)) throw new IllegalArgumentException();
        Picture removed = new Picture(width(), height() - 1);
        for (int col = 0; col < removed.width(); col++) {
            for (int row = 0; row < removed.height(); row++) {
                Color color;
                if (row == seam[col]) {
                    color = removed.get(col, row + 1);
                }
                else {
                    color = removed.get(col, row);
                }
                removed.set(col, row, color);
            }
        }
        this.picture = removed;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (!validSeam(seam, true)) throw new IllegalArgumentException();
        Picture removed = new Picture(width() - 1, height());
        for (int row = 0; row < removed.height(); row++) {
            for (int col = 0; col < removed.width(); col++) {
                Color color;
                if (col == seam[row]) {
                    color = picture.get(col + 1, row);
                }
                else {
                    color = picture.get(col, row);
                }
                removed.set(col, row, color);
            }
        }
        this.picture = removed;
    }

    //  unit testing (required)
    public static void main(String[] args) {

    }
}

// javac-algs4 ResizeDemo.java
// java-algs4 ResizeDemo HJoceanSmall.png 150 0
