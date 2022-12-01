# GhostBusters
## A Haunted House Adventure for O1 final project


Ghost Busters is an investigative adventure-horror **text-based** game where the player hunts the ghost and kills it by guessing the “type” of ghost that is haunting the house. Inspired by [Phasmophobia](https://fi.wikipedia.org/wiki/Phasmophobia). Built by [Anshul Mahajan](https://github.com/mahansh564) & [Ricky Foxell](https://github.com/Rickyya).

## Features

- Haunted house with numerous scary rooms
- Tools such as thermometer, scanner etc. to search for ghosts.
- Text based commands to play the game

Open the [playing guide](/src/GhostBusters/assets/GhostBusters%20-%20Playing%20Guide.pdf) to understand the game better.

## Tech

Ghostbusters uses a number of open source projects to work properly:

- [Scala](https://www.scala-lang.org/) - combines OOPS & Functional programming
- [Java](https://www.java.com/) - high-level, class-based, object-oriented programming language

## Installation

Run the game with the following steps.

```sh
Unzip .zip file.
Open Directory in Intellij a. File -> Open ->
Go to src -> Ghostbusters -> ui -> GhostBustersGUI.scala a. if there is any error such as "setup scala SDK", click on setup SDK. b. click create -> download -> select scala 3.2.0.
If there are errors, right click on ghostbusters in project and then go to 'open module settings' a. project settings -> modules -> dependencies -> + -> JARs or Directories -> locate the scala-swing_3-3.0.0.0.jar -> check the checkbox -> click apply and ok
Build & Run
```
