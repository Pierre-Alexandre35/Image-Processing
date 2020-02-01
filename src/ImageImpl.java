import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class represents an Image which constructs the Object with 3D array representing the [r, g,
 * b] for each pixel of the image. It contains all the methods required in the * Image interface.
 */
public class ImageImpl implements Image {
  private int[][][] data;


  /**
   * Constructor that takes in a 3D array of int represents the [r,b,g] for every pixel of the
   * image.
   *
   * @param m 3d array of int represents a image.
   * @throws IllegalArgumentException if input 3D array is not in correct format.
   */
  public ImageImpl(int[][][] m) {
    this.data = m;
  }


  /**
   * Constructor that takes in no parameter and construct an Image object with null data.
   */
  public ImageImpl() {
    this.data = null;
  }


  @Override
  public int[][][] getData() {
    if (data == null) {
      return null;
    }
    int height = this.getHeight();
    int width = this.getWidth();
    int[][][] copyData = new int[height][width][3];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        for (int x = 0; x < 3; x++) {
          copyData[i][j][x] = data[i][j][x];
        }
      }
    }
    return copyData;
  }

  @Override
  public BufferedImage getBufferImage() {
    if (data == null) {
      return null;
    }
    int width = this.getWidth();
    int height = this.getHeight();
    BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = data[i][j][0];
        int g = data[i][j][1];
        int b = data[i][j][2];

        int color = (r << 16) + (g << 8) + b;
        output.setRGB(j, i, color);
      }
    }
    return output;
  }

  @Override
  public int getHeight() {
    if (data == null) {
      return 0;
    }
    return data.length;
  }


  @Override
  public int getWidth() {
    if (data == null) {
      return 0;
    }
    return data[0].length;
  }

  @Override
  public Image blur() {
    return applyFilter(Filter.BLUR);
  }


  @Override
  public Image sharpen() {
    return applyFilter(Filter.SHARPEN);
  }


  @Override
  public Image sepia() {
    return applyColorConvert(ColorConverter.SEPIA);
  }


  @Override
  public Image greyscale() {
    return applyColorConvert(ColorConverter.GREYSCALE);
  }

  @Override
  public Image applyDithering() {
    if (data == null) {
      return this;
    }

    int height = this.getHeight();
    int width = this.getWidth();


    Image newResult = this.applyColorConvert(ColorConverter.GREYSCALE);
    int[][][] result = ((ImageImpl) newResult).data;

    int oldColor;
    int newColor;
    int error;

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        for (int color = 0; color < 3; color++) {
          oldColor = result[r][c][color];
          newColor = getNewColor(oldColor);
          error = oldColor - newColor;
          result[r][c][color] = newColor;

          if (c + 1 < width) {
            result[r][c + 1][color] += Math.round(error * 7 / 16.0);
          }
          if (r + 1 < height && c - 1 >= 0) {
            result[r + 1][c - 1][color] += Math.round(error * 3 / 16.0);
          }
          if (r + 1 < height) {
            result[r + 1][c][color] += Math.round(error * 5 / 16.0);
          }
          if (r + 1 < height && c + 1 < width) {
            result[r + 1][c + 1][color] += Math.round(error / 16.0);
          }
        }
      }
    }
    return new ImageImpl(result);
  }

  @Override
  public Image mosaicing(int seed) throws IllegalArgumentException {
    if (data == null) {
      return this;
    }
    if (seed < 0) {
      throw new IllegalArgumentException("Number of seed must be positive.");
    }

    int totalPixel = this.getWidth() * this.getHeight();
    int height = this.getHeight();
    int width = this.getWidth();
    //set the seed to the total pixel number if it's out of boundary.
    if (seed > totalPixel) {
      seed = totalPixel;
    }

    //get different random pixels and place them into Hashmap seeds.
    // key is position[row,column], value is [r,g,b]
    Map<int[], int[]> seeds = new HashMap<>();
    int count = 0;
    int row;
    int column;
    Random random = new Random();
    while (count < seed) {
      row = random.nextInt(this.getHeight());
      column = random.nextInt(this.getWidth());
      int[] position = {row, column};
      if (!seeds.containsKey(position)) {
        int[] color = data[row][column];
        seeds.put(position, color);
        count++;
      }
    }

    int[][][] result = new int[height][width][3];
    //assign new color to each pixel
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        result[r][c] = getMosaicColor(r, c, seeds);
      }
    }
    return new ImageImpl(result);
  }

  @Override
  public Image rainbowFlag(int height, int width, String direction)
          throws IllegalArgumentException {
    if (direction.equals("h")) {
      return new ImageImpl(generateRainbowFlagHorizontal(height, width));
    } else if (direction.equals("v")) {
      return new ImageImpl(generateRainbowFlagvertical(height, width));
    } else {
      throw new IllegalArgumentException("Direction can only be 'v' or 'h'.");
    }
  }

  @Override
  public Image generateCheckerBoard(int squareSize) throws IllegalArgumentException {
    if (squareSize < 1) {
      throw new IllegalArgumentException("Square size must be positive ");
    }
    int[][][] result = new int[squareSize * 8][squareSize * 8][3];
    java.util.List<Color> colorList = new ArrayList<>();
    colorList.add(Color.WHITE);
    colorList.add(Color.BLACK);

    for (int j = 0; j < 8; j += 2) {
      //white first rows:
      for (int height = squareSize * j; height < squareSize * (j + 1); height++) {
        for (int i = 0; i < 8; i++) {
          for (int width = squareSize * i; width < squareSize * (i + 1); width++) {
            int[] rgbArray = {colorList.get(i % 2).getRed(), colorList.get(i % 2).getGreen(),
                    colorList.get(i % 2).getBlue()};
            result[height][width] = rgbArray;
          }
        }
      }
      //black first rows:
      for (int height = squareSize * (j + 1); height < squareSize * (j + 2); height++) {
        for (int i = 0; i < 8; i++) {
          for (int width = squareSize * i; width < squareSize * (i + 1); width++) {
            int[] rgbArray = {colorList.get((i + 1) % 2).getRed(),
                    colorList.get((i + 1) % 2).getGreen(),
                    colorList.get((i + 1) % 2).getBlue()};
            result[height][width] = rgbArray;
          }
        }
      }
    }
    return new ImageImpl(result);
  }


  @Override
  public Image generateFrenchFlag(int height, int width) throws IllegalArgumentException {
    if ((height < 1) || (width < 3)) {
      throw new IllegalArgumentException("Too small for the french flag");
    }

    //Creating a new empty 3D array to store the final result.
    int[][][] result = new int[height][width][3];

    //Creating a new Array List with the three colors of the french flag: Blue, White and Red.
    java.util.List<Color> colorList = new ArrayList<>();
    colorList.add(Color.BLUE);
    colorList.add(Color.WHITE);
    colorList.add(Color.RED);
    int h;
    int w;
    int color;
    int stripWidth = width / 3;

    // First iteration over the height of the image.
    for (h = 0; h < height; h++) {
      // Second iteration over the number of colors (3).
      for (color = 0; color < 3; color++) {
        // Third iteration over the width of the image.
        for (w = color * stripWidth; w < (color + 1) * stripWidth; w++) {
          //Storing the current color into a new array and apply it to the result 2D array.
          int[] rgbArray = {colorList.get(color).getRed(), colorList.get(color).getGreen(),
                  colorList.get(color).getBlue()};
          result[h][w] = rgbArray;
        }
      }
    }
    return new ImageImpl(result);
  }


  @Override
  public Image generateSwitzerlandFlag(int height, int width) throws IllegalArgumentException {

    if ((height < 5) || (width < 5)) {
      throw new IllegalArgumentException("Too small for the Switzerland flag");
    }

    //Creating a new empty 3D array to store the final result.
    int[][][] result = new int[height][width][3];

    //Creating a new Array List with the three colors of the french flag: Blue, White and Red.
    java.util.List<Color> colorList = new ArrayList<>();
    colorList.add(Color.RED);
    colorList.add(Color.WHITE);
    int h;
    int w;

    //Storing the dimensions of the cross.
    double crossCheck20 = 1.0 / 5.0;
    double crossCheck40 = 2.0 / 5.0;
    double crossCheck60 = 3.0 / 5.0;
    double crossCheck80 = 4.0 / 5.0;

    // First iteration over the height of the image.
    for (h = 0; h < height; h++) {
      // Second iteration over the width of the image.
      for (w = 0; w < width; w++) {
        //Checking if the current height and width are within the range of the cross.
        if (h > (crossCheck40 * height) && h < (crossCheck60 * height)
                && (w > (crossCheck20 * width) && w < (crossCheck80 * width))
                || ((h > (crossCheck20 * height) && h < (crossCheck80 * height)
                && (w > (crossCheck40 * width) && w < (crossCheck60 * width))))) {
          //If true, the current point is colored is white.
          int[] rgbArray = {Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue()};
          result[h][w] = rgbArray;
        } else {
          //Otherwise, the current point is colored is red.
          int[] rgbArray = {Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue()};
          result[h][w] = rgbArray;
        }
      }
    }
    return new ImageImpl(result);
  }

  @Override
  public Image generateGreeceFlag(int height, int width) throws IllegalArgumentException {
    if ((height < 9) || (width < 4)) {
      throw new IllegalArgumentException("Too small for the Greece flag");
    }
    //Creating a new empty 3D array to store the final result.
    int[][][] result = new int[height][width][3];

    //Storing the color of the greece flag into a new list.
    java.util.List<Color> colorList = createGreeceFlagColor();
    int h;
    int w;
    int color;
    int stripThickness = height / 9;


    //Storing the break point to exit the cross on the right top corner.
    double cornerBreakHeight = 10.0 / 18.1;
    double cornerBreakWidth = 10.0 / 27.0;


    //Storing the dimensions of the little white cross on the right corner.
    double crossVerticalLeft = 4.0 / 27.0;
    double crossVerticalRight = 6.0 / 27.0;
    double crossHorizontalLeft = 4.0 / 18.1;
    double crossHorizontalRight = 6.0 / 18.1;


    // Iterate over the 9 horizontal stripes of the greece flag.
    for (color = 0; color < 9; color++) {
      // Iterating over the flag height and switch color of we are reaching the stripThickness.
      for (h = color * stripThickness; h < (color + 1) * stripThickness; h++) {
        // Third iteration over the width of the flag.
        for (w = 0; w < width; w++) {
          int[] rgbArray = {colorList.get(color).getRed(), colorList.get(color).getGreen(),
                  colorList.get(color).getBlue()};
          result[h][w] = rgbArray;
        }
      }
    }

    // Now we are going to draw the cross on the top left corner of
    // the flag by overriding the current stripes on that specific location.
    //First iteration over the height of the image.
    for (h = 0; (h <= (cornerBreakHeight * height)); h++) {
      // Second iteration over the width of the image.
      for (w = 0; (w <= (cornerBreakWidth * width)); w++) {
        //Checking if the current height and width are within the range of the cross.
        if (h > (height * crossHorizontalLeft) && h < (height * crossHorizontalRight)
                || (w > (width * crossVerticalLeft) && w < (width * crossVerticalRight))) {
          int[] rgbArray = {Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue()};
          result[h][w] = rgbArray;
        } else {
          // Drawing the blue background within the top right corner.
          int[] rgbArray = {Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue()};
          result[h][w] = rgbArray;
        }
      }
    }
    return new ImageImpl(result);
  }


  /**
   * Takes in a ColorConverter object and apply it to the image. Helper function of greyscale and
   * sepia methods.
   *
   * @param converter a ColorConverter object.
   * @return a new Image object represents the result of color transformation.
   */
  private Image applyColorConvert(ColorConverter converter) {
    if (data == null) {
      return this;
    }
    int height = this.getHeight();
    int width = this.getWidth();
    int[][][] result = new int[height][width][3];
    double[][] converterData = converter.getData();

    int r;
    int c;
    for (r = 0; r < height; r++) {
      for (c = 0; c < width; c++) {
        result[r][c] = multiplyMatrix(this.data[r][c], converterData);
      }
    }
    return new ImageImpl(result);
  }


  /**
   * Takes in a Filter object and apply that filter to current image. Helper function of blur and
   * sharpen methods.
   *
   * @param blur A filter Object represents a filtering matrix.
   * @return a new Image object represents the filtered image.
   */
  private Image applyFilter(Filter blur) {
    if (data == null) {
      return this;
    }
    int height = this.getHeight();
    int width = this.getWidth();
    int[][][] newImage = new int[height][width][3];
    double[][] filter = blur.getMatrix();
    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        //apply the helper function for each pixel in this image.
        newImage[r][c] = applyFilterHelper(r, c, filter);
      }
    }
    return new ImageImpl(newImage);
  }


  /**
   * Helper function of applyColorConvert.
   *
   * @param rgbArray      3 element array of int represent r, g, b.
   * @param converterData a 3*3 array represent the color converting matrix.
   * @return a 3D array represents the result of the multiplication of the two matrices.
   */
  private int[] multiplyMatrix(int[] rgbArray, double[][] converterData) {
    int[] result = new int[3];
    int i;
    int newRGB;
    for (i = 0; i < 3; i++) {
      newRGB = (int) Math.round(converterData[i][0] * rgbArray[0] + converterData[i][1]
              * rgbArray[1] + converterData[i][2] * rgbArray[2]);
      result[i] = Math.min(Math.max(newRGB, 0), 255);
    }
    return result;
  }


  /**
   * Return the new [r,g,b] of a single pixel in position (r, c) after apply the filter.
   *
   * @param r the row number of the pixel in current image.
   * @param c the column number of the pixel in currect image.
   * @param f a 2D array of double represents a filter.
   * @return the new [r,g,b] of a single pixel in position (r, c) after apply the filter.
   */
  private int[] applyFilterHelper(int r, int c, double[][] f) {
    int filterSize = f.length;
    int half = filterSize / 2;
    int[] newRGB = new int[3]; //collect new [r, g, b]


    //loop through 3 color:
    for (int colorIndex = 0; colorIndex < 3; colorIndex++) {
      double temp = 0; // it is used to collect the accumulated result. must reset to 0 when start a
      //calculation of a new color.
      int tempInt = 0;

      //loop through the filter matrix and corresponding pixels in the original image for one color.
      for (int i = 0; i < filterSize; i++) { //row number in the filter
        for (int j = 0; j < filterSize; j++) { // column number in the filter
          if (positionInBoundary(r - half + i, c - half + j)) {
            //position (r-half, c-half) is the position of the top left corner which needs to be
            //included in the calculation of pixel (r, c).
            temp += f[i][j] * data[r - half + i][c - half + j][colorIndex];
          }
        }
      }
      tempInt = (int) Math.round(temp); //round the temp from double to int.
      newRGB[colorIndex] = Math.min(Math.max(tempInt, 0), 255);
    }
    return newRGB;
  }


  /**
   * Return true if pixel in position (r, c) is inside the boundary of current image.
   *
   * @param r row number.
   * @param c column number.
   * @return true if pixel in position (r, c) is inside the boundary of current image.
   */
  private boolean positionInBoundary(int r, int c) {
    return r >= 0 && r < this.getHeight() && c >= 0 && c < this.getWidth();
  }


  /**
   * Return 0 or 255 whichever is closer to the int in the parameter.
   *
   * @param color int.
   * @return 0 or 255 whichever is closer to the int in the parameter.
   */
  private int getNewColor(int color) {
    if (Math.abs(color - 0) <= Math.abs(color - 255)) {
      return 0;
    }
    return 255;
  }


  /**
   * Return the [r,g,b] of the mosiced color for pixel in position (r, c). The mosiced color would
   * be the same as the color of current pixel's closest seed.
   *
   * @param r     row number of current pixel.
   * @param c     column number of current pixel.
   * @param seeds Map of seeds whose keys are position of all the keys and values are the key's
   *              corresponding color(represented as [r,g,b]).
   * @return he [r,g,b] of the mosaiced color for pixel in position (r, c).
   */
  private int[] getMosaicColor(int r, int c, Map<int[], int[]> seeds) {
    double distance = Double.POSITIVE_INFINITY;
    int[] position = {r, c};
    int[] result = new int[3];
    for (int[] key : seeds.keySet()) {
      double currentDistance = distanceBetweenPixels(position, key);
      if (currentDistance < distance) {
        distance = currentDistance;
        result = seeds.get(key);
      }
    }
    return result;
  }


  /**
   * Return the distance between position1 and position2.
   *
   * @param position1 int array of [row, column] of first pixel.
   * @param position2 int array of [row, column] of second pixel.
   * @return he distance between position1 and position2.
   */
  private double distanceBetweenPixels(int[] position1, int[] position2) {
    return Math.sqrt((position1[0] - position2[0]) * (position1[0] - position2[0])
            + (position1[1] - position2[1]) * (position1[1] - position2[1]));
  }


  /**
   * Helper method for generateRainbowFlag. Return a array of seven color's [r,g,b] which will shown
   * in the generated rainbow flag.
   *
   * @return a 2D array of seven color's.
   */
  private int[][] createRainbowColor() {

    int[][] result = new int[7][3];
    result[0] = new int[]{Color.RED.getRed(), Color.RED.getGreen(),
            Color.RED.getBlue()};
    result[1] = new int[]{Color.ORANGE.getRed(), Color.ORANGE.getGreen(),
            Color.ORANGE.getBlue()};
    result[2] = new int[]{Color.YELLOW.getRed(), Color.YELLOW.getGreen(),
            Color.YELLOW.getBlue()};
    result[3] = new int[]{Color.GREEN.getRed(), Color.GREEN.getGreen(),
            Color.GREEN.getBlue()};
    result[4] = new int[]{Color.CYAN.getRed(), Color.CYAN.getGreen(),
            Color.CYAN.getBlue()};
    result[5] = new int[]{Color.BLUE.getRed(), Color.BLUE.getGreen(),
            Color.BLUE.getBlue()};
    result[6] = new int[]{104, 49, 255}; //PURPLE

    return result;
  }


  /**
   * Return an 3D array represents a rainbow flag with horizontal strips.
   *
   * @param height height of the return image
   * @param width  width of the return image
   * @return an Image object represent a rainbow flag with horizontal strips.
   */
  private int[][][] generateRainbowFlagHorizontal(int height, int width) {
    if (height < 7 || width < 1) {
      throw new IllegalArgumentException("Inputs must be positive "
              + "and height should at least be 7.");
    }
    int[][][] result = new int[height][width][3];
    int[][] colorList = createRainbowColor();
    int stripThickness = height / 7;
    for (int color = 0; color < 7; color++) {
      for (int h = color * stripThickness; h < (color + 1) * stripThickness; h++) {
        for (int w = 0; w < width; w++) {
          result[h][w] = colorList[color];
        }
      }
    }
    return result;
  }


  /**
   * Return an Image object represents a rainbow flag with vertical strips.
   *
   * @param height height of the return image
   * @param width  width of the return image
   * @return an Image object represent a rainbow flag with horizontal strips.
   * @throws IllegalArgumentException if input width is smaller than 7
   */
  private int[][][] generateRainbowFlagvertical(int height, int width) {
    if (width < 7 || height < 1) {
      throw new IllegalArgumentException("Inputs must be positive and width should at least be 7.");
    }
    int[][][] result = new int[height][width][3];
    int[][] colorList = createRainbowColor();
    int h;
    int w;
    int color;
    int stripWidth = width / 7;

    for (h = 0; h < height; h++) {
      for (color = 0; color < 7; color++) {
        for (w = color * stripWidth; w < (color + 1) * stripWidth; w++) {
          result[h][w] = colorList[color];
        }
      }
    }
    return result;
  }


  /**
   * Method to store the nine horizontal stripes of the greece flag in a list.
   *
   * @return a new list with the color for each stripes of blue alternating with white.
   */
  private static java.util.List<Color> createGreeceFlagColor() {
    List<Color> color = new ArrayList<>();
    color.add(Color.BLUE);
    color.add(Color.WHITE);
    color.add(Color.BLUE);
    color.add(Color.WHITE);
    color.add(Color.BLUE);
    color.add(Color.WHITE);
    color.add(Color.BLUE);
    color.add(Color.WHITE);
    color.add(Color.BLUE);
    return color;
  }

}
