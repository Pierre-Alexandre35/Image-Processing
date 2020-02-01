/**
 * This class represent Filter of image. Filter is a matrix which can blur and sharpen the image.
 */
public class Filter {
  /**
   * A 3x3 filter to blur a given picture.
   */
  public static final Filter BLUR = new Filter(new double[][]{{0.0625, 0.125, 0.0625},
                                                              {0.125, 0.25, 0.125},
                                                              {0.0625, 0.125, 0.0625}});

  /**
   * A 5x5 filter to blur a given picture.
   */
  public static final Filter SHARPEN =
          new Filter(new double[][]{{-0.125, -0.125, -0.125, -0.125, -0.125},
                                    {-0.125, 0.25, 0.25, 0.25, -0.125},
                                    {-0.125, 0.25, 1, 0.25, -0.125},
                                    {-0.125, 0.25, 0.25, 0.25, -0.125},
                                    {-0.125, -0.125, -0.125, -0.125, -0.125}});

  /**
   * 2D array represent the filter matrix.
   */
  private double[][] matrix;

  /**
   * Constructor of Filter object. It takes in a 2D matrix which must be square and has odd
   * dimension.
   *
   * @param matrix a 2D array of double which represents the filter matrix.
   * @throws IllegalArgumentException if matrix is empty, or if it's not square, or if its dimension
   *                                  is not odd.
   */
  public Filter(double[][] matrix) throws IllegalArgumentException {
    if (!matrixIsSquare(matrix) || matrix.length == 0 || matrix.length % 2 != 1) {
      throw new IllegalArgumentException("Input matrix must be non-empty, square matrix with odd "
              + "length.");
    }
    this.matrix = matrix;
  }


  /**
   * Returning the filtering matrix of this object.
   *
   * @return the filtering matrix of this object.
   */
  public double[][] getMatrix() {
    return this.matrix;
  }

  /**
   * Method to verify if a given Matrix is a square.
   *
   * @param matrix 2d array of double.
   * @return true if the matrix is a square, false otherwise.
   */
  private boolean matrixIsSquare(double[][] matrix) {
    int length = matrix.length;
    int i;
    for (i = 0; i < length; i++) {
      if (matrix[i].length != length) {
        return false;
      }
    }
    return true;
  }
}
