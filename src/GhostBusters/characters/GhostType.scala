package GhostBusters.characters;
import GhostBusters.map.*

import java.net.http.HttpRequest
import scala.util.Random

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
  private def getLocation = location.get
  private def getAllRooms = allRooms.get
  private def getIndicators = traits
  private def getCurrentRoomIndicators = getLocation.indicators
  private def setLocation(loc: Room) =
    location = Option(loc)
    println(loc)

  def init(nameToSet: String, startingRoom: Room, rooms: Vector[Room]) =
    // Set variables
    name = Option(nameToSet)
    location = Option(startingRoom)
    allRooms = Option(rooms)

  private def applyIndicatorsToRoom() =
    for (indicator, value) <- getIndicators do
      indicator match
        case "camera" => if getLocation.cameraInstalled.isDefined then getLocation.cameraInstalled.get.imageOfGhost = true
        case "sound" => if getLocation.soundrecorderInstalled.isDefined then getLocation.soundrecorderInstalled.get.soundOfGhost = true
        case _ => getCurrentRoomIndicators(indicator) = value

  /** Advance is called by the player object everytime the player does one turn */
  def advance() =
    setLocation(getAllRooms(Random.between(0, getAllRooms.size)))
    applyIndicatorsToRoom()

end GhostType