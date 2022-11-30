package GhostBusters.misc;
import GhostBusters.characters.*

/** Represents an item.
 *
 * @param name : The name of the item
 * @param owner: The (option) owner of an item. None or Player */
abstract class Item(val name: String, owner: Option[Player]):
  var uses = 4
  /** Check  */
  def checkUsesLeft =
    if uses == 0 then
      owner.get.removeItem(this.name)
      s"Your ${this.name} broke! "
    else
      ""
  def roomIndicators = owner.get.location.indicators
  def use: String
  def examine = this.name + s": $uses left"
  override def toString = f"${name}%-13s: $uses uses left"

trait Installable:
  def install: String

/** All items are classified below **/

class Camera(name: String, owner: Option[Player]) extends Item(name, owner) with Installable:
  /** Blurry image of ghost
  **Not all ghosts are visible to the camera **/
  uses = 1
  var imageOfGhost: Boolean = false
  def use = install
  def install =
      if this.owner.get.location.cameraInstalled.isEmpty then
        this.owner.get.location.cameraInstalled = Some(this)
        this.owner.get.removeItem("camera")
        "You've succesfully installed the camera in the " + owner.get.location.name
      else
        "There is already a camera installed in this room!"

  override def examine =
    if imageOfGhost then
      owner.get.angeredGhost = true
      this.name + ": 1 capture(s)"
    else
      this.name + ": 0 capture(s)"

class Soundrecorder(name: String, owner: Option[Player]) extends Item(name, owner) with Installable:
  /** Ghost sounds
  **Not all ghosts make sound **/
  uses = 1
  var soundOfGhost: Boolean = false
  def use = install
  def install =
    if this.owner.get.location.soundrecorderInstalled.isEmpty then
      this.owner.get.location.soundrecorderInstalled = Some(this)
      this.owner.get.removeItem("soundrecorder")
      "You've succesfully installed the soundrecorder in the " + owner.get.location.name
    else
      "There is already a soundrecorder installed in this room!"

  override def examine =
    if soundOfGhost then
      owner.get.angeredGhost = true
      this.name + ": 1 capture(s)"
    else
      this.name + ": 0 capture(s)"

class Thermometer(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "You look at the thermometer, the temperature is normal in this room"
    val temperature = roomIndicators("temp")
    if temperature == "cold" then
      owner.get.angeredGhost = true
      output = "You look at the thermometer, it is freezing cold in this room!"
    else if temperature == "hot" then
      owner.get.angeredGhost = true
      output = "You look at the thermometer, it is scorching hot in this room!"

    checkUsesLeft + output + " (Uses: " + uses + ")"

class Scanner(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "Your scanner detects no fingerpints in this room."
    if roomIndicators("fingerprints") == true then
      owner.get.angeredGhost = true
      output = "Your scanner detects fingerprints in this room. Oooooo....."

    checkUsesLeft + output + " (Uses: " + uses + ")"


class Torch(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "Your scanner detects no bacteria from a ghost in this room."
    if roomIndicators("bacteria") == true then
      owner.get.angeredGhost = true
      output = "Your scanner detects bacteria stains in this room. Oooooo....."


    checkUsesLeft + output + " (Uses left: " + uses + ")"

