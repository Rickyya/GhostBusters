package GhostBusters.map
import GhostBusters.misc.*

import scala.collection.mutable.*

class Room(val name: String, val description: String, val isStaircase: Boolean):
  private var items = Map[String, Item]()
  private val neighbors = Map[String, Room]()
  def getItems = items
  def neighbours = neighbors

  // Ghost indicators
  var indicators: Map[String, Any] = Map().withDefaultValue(false)
  // Installables
  var cameraInstalled: Option[Camera] = None
  var soundrecorderInstalled: Option[Soundrecorder] = None

  // Helper functions
  def neighbor(room: String) = this.neighbors.get(room)
  def setNeighbor(room: String, neighbor: Room) = this.neighbors += room -> neighbor
  def setNeighbors(exits: Vector[(String, Room)]) = this.neighbors ++= exits
  def addItem(item: Item) = this.items += item.name -> item
  def removeItem(itemName: String) = this.items.remove(itemName)
  def contains(itemName: String) = items.keys.exists(_ == itemName)

  // Returns description of the current room
  def fullDescription =
    val exitList = "\n\nRooms available: " + this.neighbors.values.map(_.name.toLowerCase).mkString(", ")
    val itemList = if items.nonEmpty then "\nYou see here:" + items.keys.mkString(" ") else ""
    if itemList.isEmpty then this.description + exitList + itemList else this.description + itemList + exitList

end Room
