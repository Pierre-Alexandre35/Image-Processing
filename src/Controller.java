import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * This class takes in user commands as a list of string and react accordingly. The controller class
 * is the interaction between the user and the model. The user controls how and when the model is
 * used. The user has the ability to convert, filter or generate a new Image by just typing the
 * method desired.
 */
public class Controller implements Feature {


  private Image model;
  private IView view;
  private Stack<Image> redoStack;
  private Stack<Image> undoStack;

  /**
   * Construct a Controller object with and Image object. Set default empty redoStack and empty
   * undoStack. The view is set to null before any View object is linked to this controller through
   * the setView.
   *
   * @param model Image object server as model of this controller.
   */
  public Controller(Image model) {
    this.model = model;
    view = null;
    redoStack = new Stack<>();
    undoStack = new Stack<>();
  }

  @Override
  public void setView(IView v) {
    view = v;
    view.setFeatures(this);
  }

  @Override
  /**
   * Command can be "blur", "sharpen", "greyscale", "sepia", "dithering",
   * "mosaicing", "generate rainbowflag" or "generate checkerboard". The last three
   * command ask the view to promote user input.
   */
  public void processCommand(String command) {
    if (view == null) {
      return;
    }
    switch (command) {
      case "blur":
        model = model.blur();
        break;
      case "sharpen":
        model = model.sharpen();
        break;
      case "greyscale":
        model = model.greyscale();
        break;
      case "sepia":
        model = model.sepia();
        break;
      case "dithering":
        model = model.applyDithering();
        break;
      case "mosaicing":
        String seed_s = view.getInput("Enter number of seed (integer): ");
        if (seed_s == null) {
          return;
        }
        try {
          int seed = Integer.parseInt(seed_s);
          model = model.mosaicing(seed);
        } catch (NumberFormatException e) {
          view.showErrorMessage("Please enter an integer for number of seeds.");
        }
        break;
      case "generate rainbowflag":
        Object[] directionOptions = {"Horizontal", "Vertical"};
        String message = "I want the strips of my rainbow flag to be: ";
        String d_s = view.getInputFromOption(directionOptions, message);
        if (d_s == null) {
          return;
        }
        String h_s = view.getInput("Enter height of your image (integer): ");
        if (h_s == null) {
          return;
        }
        String w_s = view.getInput("Enter width of your image (integer): ");
        if (w_s == null) {
          return;
        }
        String direction;
        if (d_s.equals("Horizontal")) {
          direction = "h";
        } else {
          direction = "v";
        }
        try {
          int h = Integer.parseInt(h_s);
          int w = Integer.parseInt(w_s);
          model = model.rainbowFlag(h, w, direction);
        } catch (NumberFormatException e) {
          view.showErrorMessage("Please enter an integer for height and width.");
        }
        break;
      case "generate frenchFlag":
        String size_s = view.getInput("Enter size of each square (integer): ");
        if (size_s == null) {
          return;
        }
        try {
          int squareSize = Integer.parseInt(size_s);
          model = model.generateFrenchFlag(squareSize,squareSize);
        } catch (NumberFormatException e) {
          view.showErrorMessage("Please enter an integer for square size.");
        }
        break;
      case "generate checkerboard":
        String size_s = view.getInput("Enter size of each square (integer): ");
        if (size_s == null) {
          return;
        }
        try {
          int squareSize = Integer.parseInt(size_s);
          model = model.generateCheckerBoard(squareSize);
        } catch (NumberFormatException e) {
          view.showErrorMessage("Please enter an integer for square size.");
        }
        break;
      default:
        break;
    }
    if (model.getData() == null) {
      return;
    }
    view.updateImage(model.getBufferImage());
    undoStack.push(model);
  }

  @Override
  public void redo() {
    if (redoStack.empty()) {
      return;
    }
    Image temp = redoStack.pop();
    undoStack.push(temp);
    model = undoStack.peek();
    if (view == null) {
      return;
    }
    view.updateImage(model.getBufferImage());
  }

  @Override
  public void undo() {
    if (undoStack.empty()) {
      return;
    }
    Image temp = undoStack.pop();
    if (undoStack.empty()) {
      undoStack.push(temp);
      return;
    }
    redoStack.push(temp);
    model = undoStack.peek();
    if (view == null) {
      return;
    }
    view.updateImage(model.getBufferImage());
  }

  @Override
  public void loadImage() throws IOException {
    if (view == null) {
      return;
    }
    String loadedImage = view.getFilePath();
    if (!loadedImage.equals("")) {
      load(loadedImage);
      view.updateImage(model.getBufferImage());
    }
  }

  /**
   * Lode the image file into the model. The image file will be transfer into 3D array and store as
   * the data in the model. Initilize undo and redo stack when loading a new file.
   *
   * @param loadedImage String represants path and name of the loaded file.
   * @throws IOException if reading the file not successes.
   */
  private void load(String loadedImage) throws IOException {
    model = new ImageImpl(ImageUtil.readImage(loadedImage));
    undoStack = new Stack<>();
    redoStack = new Stack<>();
    undoStack.push(model);
  }

