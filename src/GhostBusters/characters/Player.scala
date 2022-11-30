package GhostBusters.characters;

/** Internal Imports */
import GhostBusters.map.*
import GhostBusters.misc.*
/** Java Imports */
import java.net.URL
import java.util.Scanner
/** Scala Imports */
import scala.util.Random
import scala.collection.mutable.*

/** Represents the player of the game.
 *
 * A player object is immutable. All commands that the player uses are routed from 'Action' to this class.
 *
 *
 * @param startingRoom : the room where the player starts; later generalized to currentRoom
 * @param ghost        : the ghost which is haunting the player*/
class Player(startingRoom: Room, ghost: GhostType):

  var angeredGhost = false                            // checks if the ghost is "angered" -> super-powered ghost
  private var currentRoom = startingRoom              // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false                // flag to check if the game has been quit
  private var nameGuessed = false                     // flag to check if the player has guessed the name of the ghost
  private var nameGuessedWrong = false                // flag to check if the player has guessed the wrong name of the ghost
  private var inv = Map[String, Item]()               // map of all the items that the player has in their inventory

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven
  /** Determines if the player has guessed the name of the ghost correctly. */
  def hasWon = this.nameGuessed
  /** Determines if the player has guessed the name of the ghost incorrectly. */
  def hasLost = this.nameGuessedWrong

  /** Returns the player’s current location. */
  def location = this.currentRoom

  /** Moves the player to the specified room */
  def go(room: String) =
    val destination = this.location.neighbor(room.toLowerCase)
    this.currentRoom = destination.getOrElse(this.currentRoom)
    ghost.advance()
    if destination.isDefined then "You enter " + room + "." else "You can't enter " + room + "."

  /** Guess the ghost type */
  def guess(name: String) =
    if name.toLowerCase == this.ghost.toString.toLowerCase then
      nameGuessed = true
      "\r"
    else
      nameGuessedWrong = true
      "\r"

  def quit() =
    this.quitCommandGiven = true
    ""
  def removeItem(itemName: String) = this.inv.remove(itemName)
  def addItem(item: Item) = this.inv += item.name -> item
  def addItems(items: Vector[Item]) = for item <- items do addItem(item)
  def has(itemName: String) = this.inv.keys.exists(_ == itemName.toLowerCase)

  /** Returns a string of the players inventory */
  def inventory: String =
    if inv.nonEmpty then
      "Inventory" + "\n" + inv.values.mkString("\n")
    else
      "Inventory" + "\n" + "You are empty-handed."

  /** pickup/get tries to get an item in a room and then adds it into the players inventory */
  def pickup(itemName: String) = get(itemName)
  def get(itemName: String) =
    if itemName == "camera" then
      if this.location.cameraInstalled.isDefined then
        addItem(this.location.cameraInstalled.get)
        this.location.cameraInstalled = None
        s"You pick up the $itemName, examine it to see what it has captured."
      else
        s"There is no $itemName installed"
    else if itemName == "soundrecorder" then
      if this.location.soundrecorderInstalled.isDefined then
        addItem(this.location.soundrecorderInstalled.get)
        this.location.soundrecorderInstalled = None
        s"You pick up the $itemName, examine it to see what it has captured."
      else
        s"There is no $itemName installed"
    else if currentRoom.contains(itemName) then
      this.inv += itemName -> currentRoom.removeItem(itemName).get
      s"You pick up the $itemName."
    else
      s"There is no $itemName here to pick up."

  /** use an item and call angerGhost if possible */
  def use(itemName: String) =
    if has(itemName) then
      this.inv(itemName).use + angerGhost
    else
      s"You don't have $itemName"

  /** Install an item if possible */
  def install(itemName: String) =
    if has(itemName) && this.inv(itemName).isInstanceOf[Installable] then
      this.inv(itemName).asInstanceOf[Installable].install
    else
      s"You don't have $itemName or it can't be installed."

  /** Examine an item if possible */
  def examine(itemName: String) =
    if has(itemName) then
      this.inv(itemName).examine + angerGhost
    else
      "You don't have " + itemName

  // Ghost-specific methods:
  /** Generates a random taunt from the API that the ghost then says to the player. */
  def randomTaunt =
    // Forms a HTTP Request to the API, returns the taunt
    def connect =
      val tauntAPI = new URL("https://genr8rs.com/api/Content/Fun/GameTauntGenerator?genr8rsUserId=1669455422.61076381de3e9519a&_sInsultLevel=funny")
      val connection = tauntAPI.openConnection()
      val scan = new Scanner(connection.getInputStream)
      val taunt = scan.nextLine()
      scan.close()
      taunt.substring(13, taunt.length - 2)

    try
      var taunt = connect
      val badWords = Vector("genital", "reproductive", "reproductive organs", "genitals", "groin", "retarded", "puffy", "reproductive organ")
      while badWords.exists( (word) => taunt.contains(word) ) do
        taunt = connect
      "\"" + taunt + "\"" + "\n" + "  " + s"- ${ghost.name.get} \uD83D\uDC7B"
    catch
      case _ => "\"" + "You will regret coming to this house!" + "\"" + "\n" + "  " + s"- ${ghost.name.get} \uD83D\uDC7B"




  /** Checks if the ghost is angered and then removes all indicators from current location. */
  def angerGhost =
    if angeredGhost then
      angeredGhost = false
      location.indicators.clear()          // removes all indicators from current location; used to agitate the player
      "\n\n" + ghost.name.get + ": You have found something that belongs to me... You will regret this\n\nYou realize that the room was wiped clean by the ghost..."
    else
      ""

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

end Player

