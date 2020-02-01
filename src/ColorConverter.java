/**
 * This Class represents color converter which contains a color converting matrix. This is the
 * matrix used to convert a image (represented in RGB format) into different color tone.
 */
public class ColorConverter {
  /**
   * Converter that convert a color image into a greyscale image. A greyscale image is composed only
   * of shades of grey. This is a standard converting matrix to compute the "luma" for
   * high-definition television.
   */
  public static final ColorConverter GREYSCALE = new ColorConverter(new double[][]{{0.2126,
          0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}, {0.2126, 0.7152, 0.0722}});

  /**
   * Converter that convert a color image into sepia tone, which has a characteristic reddish brown
   * tone.
   */
  public static final ColorConverter SEPIA = new ColorConverter(new double[][]{
          {0.393, 0.769, 0.189}, {0.349, 0.686, 0.168}, {0.272, 0.534, 0.131}});

  /**
   * Color transformation matrix.
   */
  private double[][] matrix;

  /**
   * Constructor of a ColorConverter Object. This constructor takes in a 2D array of double which
   * represents the color converting matrix.
   *
   * @param matrix a 2D array of double which represents the color converting matrix.
   * @throws IllegalArgumentException if the input matrix is not 3 * 3.
   */
  public ColorConverter(double[][] matrix) throws IllegalArgumentException {
    if (matrix.length != 3 || matrix[0].length != 3 || matrix[1].length != 3
            || matrix[2].length != 3) {
      throw new IllegalArgumentException("Input must be a 3*3 matrix.");
    }
    this.matrix = matrix;
  }

  /**
   * The color converting matrix of this ColorConverter.
   *
   * @return the color converting matrix (2D array of double).
   */
  public double[][] getData() {
    return this.matrix;
  }
}