  @Override
  public void saveImage() throws IOException {
    if (view == null) {
      return;
    }
    String saveImage = view.getFilePath();
    if (!saveImage.equals("")) {
      ImageUtil.writeImage(model.getData(), model.getWidth(), model.getHeight(), saveImage);
    }
  }

  @Override
  public void exitProgram() {
    System.exit(0);
  }

  @Override
  public void executeScrip() {
    if (view == null) {
      return;
    }
    String inputScrip = view.getInputScrip();
    if (inputScrip.equals("")) {
      return;
    }
    try {
      processFile(inputScrip);
      view.updateImage(model.getBufferImage());
    } catch (IllegalArgumentException e) {
      view.showErrorMessage(e.getMessage());
    }
  }

  @Override
  public void processFile(String f) throws IllegalArgumentException {
    Scanner scanTemp = new Scanner(f);
    String first = scanTemp.next();
    if (!first.equals("load") && !first.equals("generate")) {
      throw new IllegalArgumentException("File must start with load or generate.");
    }

    Scanner scan = new Scanner(f);
    while (scan.hasNext()) {
      String command = scan.next();
      switch (command) {
        case "load":
          //must follow by valid file name
          if (!scan.hasNext()) {
            throw new IllegalArgumentException("Load must follow by a file name");
          }
          String filename = scan.next();
          if (!filename.contains(".")) {
            throw new IllegalArgumentException("Load must follow by a valid file name");
          }
          try {
            load(filename);
          } catch (IOException e) {
            throw new IllegalArgumentException("Error writing or reading file");
          }
          break;
        case "save":
          //must follow by valid file name
          if (!scan.hasNext()) {
            throw new IllegalArgumentException("Save must follow by a file name");
          }
          String saveFileName = scan.next();
          if (!saveFileName.contains(".")) {
            throw new IllegalArgumentException("Save must follow by a valid file name");
          }
          try {
            ImageUtil.writeImage(model.getData(), model.getWidth(), model.getHeight(),
                    saveFileName);
          } catch (IOException e) {
            throw new IllegalArgumentException("Error writing or reading file");
          }
          break;
        case "blur":
          model = model.blur();
          break;
        case "sharpen":
          model = model.sharpen();
          break;
        case "greyscale":
          model = model.greyscale();
          break;
        case "sepia":
          model = model.sepia();
          break;
        case "dithering":
          model = model.applyDithering();
          break;
        case "mosaicing":
          if (!scan.hasNextInt()) {
            throw new IllegalArgumentException("Please specify a integer number of seed "
                    + "following 'mosaicing'");
          }
          int seed = scan.nextInt();
          model = model.mosaicing(seed);
          break;
        case "generate":
          if (!scan.hasNext()) {
            throw new IllegalArgumentException("What do you want to generate? Specify "
                    + "after 'generate'");
          }
          String generateTo = scan.next();
          switch (generateTo) {
            case "rainbowFlag":
              if (!scan.hasNextInt()) {
                throw new IllegalArgumentException("Please generate rainbow flag "
                        + "with height(int), width(int) and direction('h' or 'v')");
              }
              int height = scan.nextInt();
              if (!scan.hasNextInt()) {
                throw new IllegalArgumentException("Please generate rainbow flag "
                        + "with height(int), width(int) and direction('h' or 'v')");
              }
              int width = scan.nextInt();
              if (!scan.hasNext()) {
                throw new IllegalArgumentException("Please generate rainbow flag with "
                        + "height(int), width(int) and direction('h' or 'v')");
              }
              String direction = scan.next();
              if (!direction.equals("h") && !direction.equals("v")) {
                throw new IllegalArgumentException("Direction of the rainbow flag "
                        + "must be 'h' or 'v'");
              }
              model = model.rainbowFlag(height, width, direction);
              break;
            case "checkerboard":
              if (!scan.hasNextInt()) {
                throw new IllegalArgumentException("Please generate checkerboard "
                        + "with square size(int)");
              }
              int squaresize = scan.nextInt();
              model = model.generateCheckerBoard(squaresize);

              break;
            default:
              throw new IllegalArgumentException("Generate type does not supported.");
          }
          break;
        default:
          throw new IllegalArgumentException("Unknown command");
      }
    }
  }

  @Override
  public void saveScrip() {
    if (view == null) {
      return;
    }
    String inputScrip = view.getInputScrip();
    String saveName = view.getFilePath();
    if (saveName.equals("")) {
      return;
    }
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(saveName));
      writer.write(inputScrip);
    } catch (IOException e) {
      view.showErrorMessage("Error reading or writing file.");
    } finally {
      try {
        if (writer != null) {
          writer.close();
        }
      } catch (IOException e) {
        view.showErrorMessage("Error reading or writing file.");
      }
    }
  }
}






