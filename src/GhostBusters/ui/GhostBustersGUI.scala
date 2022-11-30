package GhostBusters.ui

/** Internal Imports */
import GhostBusters.*
import GhostBusters.map.{Game, Room}
/** Java Imports */
import javax.swing.{ImageIcon, OverlayLayout, UIManager}
import java.awt.{Cursor, Desktop, Dimension, Graphics2D, Insets, Point, Toolkit}
import java.awt.Color.*
import java.awt.Graphics2D.*
import java.io.File
import javax.sound.sampled.*
/** Scala Imports */
import scala.swing.*
import scala.swing.event.*
import scala.language.adhocExtensions
import scala.swing.Dialog.Message

/** The singleton object `GhostBustersGUI` represents a GUI-based version of the GhostBusters
  * game application. The object serves as an entry point for the game app, and can
  * be run to start up a user interface that operates in a separate window. The GUI reads
  * its input from a text field and displays information about the game world in uneditable
  * text areas. */
object GhostBustersGUI extends SimpleSwingApplication:                  //Singleton object extends Simple Swing Application from Scala-Swing
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)     //Set UI design according to system presets


  // Background Music Constants:
  val music = new File("src/Ghostbusters/assets/bg_music.wav")          //Set File path for the background music
  val audio = AudioSystem.getAudioInputStream(music)                    //Retrieve audio stream from specified file path
  val clip = AudioSystem.getClip                                        //Clip object from Java AudioSystem
  clip.open(audio)                                                      //Open clip instance of the music stream


  //Window for the start of the game
  /** The "top" method is called when the object GhostBustersGUI is run. The method represents the
   * launch screen of the game. It consists of a heading, subheading, byline and three call-to-action
   * buttons. It also incorporates an easy-access menubar on the top. */
  def top = new MainFrame:

    // Components:
    val titleLabel = Label("\uD83D\uDC7B Ghost Busters \uD83D\uDC7B")   //Label for the title of the game
    titleLabel.foreground = white                                         //Set foreground (text) color to white
    titleLabel.font = new Font("Monospaced", 100, 85)                     //Set font attributes

    val authorsLabel = Label("By: Anshul Mahajan & Ricky Foxell")       //Label for the authors of the game
    authorsLabel.foreground = white                                       //Set foreground (text) color to white
    authorsLabel.font = new Font("Monospaced", 100, 25)                   //Set font attributes

    val headlineLabel = new Label("A Haunted House Adventure")          //Label for the sub heading of the game
    headlineLabel.foreground = white                                      //Set foreground (text) color to white
    headlineLabel.font = new Font("Monospaced", 100, 35)                  //Set font attributes

    val topHeader = new GridPanel(2, 1):                                //Wrapper Element -> contains the title of the game and the name of authors.
      background = darkGray                                               //Set background color to dark gray
      contents += titleLabel                                              //Add label to wrapper
      contents += new GridPanel(4,1):                                     //Make sub-wrapper for adequate spacing
        background = darkGray                                             //Set background color to dark gray
        contents += headlineLabel                                         //Add label to sub-wrapper
        contents += authorsLabel                                          //Add label to sub-wrapper
      maximumSize = this.preferredSize                                  //Force size to the preferred size
      minimumSize = this.preferredSize                                  //Force size to the preferred size

    var innerPlayButton = new Button("Start Game"):                     //Button element for starting the game
      font = new Font("Monospaced", 100, 35)                              //Set font attributes
      contentAreaFilled = false                                           //Remove empty space from around the text in the button
      background = black                                                  //Set background color to black
      foreground = white                                                  //Set foreground (text) color to white
      opaque = true                                                       //Make the object opaque so that background color is visible
      cursor = new Cursor(Cursor.HAND_CURSOR)                             //Set cursor to 'hand' cursor for interactability

    var playButton = new BorderPanel:                                   //Wrapper for Button component
      background = darkGray                                               //Set background color to dark gray
      add(innerPlayButton, BorderPanel.Position.Center)                   //Add button component at 'Center' position
      border = Swing.EmptyBorder(0, 0, 10, 0)                             //Make 'Empty' border around the button -> Spacing

    var innerGuideButton = new Button("Open Guide"):                    //Button element for opening the guide of the game
      font = new Font("Monospaced", 100, 35)                              //Set font attributes
      contentAreaFilled = false                                           //Remove empty space from around the text in the button
      background = black                                                  //Set background color to black
      foreground = white                                                  //Set foreground (text) color to white
      opaque = true                                                       //Make the object opaque so that background color is visible
      cursor = new Cursor(Cursor.HAND_CURSOR)                             //Set cursor to 'hand' cursor for interactability

    var guideButton = new BorderPanel:                                  //Wrapper for Button component
      background = darkGray                                               //Set background color to dark gray
      add(innerGuideButton, BorderPanel.Position.Center)                  //Add button component at 'Center' position
      border = Swing.EmptyBorder(0, 0, 10, 0)                             //Make 'Empty' border around the button -> Spacing

    var innerQuitButton = new Button("Quit Game"):                      //Button element for quitting the game
      font = new Font("Monospaced", 100, 35)                              //Set font attributes
      contentAreaFilled = false                                           //Remove empty space from around the text in the button
      background = black                                                  //Set background color to black
      foreground = RED                                                    //Set foreground (text) color to red
      opaque = true                                                       //Make the object opaque so that background color is visible
      cursor = new Cursor(Cursor.HAND_CURSOR)                             //Set cursor to 'hand' cursor for interactability

    var quitButton = new BorderPanel:                                   //Wrapper for Button component
      background = darkGray                                               //Set background color to dark gray
      add(innerQuitButton, BorderPanel.Position.Center)                   //Add button component at 'Center' position
      border = Swing.EmptyBorder(0, 0, 10, 0)                             //Make 'Empty' border around the button -> Spacing


    // Layout:
    val buttonContainer = new GridPanel(3, 1):                          //Wrapper for all Button Components -> Grid of 3 Rows & 1 Column
      border = Swing.EmptyBorder(50, 300, 50, 300)                        //Make 'Empty' border around the elements -> Spacing
      background = darkGray                                               //Set background color to dark gray
      tooltip = "Read rules before playing!"                              //Add hover-message
      contents += playButton                                              //Place play button
      contents += guideButton                                             //Place guide button
      contents += quitButton                                              //Place quit button

    this.contents = new GridPanel(2, 2):                                //Main Wrapper for all elements on the screen -> Grid of 2 Rows & 1 Column
      background = darkGray                                               //Set background color to dark gray
      contents += topHeader                                               //Place top header element
      contents += buttonContainer                                         //Place button elements

    var helpButtonClicked = false                                       //Flag for click of help button
    // Events:
    this.listenTo(innerPlayButton, innerGuideButton, innerQuitButton)   //Add listeners for button clicks
    this.reactions += {                                                 //Add reactions for corresponding button clicks
      case clickEvent: ButtonClicked =>
        val nameOfButton = clickEvent.source.text.toLowerCase()
        if nameOfButton == "start game" then                            //Reaction for 'Start Game' Button
          playButtonHandler()
        else if nameOfButton == "open guide" then                       //Reaction for 'Open Guide' Button
          helpButtonClicked = true
          helpButtonHandler()
        else                                                            //Reaction for 'Quit Game' Button
          quitButtonHandler()
    }


    // Menu:
    this.menuBar = new MenuBar:                                         //Add a Menu Bar to the default mainframe
      contents += new Menu("Program"):                                    //Add new 'Program' Menu Group
        val quitAction = Action("Quit")( dispose() )                        //Set action of the 'Quit' Button
        contents += MenuItem(quitAction)                                    //Add 'Quit' button to the Menu Group
      contents += new Menu("Help"):                                       //Add new 'Help' Menu Group
        val guideAction = Action("Open Guide") (helpButtonHandler() )       //Set action of the 'Open Guide' Button
        contents += MenuItem(guideAction)                                   //Add 'Open Guide' button to the Menu Group


    // Event Handlers:
    /** This method is called when the "Play Game" button is clicked. It checks if the user has opened the playing guide.
     * If not, the user prompts the user to open the guide, otherwise the method stops the music, disposes the "top" screen,
     * and opens the playing screen. */
    def playButtonHandler() =
      if helpButtonClicked then
        stopMusicOnExit()
        PlayScreen.visible = true
        dispose()
      else
        Dialog.showMessage(top, "Open the Playing Guide atleast once to understand the game well!", "Warning", messageType = Message.Warning)

    /** This method is called when the "Open Guide" button is clicked. It calls the openGuide method. */
    def helpButtonHandler() =
      helpButtonClicked = true
      openGuide()

    /** This method is called when the "Quit Game" button is clicked. It opens a dialog message which asks the user if the
     * actually want to quit. Upon clicking 'Yes', the game is quit. */
    def quitButtonHandler() =
      val options = Vector("Yes", "Cancel")
      val optionSelected = Dialog.showOptions(top, "Are you sure you want to quit the game?", optionType = Dialog.Options.OkCancel, title = "Quit", entries = options, initial = 0)
      if optionSelected == Dialog.Result.Ok then
        stopMusicOnExit()
        quit()

    /** This method opens the PDF of the Playing Guide for the game. */
    def openGuide() =
      val file = new File("src/Ghostbusters/assets/GhostBusters - Playing Guide.pdf")
      Desktop.getDesktop.open(file)

    /** This method starts the background music and loops it (almost) infinite times. */
    def playMusicOnStart() =
      clip.loop(10000)
      clip.start()

    /** This method stops the background music. */
    def stopMusicOnExit() =
      clip.stop()


    // Set up the GUI’s initial state:
    this.title = "Ghost Busters by Anshul & Ricky | Aalto University, 2022"   //Set the title of the window
    this.location = Point(50, 50)                                             //Set location of the window
    this.resizable = false                                                    //Prohibit resizing of the window by the user
    this.preferredSize = Toolkit.getDefaultToolkit.getScreenSize              //Set height & width to the screen height & width
    this.pack()                                                               //Pack all elements to their preferred sizes
    this.maximize()                                                           //Maximize window
    this.playMusicOnStart()                                                   //Play background music

  end top


  // Window for the play screen of the game
  def PlayScreen = new MainFrame:

    // Access to the application’s internal logic:
    val game = Game()                                                         //Make a new instance of the Game
    val player = game.player                                                  //Retrieve the Player object from the Game instance


    // Components:
    val locationInfo = new TextArea(7, 80):                                   //TextArea Component for info about the current location
      editable = false                                                          //Disable editing of the textarea
      wordWrap = true                                                           //Enable word wrap in the textarea
      lineWrap = true                                                           //Enable line wrap in the textarea
      background = darkGray                                                     //Set background color to dark gray
      foreground = white                                                        //Set foreground (text) color to white
      font = new Font("Monospaced", 100, 20)                                    //Set font attributes

    val turnOutput = new TextArea(7, 80):                                     //TextArea Component for output based on current turn
      editable = false                                                          //Disable editing of the textarea
      wordWrap = true                                                           //Enable word wrap in the textarea
      lineWrap = true                                                           //Enable line wrap in the textarea
      background = darkGray                                                     //Set background color to dark gray
      foreground = white                                                        //Set foreground (text) color to white
      font = new Font("Monospaced", 100, 20)                                    //Set font attributes

    val input = new TextField(50):                                            //TextField Component for input from the player
      minimumSize = preferredSize                                               //Set size to preferred size
      background = darkGray                                                     //Set background color to dark gray
      foreground = white                                                        //Set foreground (text) color to white
      caret.color = white                                                       //Set caret ("|") color to white
      font = new Font("Monospaced", 100, 20)                                    //Set font attributes

    val inventoryListing = new TextArea(2, 10):                               //TextArea Component for output based on the inventory
      editable = false                                                          //Disable editing of the textarea
      wordWrap = true                                                           //Enable word wrap in the textarea
      lineWrap = true                                                           //Enable line wrap in the textarea
      background = darkGray                                                     //Set background color to dark gray
      foreground = white                                                        //Set foreground (text) color to white
      font = new Font("Monospaced", 100, 20)                                    //Set font attributes

    val ghostConvo = new TextArea(2, 10):                                     //TextArea Component for the ghost taunts
      editable = false                                                          //Disable editing of the textarea
      wordWrap = true                                                           //Enable word wrap in the textarea
      lineWrap = true                                                           //Enable line wrap in the textarea
      background = darkGray                                                     //Set background color to dark gray
      foreground = white                                                        //Set foreground color to white
      font = new Font("Monospaced", 100, 20)                                    //Set font attributes


    this.listenTo(input.keys)                                                 //Listen to keyboard input
    val turnCounter = Label()                                                 //Label for number of turns played
    turnCounter.foreground = white                                              //Set foreground (text) color to white
    turnCounter.font = new Font("Monospaced", 100, 20)                          //Set font attributes


    val imageIcon = new ImageIcon(                                            //ImageIcon Component for the ghost image
      "src/Ghostbusters/assets/images/ghost.png").getImage
    val imageSize = Toolkit.getDefaultToolkit.getScreenSize                   //Get screen size
    val scaledImage = new ImageIcon(
      imageIcon.getScaledInstance(                                            //Scale image using custom defined proportions
        (imageSize.width/4).toInt,
        ((imageSize.width/4)/1.27).toInt,
        java.awt.Image.SCALE_SMOOTH))
    var image = new Label { icon =  scaledImage}                              //Set the image contents into a label for easier placement

    val label1 = Label("Description:")                                        //Label for "Description"
    label1.foreground = white                                                   //Set foreground (text) color to white
    label1.font = new Font("Monospaced", 100, 20)                               //Set font attributes
    val label2 = Label("Command:")                                            //Label for "Command"
    label2.foreground = white                                                   //Set foreground (text) color to white
    label2.font = new Font("Monospaced", 100, 20)                               //Set font attributes
    val label3 = Label("Events:")                                             //Label for "Events"
    label3.foreground = white                                                   //Set foreground (text) color to white
    label3.font = new Font("Monospaced", 100, 20)                               //Set font attributes


    // Events:
    this.reactions += {                                                       //Set reactions for keyboard input
      case keyEvent: KeyPressed =>
        if keyEvent.source == this.input && keyEvent.key == Key.Enter && !this.game.isOver then
          val command = this.input.text.trim.toLowerCase
          if command.nonEmpty then
            this.input.text = ""
            this.playTurn(command)                                            //Play turn using the command
    }


    // Layout:
    this.contents = new GridBagPanel:                                         //Main play screen
      import scala.swing.GridBagPanel.Anchor.*
      import scala.swing.GridBagPanel.Fill
      layout += image              -> Constraints(2, 1, 1, 4, 0, 0, SouthEast.id, Fill.None.id, Insets(0, 0, 0, 0), 0, 0)
      layout += label1             -> Constraints(0, 0, 1, 1, 0, 1, NorthWest.id, Fill.None.id, Insets(5,  5, 5, 5), 0, 0)
      layout += label2             -> Constraints(0, 1, 1, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(5, 5, 5, 5), 0, 0)
      layout += label3             -> Constraints(0, 2, 1, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(5, 5, 5, 5), 0, 0)
      layout += turnCounter        -> Constraints(0, 3, 2, 1, 0, 0, NorthWest.id, Fill.None.id, Insets(5, 5, 5, 5), 0, 0)
      layout += locationInfo       -> Constraints(1, 0, 1, 1, 1, 1, NorthWest.id, Fill.Both.id, Insets(10, 5, 5, 5), 0, 0)
      layout += input              -> Constraints(1, 1, 1, 1, 1, 0, NorthWest.id, Fill.Both.id, Insets(5, 5, 5, 5), 0, 0)
      layout += ghostConvo         -> Constraints(2, 1, 1, 2, 1, 0, NorthWest.id, Fill.Both.id, Insets(5, 5, 5, 10), 0, 0)
      layout += turnOutput         -> Constraints(1, 2, 1, 1, 1, 1, SouthWest.id, Fill.Both.id, Insets(5, 5, 5, 5), 0, 0)
      layout += inventoryListing   -> Constraints(2, 0, 1, 1, 1, 1, NorthWest.id, Fill.Both.id, Insets(10, 5, 5, 10), 0, 0)
      background = black


    // Event Handlers:
    /** This method opens the PDF of the Playing Guide for the game. */
    def openGuide() =
      val file = new File("src/Ghostbusters/assets/GhostBusters - Playing Guide.pdf")
      Desktop.getDesktop.open(file)

    /** This method is called when the user presses enter in the input. The method pushes the command to the internal game logic. */
    def playTurn(command: String) =
      val turnReport = this.game.playTurn(command)
      if this.player.hasQuit then
        stopMusicOnExit()
        this.dispose()
        top.visible = true
      else
        this.updateInfo(turnReport)
        this.input.enabled = !this.game.isOver

    /** This method is called to update all text areas on the playing screen */
    def updateInfo(info: String) =
      if !this.game.isOver then
        this.turnOutput.text = info
      else
        this.turnOutput.text = this.game.goodbyeMessage
        gameOverHandler()
      this.locationInfo.text = this.player.location.fullDescription + "\n"
      this.inventoryListing.text = this.player.inventory + "\n"
      this.turnCounter.text = "Turns Played: " + this.game.turnCount + "           " + "Current Location: " + this.player.location.name
      this.ghostConvo.text = this.player.randomTaunt
      this.image.visible = false
      this.image.visible = true

    /** This method handles the winning and closing condition of the game */
    def gameOverHandler() =
      val options = Vector(" Main Menu ", "Quit")
      var message = ""
      var messageT = Message.Info
      stopMusicOnExit()
      if this.player.hasLost then
        message = "You have lost! Better luck next time :)"
        messageT = Message.Error
      else
        message = "You have won! Good job, GhostBuster :)"
      val optionSelected = Dialog.showOptions(top, message, title = "Game Over!", optionType = Dialog.Options.YesNo, entries = options, initial = 0, messageType = messageT)
      if optionSelected == Dialog.Result.No then
        stopMusicOnExit()
        quit()
      else
        stopMusicOnExit()
        dispose()
        top.visible = true

    /** This method starts the background music and loops it (almost) infinite times. */
    def playMusicOnStart() =
      clip.loop(10000)
      clip.start()

    /** This method stops the background music. */
    def stopMusicOnExit() =
      clip.stop()

    // Menu:
    this.menuBar = new MenuBar:                                         //Add a Menu Bar to the default mainframe
      contents += new Menu("Program"):                                    //Add new 'Program' Menu Group
        val quitAction = Action("Quit")( dispose() )                        //Set action of the 'Quit' Button
        contents += MenuItem(quitAction)                                    //Add 'Quit' button to the Menu Group
      contents += new Menu("Help"):                                       //Add new 'Help' Menu Group
        val guideAction = Action("Open Guide") (openGuide() )       //Set action of the 'Open Guide' Button
        contents += MenuItem(guideAction)                                   //Add 'Open Guide' button to the Menu Group


    // Set up the GUI’s initial state:
    this.title = "Ghost Busters by Anshul " +                       //Set the title of the window
      "& Ricky | Aalto University, 2022"
    this.updateInfo(this.game.welcomeMessage)                       //Update all the text areas in the window
    this.location = Point(50, 50)                                   //Set location of the window
    this.resizable = false                                          //Prohibit resizing of the window by the user
    this.preferredSize = Toolkit.getDefaultToolkit.getScreenSize    //Set height & width to the screen height & width
    this.pack()                                                     //Pack all elements to their preferred sizes
    this.maximize()                                                 //Maximize window
    this.input.requestFocusInWindow()                               //Bring the command input in focus
    this.playMusicOnStart()                                         //Play background music

  end PlayScreen

  // Enable this code to work even under the -language:strictEquality compiler option:
  private given CanEqual[Component, Component] = CanEqual.derived
  private given CanEqual[Key.Value, Key.Value] = CanEqual.derived

end GhostBustersGUI

