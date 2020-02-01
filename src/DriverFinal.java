import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Driver to run the image processing program. Run it by typing "java -jar ProgramName.jar -script
 * path-of-script-file" to execute batch-scrip file, or run it by typing "java -jar ProgramName.jar
 * -interactive" into the terminal to open the User Interface.
 */
public class DriverFinal {
  /**
   * Driver method to run the image processing program.
   * @param arg arguments indicating the way to run the program. If run by scrip, also include
   *            path name of the scrip file.
   */
  public static void main(String[] arg) {
    Image model = new ImageImpl();
    Controller c = new Controller(model);
    if (arg[0].equals("-script")) {
      String content;
      try {
        content = new String(Files.readAllBytes(Paths.get(arg[1])));
        c.processFile(content);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else if (arg[0].equals("-interactive")) {
      View view1 = new View();
      c.setView(view1);
    } else {
      throw new IllegalArgumentException("Invalid arguments.");
    }
  }
}
