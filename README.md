# game-of-life

Conway's Game of Life application.  Rendered using Java StdDraw api. ImportedPatterns contains parsed board patterns from LifeWiki that can be displayed.

To run the app:

Make sure game-of-life is the current directory
% cd game-of-life

Ensure you have access permissions
% chmod +x run.sh

Start the program
% ./run.sh

Navigating Game of Life:

Click any cell to flip its current state
Press space to start / pause life
Use [WASD] to move the view window and [QE] to zoom in/out
Use [UP,DOWN,LEFT,RIGHT] keys to scale the x and y axis independently

Press [I] to enter configuration mode in console

Press [R] (and mouse while life running) to reset board to default
Press [T] to set current board state as default
Press [C] to "Factory Reset" board state

Press [P] to adjust life running speed

Simple Patterns

Static patterns

        XX
        XX


        XX
       X  X
        XX


        XX
       X  X
        X X
         X


       XX
       X X
        X


        X
       X X
        X

Oscillators

        XXX 


        XXX
         XXX


        XX
        XX
          XX
          XX

