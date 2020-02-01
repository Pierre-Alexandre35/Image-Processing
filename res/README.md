**Read Me - Assignment**  **10**

This program is the of the image processing application that contained multiples features to create, generate and modify pictures which are still executables. We added new features and modified few elements in our design.

In this version of the program(Assignment 10), an IView interface is added to the program represents the user interface. This View class extends the JView interface and use the swing library to build an interactive user interface to support image processing functionality. Also, this UI provides a function for the user to type in batch script and execute it directly from the user interface and also allow the user to save the batch script they typed as a txt file.

The controller is modified to fit the new functionality. This controller not only controls the model now, but also controls the view. Most of the methods of the controller servers as action events for the action listeners inside the View. However, the controller still only takes in the model to construct, and won&#39;t linked to a View until the setView method is called. This makes it suitable not only for the interactive mode, but also suitable for the older version of this program which runs batch script file without the UI. When ruing without interactive mode, the controller&#39;s view is set to null (default), so no redundant process is executed.

Currently, our batch scriptand UI supports nine commands. The &quot;generate&quot; command must follow by sub-command which indicating which image you want to generate. The sub-commands are also listed below. The commands in the parentheses are required parameters for that command. If user command doesn&#39;t follow the required format of command provided below, IllegalArgumentEception will be thrown by the controller and shown in the UI as an error message dialog. The UI also support redo and undo functionalities by clicking the corresponding buttons. But these two commands are not supported in the batch script file.

This is the current list of the keyword that the user can perform.

- –&quot;load&quot; + (path and name of the file you want to load)
- –&quot;save&quot; + (path and name of the file you want to load)
- &quot;blur&quot;
- &quot;sharpen&quot;
- &quot;greyscale&quot;
- &quot;sepia&quot;
- –&quot;dithering&quot;
- –&quot;mosaicing&quot; + (number of seeds, int)
- –&quot;generate&quot;
  - –&quot;rainbowFlag&quot; + (height, int) + (width, int) + (direction, &quot;h&quot; or &quot;v&quot; represents horizontal of vertical strips)
  - –&quot;checkerboard&quot; + (square size, int)

These processing methods work overlap with previous result image. It means processes work on the image resulted from previous steps. But you can always use the undo and redo button to go back to previous stages.

There are two changes in the model design:

Firstly,a method, getBufferImage(),  is added to the model interface. This method return the image object as a corresponding BufferImage object. This method is used to display the image in the user interface.

Secondly, when the model is initialized without any parameters, the model data is null. Previous model didn&#39;t check this situation inside the methods and will cause nullPointerException if you click the image processing button before you load the image. Therefore, in this version, for every image processing method inside the model, it first checks if data is null, if it is, the method will immediately return and do nothing. Otherwise, it will perform the previous image processing algorithm.

**Usage**

- This program is running with Java 1.8
- Two images are already provided to apply the different features provided in our program
- Four sample command files are provided to run the program with corresponding commands.
- You can now import, modify or generate your own pictures by using the batch command filewith supported command listed above.
- **[new]** You can now use the user interface to run the program interactively.

**Features**

- Blurring and sharpen images
- Generate images with horizontal and vertical rainbow stripes
- Generate a checkerboard pattern
- Generate the flags of France, Greece and Switzerland
- Convert images to greyscale and sepia
- Dither images
- Images mozaicing
- Image manipulation by batch commands
- **[new]** Image manipulation by using user interface.
- **[new]** Redo and undo the commands in the user interface.
- **[new]** Write and execute the batch script interactively using the user interface.
- **[new]** Write and save the batch script file from user interface.

**Citations**

[1] Image 1, Khoury College of Computer Science, [https://www.khoury.northeastern.edu/](https://www.khoury.northeastern.edu/)

[2] Image 2 – New York City, [https://course.ccs.neu.edu/cs5004/assignment8.html](https://www.cafepress.com/+lower_manhattan_skyline_new_york_city_banner,1663742131), [course.ccs.neu.edu](http://course.ccs.neu.edu/)

**Authors**

This project was made as part of the Object-Oriented Design course at Northeastern University by CCIS graduates students Weihan Liu and Pierre-Alexandre Mousset.