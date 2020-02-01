import java.awt.Image;

/**
 * The interface for our view class. This interface represents a User Interface for picture
 * processing program.
 */
public interface IView {
  /**
   * Get the set of feature callbacks that the view can use.
   *
   * @param c the set of feature callbacks as a Features object
   */
  void setFeatures(Feature c);

  /**
   * Return the text typed into the text area as a string.
   *
   * @return the text typed into the text area as a string. Return empty string if nothing is typed.
   */
  String getInputScrip();

  /**
   * Update the JLable with new Image object.
   *
   * @param i the Image object that is shown in the JLable.
   */
  void updateImage(Image i);

  /**
   * Open the file choose and return the path and name of the file chosen by the user.
   *
   * @return the path and name of the file chosen by the user.
   */
  String getFilePath();

  /**
   * Open a optional input dialog with options in the draw down menu and return the user input as a
   * string.
   *
   * @param options the options shown in the user input dialog.
   * @param message The message shown in the dialog as explanation/instruction.
   * @return user input as a string.
   */
  String getInputFromOption(Object[] options, String message);

  /**
   * Open a user input dialog with a text field and return the user input as a string.
   *
   * @param message The message shown in the dialog as explanation/instruction.
   * @return the user input as a string.
   */
  String getInput(String message);

  /**
   * Open a dialog showing error message.
   *
   * @param errorMessage the error message being shown in the dialog.
   */
  void showErrorMessage(String errorMessage);
}
