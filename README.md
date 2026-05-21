# Tetris
The application is a recreation of the classic Tetris arcade game.

## Running the Application
This project was developed using the Eclipse IDE. Simply download the source code .zip file, and place the "tetris" directory within it into the "eclipse-workspace" directory on your device that all Eclipse project directories are contained within. Then, from within Eclipse, import the project (File -> Import -> Existing Projects into Workspace -> Under "Select root directory", browse for the project directory that was placed in the "eclipse-workspace" directory). As of right now, the repository cannot be easily cloned. This will be updated in the future.

## About the Application
This build of the application is an __incomplete__ recreation of the classic Tetris arcade game. The application implements the main game loop, which involves placing falling pieces (composed of blocks) onto a 2-D grid in a way that efficiently packs them. Pieces to be placed by the player are given in a random sequence. If the player fills an entire row of the grid with blocks, the row is cleared of blocks, and all blocks above it are shifted downward, allowing for more screen space to place more pieces. The player is intended to score points when clearing rows (not yet implemented), and the game ends when the player runs out of space to place pieces (also not yet implemented). See https://www.youtube.com/watch?v=-FAzHyXZPm0 for a clearer visual.

## Use Details
The player uses the left and right arrow keys to shift a piece left and right. Holding left or right shifts a piece repeatedly in that direction. The player presses the [Z] key to rotate a piece clockwise and the [X] key to rotate a piece counterclockwise. The player can hold the down arrow key to make the current piece fall faster. The box on the right of the screen shows the next piece the player will place after placing the currently falling piece.

A few hotkeys are implemented for testing purposes:
- The number keys [0-9] above the alphabetic keyboard change the speed at which the pieces fall. A specific speed change is indicated by a different color scheme of the newly generated pieces.
- The [R-Shift] key will increase one of the [0-9] speed levels even more (the user will not notice a color change). Pressing the [R-Shift] key again will return the user to the original speed level corresponding with the piece color scheme (as selected using keys [0-9]). The [R-Shift] key essentially functions as a toggle between different speed tiers for each piece color scheme.
