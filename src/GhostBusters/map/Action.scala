package GhostBusters.map;
import GhostBusters.characters.*

/** The class `Action` represents actions that a player may take in a text adventure game.
  * `Action` objects are constructed on the basis of textual commands and are, in effect,
  * parsers for such commands. An action object is immutable after creation.
  * @param input  a textual in-game command such as “enter bathroom” or “get torch” */
class Action(input: String):

  private val commandText = input.trim.toLowerCase
  private val verb        = commandText.takeWhile( _ != ' ' )
  private val modifiers   = commandText.drop(verb.length).trim

  /** Causes the given player to take the action represented by this object, assuming
    * that the command was understood. Returns a description of what happened as a result
    * of the action (such as “You go west.”). The description is returned in an `Option`
    * wrapper; if the command was not recognized, `None` is returned. */
  def execute(actor: Player) = this.verb match
    case "enter"        => Some(actor.go(this.modifiers))
    case "quit"         => Some(actor.quit())
    case "get"          => Some(actor.get(this.modifiers))
    case "use"          => Some(actor.use(this.modifiers))
    case "install"      => Some(actor.install(this.modifiers))
    case "inventory"    => Some(actor.inventory)
    case "inv"          => Some(actor.inventory)
    case "guess"        => Some(actor.guess(this.modifiers))
    case "examine"      => Some(actor.examine(this.modifiers))
    case "help"         => Some(this.help)
    case "commands"     => Some(this.availableCommands)
    case other          => None

  def help = "See the guide for help or see the available commands with 'commands'"
  def availableCommands = Vector("enter", "get", "use", "install", "examine", "inv", "inventory", "guess", "quit").mkString(", ")

  /** Returns a textual description of the action object, for debugging purposes. */
  override def toString = s"$verb (modifiers: $modifiers)"

end Action

