package GhostBusters.characters;

/** Internal Imports */
import GhostBusters.map.*
/** Java Imports */
import java.net.http.HttpRequest
/** Scala Imports */
import scala.util.Random

/** Parameterized enumerations of the types of ghosts (GhostType) along with individual characteristics */
enum GhostType(traits: Map[String, Any]):
  case Inferno    extends GhostType(Map("fingerprints"->true, "camera"->true,                "bacteria"->true, "temp"->"cold")  .withDefaultValue(false))
  case Yeti       extends GhostType(Map(                      "camera"->true,                "bacteria"->true, "temp"->"hot")   .withDefaultValue(false))
  case Nano       extends GhostType(Map("fingerprints"->true,                 "sound"->true,                   "temp"->"hot")   .withDefaultValue(false))
  case Demon      extends GhostType(Map(                      "camera"->true, "sound"->true, "bacteria"->true, "temp"->"normal").withDefaultValue(false))
  case Spirit     extends GhostType(Map("fingerprints"->true,                 "sound"->true,                   "temp"->"cold")  .withDefaultValue(false))
  case Ultraghost extends GhostType(Map("fingerprints"->true, "camera"->true, "sound"->true, "bacteria"->true, "temp"->"normal").withDefaultValue(false))


  var name: Option[String] = None
  var location: Option[Room] = None
  var allRooms: Option[Vector[Room]] = None
  def getName = name.get

  /** Helper functions */
  private def getLocation = location.get
  private def getAllRooms = allRooms.get
  private def getIndicators = traits
  private def getCurrentRoomIndicators = getLocation.indicators
  private def setLocation(loc: Room) = location = Option(loc)

  /** Initialize function */
  def init(nameToSet: String, startingRoom: Room, rooms: Vector[Room]) =
    name = Option(nameToSet)
    location = Option(startingRoom)
    allRooms = Option(rooms)

  /** Applies the ghost's indicators the the new room
   * i.e. the ghost will leave its fingerprints if the ghosttype leaves fingerprints
   * */
  private def applyIndicatorsToRoom() =
    for (indicator, value) <- getIndicators do
      indicator match
        case "camera" =>
          if getLocation.cameraInstalled.isDefined then
            getLocation.cameraInstalled.get.imageOfGhost = true
        case "sound" =>
          if getLocation.soundrecorderInstalled.isDefined then
            getLocation.soundrecorderInstalled.get.soundOfGhost = true
        case _ =>
          getCurrentRoomIndicators(indicator) = value

  /** Ghost moves into a random room in the house and applies its indicators to the new room
   * Advance is called by the player object everytime the player does one turn */
  def advance() =
    setLocation(getAllRooms(Random.between(0, getAllRooms.size)))
    applyIndicatorsToRoom()

end GhostType