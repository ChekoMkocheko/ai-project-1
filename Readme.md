Cheko Mkocheko and Alivia Kliesen

Name of Files:
    EightPlayer.java
    Node.java

Known Bugs:

Table:

Linear Conflict Heuristic:
    Our linear conflict heuristic calculates the number of "linear conflicts" in each row and each column of the board and then multiplies the total number of conflicts
    by 2. This number is then added to the Manhattan Distance to calculate the total heuristic. 

    A linear conflict is defined as such:

    If two tiles are in their correct row/column, then there is a linear conflict is the tile further left/further up is a greater value than the tile further right/
    further down. This is because two tiles must eventually swap in order for them to be in the correct order, which adds at least 2 to the manhattan distance. 

    For example:

    If the goal row is 1 2 3 and the current state of that row is 2 1 3, then there is one linear conflict between 2 and 1. 
    If the goal column reads 1 4 7 (from top to bottom) and the current state of that column is 1 7 4, then there is a linear conflict between 7 and 4.
