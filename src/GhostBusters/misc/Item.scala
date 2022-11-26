package GhostBusters.misc;
import GhostBusters.characters.*


abstract class Item(val name: String, owner: Option[Player]):
  var uses = 3
  def checkUsesLeft =
    if uses == 0 then
      owner.get.removeItem(this.name)
      s"Your ${this.name} broke! "
    else
      ""

  def roomIndicators = owner.get.location.indicators
  def use: String
  override def toString = f"${name}%-13s: $uses uses left"

trait Installable:
  def install: String

/** All items are classified below **/

class Camera(name: String, owner: Option[Player]) extends Item(name, owner) with Installable:
  /** Blurry image of ghost
  **Not all ghosts are visible to the camera **/
  var imageOfGhost: Boolean = false
  def use = install
  def install =
      if this.owner.get.location.cameraInstalled.isEmpty then
        this.owner.get.location.cameraInstalled = Some(this)
        this.owner.get.removeItem("camera")
        "You've succesfully installed the camera in the " + owner.get.location.name
      else
        "There is already a camera installed in this room!"

  override def toString =
    if imageOfGhost then
      this.name + "       : 1 capture(s)"
    else
      this.name + "       : 0 capture(s)"

class Soundrecorder(name: String, owner: Option[Player]) extends Item(name, owner) with Installable:
  /** Ghost sounds
  **Not all ghosts make sound **/
  var soundOfGhost: Boolean = false
  def use = install
  def install =
    if this.owner.get.location.soundrecorderInstalled.isEmpty then
      this.owner.get.location.soundrecorderInstalled = Some(this)
      this.owner.get.removeItem("soundrecorder")
      "You've succesfully installed the soundrecorder in the " + owner.get.location.name
    else
      "There is already a soundrecorder installed in this room!"

  override def toString =
    if soundOfGhost then
      this.name + ": 1 capture(s)"
    else
      this.name + ": 0 capture(s)"

class Thermometer(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "You look at the thermometer, the temperature is normal in this room"
    val temperature = roomIndicators("temp")
    if temperature == "cold" then
      output = "You look at the thermometer, it is freezing cold in this room!"
    else if temperature == "hot" then
      output = "You look at the thermometer, it is scorching hot in this room!"

    checkUsesLeft + output + " (Uses: " + uses + ")"

class Scanner(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "Your scanner detects no fingerpints in this room."
    if roomIndicators("fingerprints") == true then
      output = "Your scanner detects fingerprints in this room. Oooooo....."

    checkUsesLeft + output + " (Uses: " + uses + ")"


class Torch(name: String, owner: Option[Player]) extends Item(name, owner):
  def use =
    uses -= 1
    var output = "Your scanner detects no bacteria from a ghost in this room."
    if roomIndicators("bacteria") == true then
      output = "Your scanner detects bacteria stains in this room. Oooooo....."


    checkUsesLeft + output + " (Uses left: " + uses + ")"

