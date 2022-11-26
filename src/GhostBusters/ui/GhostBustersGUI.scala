package GhostBusters.ui
import GhostBusters.map.{Game, Room}

import scala.swing.*
import scala.swing.event.*
import javax.swing.{ImageIcon, OverlayLayout, UIManager}
import GhostBusters.*

import java.awt.{Cursor, Desktop, Dimension, Graphics2D, Insets, Point, Toolkit}
import scala.language.adhocExtensions
import java.awt.Color.*
import java.awt.Graphics2D.*
import java.io.File
import javax.sound.sampled.*

/** The singleton object `AdventureGUI` represents a GUI-based version of the Adventure
  * game application. The object serves as a possible entry point for the game app, and can
  * be run to start up a user interface that operates in a separate window. The GUI reads
  * its input from a text field and displays information about the game world in uneditable
  * text areas.
  *
  * @see [[AdventureTextUI]] */

object GhostBustersGUI extends SimpleSwingApplication:
  UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName)

  //Window for the start of the game
  def top = new MainFrame:


    // Components:
    val titleLabel = Label("Haunted House Adventure")              //Label for the title of the game
    titleLabel.foreground = white                                       //Set foreground (text) color to white
    titleLabel.font = new Font("Monospaced", 100, 85)                   //Set font attributes

    val authorsLabel = Label("By: Anshul Mahajan & Ricky Foxell")  //Label for the authors of the game
    authorsLabel.foreground = white                                     //Set foreground (text) color to white
    authorsLabel.font = new Font("Monospaced", 100, 35)                 //Set font attributes

    val topHeader = new GridPanel(2, 1):                           //Header Element -> contains the title of the game and the name of authors.
      background = darkGray
      contents += titleLabel
      contents += authorsLabel
      maximumSize = this.preferredSize
      minimumSize = this.preferredSize

    var innerPlayButton = new Button("Play"):
      font = new Font("Monospaced", 100, 35)
      contentAreaFilled = false
      background = black
      foreground = white
      opaque = true
      cursor = new Cursor(Cursor.HAND_CURSOR)

    var playButton = new BorderPanel:
      background = darkGray
      add(innerPlayButton, BorderPanel.Position.Center)
      border = Swing.EmptyBorder(0, 0, 10, 0)

    var innerGuideButton = new Button("Open Guide"):
      font = new Font("Monospaced", 100, 35)
      contentAreaFilled = false
      background = black
      foreground = white
      opaque = true
      cursor = new Cursor(Cursor.HAND_CURSOR)
    var guideButton = new BorderPanel:
      background = darkGray
      add(innerGuideButton, BorderPanel.Position.Center)
      border = Swing.EmptyBorder(0, 0, 10, 0)

    var innerQuitButton = new Button("Quit"):
      font = new Font("Monospaced", 100, 35)
      contentAreaFilled = false
      background = black
      foreground = RED
      opaque = true
      cursor = new Cursor(Cursor.HAND_CURSOR)
    var quitButton = new BorderPanel:
      background = darkGray
      add(innerQuitButton, BorderPanel.Position.Center)
      border = Swing.EmptyBorder(0, 0, 10, 0)


    // Layout:
    val buttonContainer = new GridPanel(3, 1):
      border = Swing.EmptyBorder(50, 300, 50, 300)
      background = darkGray
      tooltip = "Read rules before playing!"
      contents += playButton
      contents += guideButton
      contents += quitButton

    this.contents = new GridPanel(2, 2):
      background = darkGray
      contents += topHeader
      contents += buttonContainer


    // Events:
    this.listenTo(innerPlayButton, innerGuideButton, innerQuitButton)
    this.reactions += {
      case clickEvent: ButtonClicked =>
        val nameOfButton = clickEvent.source.text.toLowerCase()
        if nameOfButton == "play" then
          playButtonHandler()
        else if nameOfButton == "open guide" then
          helpButtonHandler()
        else
          quitButtonHandler()
    }


    // Menu:
    this.menuBar = new MenuBar:
      contents += new Menu("Program"):
        val quitAction = Action("Quit")( dispose() )
        contents += MenuItem(quitAction)
      contents += new Menu("Help"):
        val guideAction = Action("Open Guide") ( openGuide() )
        contents += MenuItem(guideAction)


    // Event Handlers:
    def playButtonHandler() =
      PlayScreen.visible = true
      dispose()

    def helpButtonHandler() =
      openGuide()

    def quitButtonHandler() =
      val optionSelected = Dialog.showConfirmation(top, "Are you sure you want to quit the game?", optionType = Dialog.Options.OkCancel, title = "Quit")
      if optionSelected == Dialog.Result.Ok then
        quit()

    def openGuide() =
      val file = new File("src/GhostBusters/assets/Playing guide.pdf")
      Desktop.getDesktop.open(file)

    def playMusicOnStart() =
      val music = new File("src/GhostBusters/assets/bg_music.wav")
      val audio = AudioSystem.getAudioInputStream(music)
      val clip = AudioSystem.getClip
      clip.loop(Clip.LOOP_CONTINUOUSLY)
      clip.open(audio)
      clip.start()


    // Set up the GUI’s initial state:
    this.title = "Haunted House Adventure by Anshul & Ricky | Aalto University, 2022"
    this.location = Point(50, 50)
    this.resizable = false
    this.preferredSize = Toolkit.getDefaultToolkit.getScreenSize
    this.pack()
    this.maximize()
    this.playMusicOnStart()

  end top


  // Window for the play screen of the game
  def PlayScreen = new MainFrame:

    // Access to the application’s internal logic:
    val game = Game()
    val player = game.player


    // Components:
    val locationInfo = new TextArea(7, 80):
      editable = false
      wordWrap = true
      lineWrap = true
      background = darkGray
      foreground = white
      font = new Font("Monospaced", 100, 20)

    val turnOutput = new TextArea(7, 80):
      editable = false
      wordWrap = true
      lineWrap = true
      background = darkGray
      foreground = white
      font = new Font("Monospaced", 100, 20)

    val input = new TextField(50):
      minimumSize = preferredSize
      background = darkGray
      foreground = white
      font = new Font("Monospaced", 100, 20)

    val inventoryListing = new TextArea(2, 10):
      editable = false
      wordWrap = true
      lineWrap = true
      background = darkGray
      foreground = white
      font = new Font("Monospaced", 100, 20)

    val ghostConvo = new TextArea(2, 10):
      editable = false
      wordWrap = true
      lineWrap = true
      background = darkGray
      foreground = white
      font = new Font("Monospaced", 100, 20)


    this.listenTo(input.keys)
    val turnCounter = Label()
    turnCounter.foreground = white
    turnCounter.font = new Font("Monospaced", 100, 20)

    val imageIcon = new ImageIcon("src/GhostBusters/assets/images/ghost.png").getImage
    val imageSize = Toolkit.getDefaultToolkit.getScreenSize
    val scaledImage = new ImageIcon(imageIcon.getScaledInstance((imageSize.width/5.5).toInt, ((imageSize.width/5.5)/1.27).toInt,  java.awt.Image.SCALE_SMOOTH))
    var image = new Label { icon =  scaledImage}

    val label1 = Label("Description:")
    label1.foreground = white
    label1.font = new Font("Monospaced", 100, 20)
    val label2 = Label("Command:")
    label2.foreground = white
    label2.font = new Font("Monospaced", 100, 20)
    val label3 = Label("Events:")
    label3.foreground = white
    label3.font = new Font("Monospaced", 100, 20)


    // Events:
    this.reactions += {
      case keyEvent: KeyPressed =>
        if keyEvent.source == this.input && keyEvent.key == Key.Enter && !this.game.isOver then
          val command = this.input.text.trim
          if command.nonEmpty then
            this.input.text = ""
            this.playTurn(command)
    }


    // Layout:
    this.contents = new GridBagPanel:
      import scala.swing.GridBagPanel.Anchor.*
      import scala.swing.GridBagPanel.Fill
      layout += image              -> Constraints(2, 1, 1, 4, 0, 0, SouthEast.id, Fill.None.id, Insets(0, 2, 0, 0), 0, 0)
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
    def openGuide() =
      val file = new File("GhostBusters/Playing guide.pdf")
      Desktop.getDesktop.open(file)

    def playTurn(command: String) =
      val turnReport = this.game.playTurn(command)
      if this.player.hasQuit then
        this.dispose()
      else
        this.updateInfo(turnReport)
        this.input.enabled = !this.game.isOver

    def updateInfo(info: String) =
      if !this.game.isOver then
        this.turnOutput.text = info
      else
        this.turnOutput.text = info + "\n\n" + this.game.goodbyeMessage
      this.locationInfo.text = this.player.location.fullDescription + "\n"
      this.inventoryListing.text = this.player.inventory + "\n"
      this.turnCounter.text = "Turns played: " + this.game.turnCount
      this.ghostConvo.text = this.player.randomTaunt
      this.image.visible = false
      this.image.visible = true


    // Menu:
    this.menuBar = new MenuBar:
      contents += new Menu("Program"):
        val quitAction = Action("Quit")( quit() )
        contents += MenuItem(quitAction)
      contents += new Menu("Help"):
        val guideAction = Action("Open Guide") ( openGuide() )
        contents += MenuItem(guideAction)


    // Set up the GUI’s initial state:
    this.title = "Haunted House Adventure by Anshul & Ricky | Aalto University, 2022"
    this.updateInfo(this.game.welcomeMessage)
    this.location = Point(50, 50)
    this.resizable = false
    this.preferredSize = Toolkit.getDefaultToolkit.getScreenSize
    this.pack()
    this.maximize()
    this.input.requestFocusInWindow()


  // Enable this code to work even under the -language:strictEquality compiler option:
  private given CanEqual[Component, Component] = CanEqual.derived
  private given CanEqual[Key.Value, Key.Value] = CanEqual.derived

end GhostBustersGUI

