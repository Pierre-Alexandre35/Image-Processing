import java.awt.image.BufferedImage;

/**
 * This interface represents a image. It contains a 3d Array of int represents the [r, g, b] data of
 * each pixel and also contains the height and width of this image (unit is pixel). It can be
 * constructed by taking in a 3d array in the correct format. It has methods that can apply filter
 * and color converter.
 */

public interface Image {
  /**
   * Method to apply the blur filter on an Image.
   *
   * @return a new Image object represents the blurred of the original object. Return this if
   *         current data is null.
   */
  Image blur();

  /**
   * Method to sharp an image.
   *
   * @return a new Image object represents the sharpened of the original object. Return this if
   *          current data is null.
   */
  Image sharpen();

  /**
   * Method to apply the sepia filter on an Image.
   *
   * @return a new Image object represents the sepia tone of the original object. Return this if
   *          current data is null.
   */
  Image sepia();

  /**
   * Method to apply the grey filter on an Image.
   *
   * @return a new Image object represents the greyscale of the original object. Return this if
   *          current data is null.
   */
  Image greyscale();

  /**
   * Return a Image object which represents the mosaiced version(stained glass window effect) of the
   * original Image object.
   *
   * @param seed number of seeds, Will be set to the total pixel number of this Image if the input
   *             number is higher than the total pixel number.
   * @return a Image object which represents the mosaiced version of the original Image object.
   *          Return this if current data is null.
   * @throws IllegalArgumentException if the input seed is not positive number,
   */
  Image mosaicing(int seed) throws IllegalArgumentException;

  /**
   * Return a Image object which represents the dithered version(black and white version) of the
   * original Image object.
   *
   * @return a Image object which represents the dithered version of the image. Return this if
   *          current data is null.
   */
  Image applyDithering();

  /**
   * Return a deep copy (copy with different reference) of the image data.
   *
   * @return a deep copy (copy with different reference) of the image data.
   */
  int[][][] getData();


  /**
   * Return the image data in the format BufferImage object.
   *
   * @return image data in the format of BufferImage object.
   */
  BufferedImage getBufferImage();


  /**
   * Return the height of the image.
   *
   * @return the height of the image. Return 0 if current data is null.
   */
  int getHeight();

  /**
   * Return the width of the image.
   *
   * @return the width of the image. Return 0 if current data is null.
   */
  int getWidth();

  /**
   * Return a new Image object represent rainbow flag.
   *
   * @param height    height of the return image
   * @param width     width of the return image
   * @param direction string "v" or "h" indicating if the rainbow strips is vertical or horizontal.
   * @return a new Image object represent rainbow flag.
   * @throws IllegalArgumentException if input height is smaller than 7 for horizontal rainbow
   *                                  strips or if input width is smaller than 7 for vertical
   *                                  rainbow strips.
   */
  Image rainbowFlag(int height, int width, String direction) throws IllegalArgumentException;

  /**
   * Return a new Image object represent 8*8 checkerBoard.
   *
   * @param squareSize user specified square size of the checker board.
   * @return a new Image object represent 8*8 checkerBoard.
   * @throws IllegalArgumentException if input squareSize is less than 1.
   */
  Image generateCheckerBoard(int squareSize) throws IllegalArgumentException;

  /**
   * Return a new Image object represents French flag.
   *
   * @param height height of the image to generate.
   * @param width  width of the image to generate.
   * @return a new Image object represents French flag.
   * @throws IllegalArgumentException if the size provided (height or width) is too small(height
   *                                  less than 1 or width less than 3).
   */
  Image generateFrenchFlag(int height, int width) throws IllegalArgumentException;

  /**
   * Return a new Image object represents Switzerland Flag.
   *
   * @param height height of the image to generate.
   * @param width  width of the image to generate.
   * @return a new Image object represents Switzerland Flag.
   * @throws IllegalArgumentException if the size provided (height or width) is too small(Less than
   *                                  5).
   */
  Image generateSwitzerlandFlag(int height, int width) throws IllegalArgumentException;

  /**
   * Return a new Image object represents Greece Flag.
   *
   * @param height height of the image to generate.
   * @param width  width of the image to generate.
   * @return a new Image object represents Greece Flag.
   * @throws IllegalArgumentException if the size provided (height or width) is too small(height
   *                                  less than 9 or width less than 4).
   */
  Image generateGreeceFlag(int height, int width) throws IllegalArgumentException;
}
