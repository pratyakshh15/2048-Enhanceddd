# 2048-Enhanced

A Java-based 2048 game built with Gradle, which features a click-to-spawn tile mechanic and configurable grid size via command-line arguments.

---

## Features
- Classic 2048 sliding and merging mechanics
- Arrow key controls for movement
- Click on any empty tile to manually spawn a random 2 or 4
- Configurable grid size using command-line arguments
- Smooth tile movement animations
- Game-over detection
- In-game timer
- Press **R** to restart the game

---

## Custom Gameplay Mechanic
Unlike the original 2048 game where tiles spawn automatically after every move, this version allows the player to manually spawn tiles by clicking on empty cells, adding an extra layer of strategy and player control to the game.

---

## Tech Stack
- **Java**
- **Gradle**
- **PApplet** 

---

## How to Run

### Prerequisites
- Java installed
- Gradle installed (or Gradle Wrapper)

### Run with default grid size (4×4)
```bash
gradle run
```
### Run with custom grid size
```bash
gradle run --args="5"
```
This will start the game with a 5×5 grid. If no argument is provided, the game runs with the default grid size.