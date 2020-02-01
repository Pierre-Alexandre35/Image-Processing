import java.io.IOException;

/**
 * This interface represents a set of features  that the Image interface（model） offers. Each feature
 * is exposed as a function in this interface. This function is used suitably as a callback by the
 * view, to pass control to the controller. How the view uses them as callbacks is completely up to
 * how the view is designed (e.g. it could use them as a callback for a button, or a callback for a
 * dialog box, or a set of text inputs, etc.)
 * Each function is designed to take in the necessary data to complete that functionality.(1)
 * (1)cited from CS5004-Lecture Code: https://course.ccs.neu.edu/cs5004/lecturegui.html
 */

public interface Feature {

  /**
   * Link the Feature and the view. Give feature callbacks to the view.
   *
   * @param v the IView object this Feature linked to.
   */
  void setView(IView v);

  /**
   * Ask the model to process the input command accordingly and update the model of this Feature
   * with the process result. Ask the view to get user input when necessary. This method is designed
   * to used inside the actionListener of the view. Do nothing if view is null.
   *
   * @param command input command.
   */
  void processCommand(String command);

  /**
   * Recover from the undo and update the image label in the view if view exits (not null). Do
   * nothing if the redo stack is empty.
   */
  void redo();

  /**
   * Change the model to last version and update the image label in the view if view exits (not
   * null). Do nothing if there as no more than one element inside the undo stack.
   */
  void undo();

  /**
   * Ask the view to promote the user with a file path and name. load this image to the model and
   * ask the view to show this image on the image label inside the UI. Do nothing if view is null.
   *
   * @throws IOException if file reading not success or user input file is not a valid format.
   */
  void loadImage() throws IOException;

  /**
   * Ask the view to promote the user with a file path and name. Save current model into the
   * specified name and path. Do nothing if view is null.
   *
   * @throws IOException if file writing not success or user input file is not a valid format.
   */
  void saveImage() throws IOException;

  /**
   * Exit the program.
   */
  void exitProgram();

  /**
   * Get the text typed into the text area of the view as a string and process it by the model.
   * Update the model accordingly. Do nothing if view is null. Ask the view to pop up a error
   * message dialog if input scrip is not a valid format.
   */
  void executeScrip();

  /**
   * Ask the view to promote the user with a file path and name. Get the text typed into the text
   * area of the view as a string and save it as txt file into the user specified path and name. If
   * IOException, ask the view to pop up a error message dialog. Do nothing if view is null.
   */
  void saveScrip();

  /**
   * Take in a batch-scrip file as a string and process the command accordingly.
   *
   * @param f string represent a batch-scrip commands.
   * @throws IllegalArgumentException if the string is not valid format of the batch-scrip.
   */
  void processFile(String f) throws IllegalArgumentException;
}
