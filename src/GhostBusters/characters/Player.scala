package GhostBusters.characters;

import scala.collection.mutable.*
import GhostBusters.map.*
import GhostBusters.misc.*


import java.net.URL
import java.util.Scanner
import scala.util.Random

class Player(startingRoom: Room, ghost: GhostType):

  private var currentRoom = startingRoom        // gatherer: changes in relation to the previous location
  private var quitCommandGiven = false                // one-way flag
  private var nameGuessed = false
  private var nameGuessedWrong = false
  private var inv = Map[String, Item]()

  /** Determines if the player has indicated a desire to quit the game. */
  def hasQuit = this.quitCommandGiven
  def hasWon = this.nameGuessed
  def hasLost = this.nameGuessedWrong

  /** Returns the player’s current location. */
  def location = this.currentRoom

  def go(room: String) =
    val destination = this.location.neighbor(room.toLowerCase)
    this.currentRoom = destination.getOrElse(this.currentRoom)
    ghost.advance()
    if destination.isDefined then "You enter " + room + "." else "You can't enter " + room + "."

  def guess(name: String) =
    if name.toLowerCase == this.ghost.toString.toLowerCase then
      nameGuessed = true
      "\r"
    else
      nameGuessedWrong = true
      "\r"

  def randomTaunt =
    def connect =
      val tauntAPI = new URL("https://genr8rs.com/api/Content/Fun/GameTauntGenerator?genr8rsUserId=1669455422.61076381de3e9519a&_sInsultLevel=funny")
      val connection = tauntAPI.openConnection()
      val scan = new Scanner(connection.getInputStream)
      val taunt = scan.nextLine()
      scan.close()
      taunt
    var taunt = connect
    val badWords = Vector("genital", "reproductive", "reproductive organs", "genitals", "groin", "retarded", "puffy")
    while badWords.exists( (word) => taunt.contains(word) ) do
      taunt = connect
    taunt.substring(13, taunt.length - 2)

  def quit() =
    this.quitCommandGiven = true
    ""
  def removeItem(itemName: String) = this.inv.remove(itemName)
  def addItem(item: Item) = this.inv += item.name -> item
  def addItems(items: Vector[Item]) = for item <- items do addItem(item)
  def has(itemName: String) = this.inv.keys.exists(_ == itemName.toLowerCase)

  def inventory: String =
    if inv.nonEmpty then
      "Inventory" + "\n" + inv.values.mkString("\n")
    else
      "Inventory" + "\n" + "You are empty-handed."


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

  def use(itemName: String) =
    if has(itemName) then
      this.inv(itemName).use
    else
      s"You don't have $itemName"
  
  def install(itemName: String) =
    if has(itemName) && this.inv(itemName).isInstanceOf[Installable] then
      this.inv(itemName).asInstanceOf[Installable].install
    else
      s"You don't have $itemName or it can't be installed."

  def examine(itemName: String) =
    if has(itemName) then
      this.inv(itemName).toString
    else
      "You don't have " + itemName

  /** Returns a brief description of the player’s state, for debugging purposes. */
  override def toString = "Now at: " + this.location.name

end Player

