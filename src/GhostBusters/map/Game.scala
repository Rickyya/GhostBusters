package GhostBusters.map
import GhostBusters.map.*
import GhostBusters.characters.*
import GhostBusters.misc.*
import scala.*
import scala.collection.immutable.Vector
import scala.collection.mutable.Buffer
import scala.util.Random

class Game:

  /** the name of the game */
  val title = "A Haunted House Adventure"

  private val (basement: Room, floor1: Room, floor2: Room, attic: Room) =  (
    Room("Basement", "You are in the basement.", false),
    Room("Floor1", "You are on the first floor.", false),
    Room("Floor2", "You are on the second floor.", false),
    Room("Attic", "You are in the attic.", false)
  )

  private val staircase = Room("staircase", "You are in the staircase. The stairs creak with every move. There are spider webs all around. Are you in the right place?", true)
  staircase.setNeighbors(Vector("basement" -> basement, "floor1" -> floor1, "floor2" -> floor2, "attic" -> attic))

  // basement
  private val storage = Room("Storage", "desc", false)
  private val laundry = Room("Laundry", "desc", false)
  basement.setNeighbors(Vector("staircase" -> staircase, "storage" -> storage, "laundry" -> laundry))
  laundry.setNeighbors(Vector("storage" -> storage, "staircase" -> staircase))
  storage.setNeighbors(Vector("laundry" -> laundry, "staircase" -> staircase))

  // floor 1
  private val bathroom = Room("Bathroom" , "desc", false)
  private val foyer = Room("Foyer", "You are in the foyer on the first floor", false)
  private val masterbedroom = Room("Master Bedroom", "desc", false)
  floor1.setNeighbors(Vector("staircase" -> staircase, "bathroom"->bathroom, "foyer" -> foyer, "masterbedroom" -> masterbedroom))
  bathroom.setNeighbors(Vector("staircase" -> staircase, "foyer" -> foyer, "masterbedroom" -> masterbedroom))
  foyer.setNeighbors(Vector("staircase" -> staircase, "bathroom"->bathroom, "masterbedroom" -> masterbedroom))
  masterbedroom.setNeighbors(Vector("staircase" -> staircase, "bathroom"->bathroom, "foyer" -> foyer))

  // floor 2
  private val kitchen = Room("Kitchen", "desc", false)
  private val guestbedroom = Room("Guest-Bedroom", "desc", false)
  floor2.setNeighbors(Vector("staircase" -> staircase, "kitchen" -> kitchen, "guestbedroom" -> guestbedroom))
  kitchen.setNeighbors(Vector("staircase" -> staircase, "guestbedroom" -> guestbedroom))
  guestbedroom.setNeighbors(Vector("staircase" -> staircase, "kitchen" -> kitchen))

  // attic
  private val storage2 = Room("Storage", "desc", false)
  private val observatory = Room("Observatory", "desc", false)
  attic.setNeighbors(Vector("staircase" -> staircase, "storage" -> storage2, "observatory" -> observatory))
  storage2.setNeighbors(Vector("staircase" -> staircase, "observatory" -> observatory))
  observatory.setNeighbors(Vector("staircase" -> staircase, "storage" -> storage2))

  private val allRooms: Vector[Room] = Vector(staircase, storage, laundry, basement, bathroom, foyer, masterbedroom, floor1, kitchen, guestbedroom, floor2, storage2, observatory, attic)
  private val randomStartingRoom: Room = allRooms(Random.nextInt(allRooms.size))
  private val allGhosts = GhostType.values

  def getAllRooms = staircase.neighbours.values.map(_.neighbours.values.toVector.sortBy(_.name != "staircase")).toVector.reverse

  /** Initializing all the game components
    * Ghost: GhostType(name, startingRoom, vector allRooms)
    * Player: Player(startingRoom, the ghost)
    * */
  val ghost = allGhosts(Random.nextInt(allGhosts.length))
  val randomNames = Vector("Michael", "George", "Lisa", "Emma")
  val randomName = randomNames(Random.nextInt(randomNames.size))
  ghost.init(randomName + " the Ghost", basement, allRooms)
  print(ghost) //REMVOVE IN PROD

  /** The character that the player controls in the game. */
  val player = Player(staircase, ghost)
  val items = Vector(
    Camera("camera", Some(player)),
    Soundrecorder("soundrecorder", Some(player)),
    Torch("torch", Some(player)),
    Scanner("scanner", Some(player)),
    Thermometer("thermometer", Some(player))
  )
  player.addItems(items)

  /** The number of turns that have passed since the start of the game. */
  var turnCount = 0
  /** The maximum number of turns that this adventure game allows before time runs out. */
  val turnLimit = 40

  /** Determines if the game is complete, that is, if the player has won. */
  def isComplete =
    player.hasWon || player.hasLost || turnCount == turnLimit

  /** Determines whether the player has won, lost, or quit, thereby ending the game. */
  def isOver =
    player.hasQuit || this.isComplete

  /** Returns a message that is to be displayed to the player at the beginning of the game. */
  def welcomeMessage =
    "Welcome Ghostbuster!"

  /** Returns a message that is to be displayed to the player at the end of the game. The message
    * will be different depending on whether or not the player has completed their quest. */
  def goodbyeMessage =
    if this.isComplete then
      var message = "You found out my ghost type! AHHHHHHH I'M DEAD! \nGood game, ghostbuster :)"
      if player.hasLost then message = "Loser! I ate you up! \nYou're dead :)"
      message
    else  // game over due to player quitting
      "Quitter!"

    /** Plays a turn by executing the given in-game command, such as “go west”. Returns a textual
    * report of what happened, or an error message if the command was unknown. In the latter
    * case, no turns elapse. */
  def playTurn(command: String) =
    val action = Action(command)
    val outcomeReport = action.execute(this.player)
    if outcomeReport.isDefined && command.contains("enter") then
      this.turnCount += 1
    outcomeReport.getOrElse(s"Unknown command: \"$command\".")

end Game

