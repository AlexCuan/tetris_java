
// Tetris game

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    /**
     * This variable is used to keep track of the spaces to the left and the right of the piece. It is updated every
     * time the piece is moved left or right
     */
    private static int spaces_left = 0;
    private static int spaces_right = 0;

    /**
     * These variables are used to keep track of the position of the piece in the board. They are updated every time
     * the piece is moved
     */
    private static int yPosition = 0;
    private static int xPosition = 0;
    //TODO: update spaces_right everytime a piece is placed in the board

    /**
     * This variable is used to keep track of the state of the piece. If it's rotated or not
     */
    private static boolean rotated = false;

    /**
     * Main board, defined by a matrix of X by X dimension
     */
    private static int[][] board = new int[8][8];

    /**
     * Staging board, defined by a matrix of 3 by X dimension. This board is used to display the piece before it is
     * placed in the main board
     */
    private static int[][] stagingBoard = new int[3][8];

    /**
     * This variable is used to store the piece that is going to be used. It is a 2 dimension array. It will store
     * the normal shape of the piece, and the rotated one
     */
    private static int[][][] bundleOfPieces;

    /**
     * This variable is used to store the piece that is going to be used. It is a 2 dimension array. It will store
     * the normal shape of the piece, or the rotated one
     */
    private static int[][] piece;

    /**
     * This variable is used to keep track of the state of the piece on the edges. If it's out of bounds or not
     */
    private static boolean pieceOutOfBounds = false;

    private static int points = 0;
    private static int piecesPlaced = 0;


    public static void main(String[] args) {
        // input
        clearConsole();

//        System.out.println("Welcome to Tetris");
//        System.out.println("1. Basic branch");
//        System.out.println("2. Advanced branch");
        // Print welcome to tetris with decorations
        System.out.println("▄▀▀▀▀▀▀▀▀▀▀  Welcome to TETRIS  ▀▀▀▀▀▀▀▀▀▀▄");
        // Print options with decorations
        System.out.println("█                                         █");
        System.out.println("█     1. Basic branch                     █");
        System.out.println("█     2. Advanced branch                  █");
        System.out.println("█                                         █");
        // Print bottom
        System.out.println("▀▄▄▄▄▄▄▄▄▄▄ ▄▄ ▄▄ ▄▄ ▄▄ ▄▄ ▄▄ ▄▄ ▄▄▄▄▄▄▄▄▄▀");
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n\t-> Select option: ");
        String input = scanner.nextLine();
        if (input.equals("1")) {
            basicBranch();
        } else if (input.equals("2")) {
            advancedBranch();
        } else {
            System.out.println("Invalid input. Exiting...");
        }


    }

    public static void basicBranch() {
        boolean exit = false;
        while (!exit) {
            generateAndPlacePiece();
            clearConsole();
            checkIfRowIsFull();
            printPresentation(false, false, false);
            printBoard(stagingBoard);
            printBoard(board);
            if (checkIfIsGameOver(false)) {
                clearConsole();
                printPresentation(true, true, false);
                printBoard(stagingBoard);
                printBoard(board);
                exit = true;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n-> Press enter to place the piece: ");
            String input = scanner.nextLine();
            // if scanner "" then use basic movement
            if (input.equals("")) {
                wholeBoardBasicCheckingAlgorithm2(piece);
            }
        }
    }

    public static void advancedBranch() {
        boolean exit = false;
        generateAndPlacePiece();
        while (!exit) {
            clearConsole();
            updateSpacesLeft();
            updateSpacesRight();
            checkIfRowIsFull();
            printPresentation(false, false, true);
            printBoard(stagingBoard);
            printBoard(board);
            if (checkIfIsGameOver(true)) {
                clearConsole();
                printPresentation(true, true, true);
                printBoard(stagingBoard);
                printBoard(board);
                exit = true;
            }
            userMovement();
        }
    }

    /**
     * There are a set of predefine pieces. Each piece will have it's normal shape, and a rotated one.
     *
     * @returns Two dimension array containing a random pair of pieces (normal shape and rotated one)
     */
    private static void generate_piece() {


        // *
        // *
        // *
        int[][] piece_1 = {{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};
        int[][] piece_1_rotated = {{0, 0, 0}, {0, 0, 0}, {1, 1, 1}};

        //
        // *
        // * * *
        int[][] piece_2 = {{0, 0, 0}, {1, 0, 0}, {1, 1, 1}};
        int[][] piece_2_rotated = {{0, 1, 0}, {0, 1, 0}, {1, 1, 0}};

        //
        //     *
        // * * *
        int[][] piece_3 = {{0, 0, 0}, {0, 0, 1}, {1, 1, 1}};
        int[][] piece_3_rotated = {{1, 1, 0}, {0, 1, 0}, {0, 1, 0}};

        //
        //   *
        // * * *
        int[][] piece_4 = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};
        int[][] piece_4_rotated = {{0, 1, 0}, {1, 1, 0}, {0, 1, 0}};

        //
        //   * *
        // * *
        int[][] piece_5 = {{0, 0, 0}, {0, 1, 1}, {1, 1, 0}};
        int[][] piece_5_rotated = {{1, 0, 0}, {1, 1, 0}, {0, 1, 0}};

        //
        // * *
        // * *
        int[][] piece_6 = {{0, 0, 0}, {1, 1, 0}, {1, 1, 0}};

        // create array of int [][] pieces
        int[][][][] pieces = {{piece_1, piece_1_rotated}, {piece_2, piece_2_rotated}, {piece_3, piece_3_rotated}, {piece_4, piece_4_rotated}, {piece_5, piece_5_rotated}, {piece_6, piece_6}};

        // generate random number between 0 and 5
        int random_number = (int) (Math.random() * 6);

        bundleOfPieces = pieces[random_number];
    }

    /**
     * Once a piece is generated and stored into bundleOfPieces, main shape of the piece is assigned to the piece global
     * variable which represents the "in-use" piece. Then, the piece is placed in the staging board, and the spaces
     * variables are updated. Coordinates are also updated and set to 0,0 because there is no active piece in the main
     * board
     */
    private static void generateAndPlacePiece() {
        generate_piece();
        piece = bundleOfPieces[0];
        setArbitrarySpacesLeft(0);
        rotated = false;
        placeStagingPiece(0);
        setCoords(0, 0);
    }

    /**
     * This function is used to place the piece in the staging board. It iterates over the piece and places it in the
     * correct position. It also checks if the piece is out of bounds, and if it is, it sets the pieceOutOfBounds
     * variable to true
     *
     * @param addedSpaces Used when moving the piece left or right. It adds or subtracts spaces to the left or right
     */
    private static void placeStagingPiece(int addedSpaces) {

        clearBoard(stagingBoard);
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                try {
                    if (piece[i][j] == 1) {
                        stagingBoard[i][j + spaces_left + addedSpaces] = piece[i][j];
                    }

                    pieceOutOfBounds = false;

                } catch (Exception ignored) {
                    pieceOutOfBounds = true;
                }
            }
        }

    }

    /**
     * Iterates over the board matrix and prints it. It replaces 1 with ◼, and 0 with -
     *
     * @param board Matrix to be displayed
     */
    private static void printBoard(int[][] board) {

        // Iterate over the board and print it

        for (int i = 0; i < board.length; i++) {
            // Replace 1 with ◼, and 0 with -
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    System.out.print(" \u001B[31m■\u001B[0m ");
                } else {
                    System.out.print(" - ");
                }
            }
            // Print new empty line to separate rows
            System.out.println();

        }
    }


    /**
     * Rotates the piece. If the piece is not rotated, it will rotate it. If it's rotated, it will rotate it back to
     * its original position. The rotation is done by changing the piece global variable to the rotated one and
     * updating the rotated variable
     */
    public static void rotate() {
        if (rotated) {
            rotated = false;
            piece = bundleOfPieces[0];
        } else {
            rotated = true;
            piece = bundleOfPieces[1];
        }
    }

    /**
     * Clears the board by setting all the values to 0
     *
     * @param usedBoard Matrix to be cleared
     */
    static void clearBoard(int[][] usedBoard) {
        // Iterate over the board and set all the values to 0
        for (int i = 0; i < usedBoard.length; i++) {
            Arrays.fill(usedBoard[i], 0);
        }
    }

    /**
     * Used to catch the user input and move the piece. It also checks if the piece can be placed in
     * the board, and if it can't, it will display a message and call itself again. Buttons mapped are:
     * - a: move left
     * - d: move right
     * - r: rotate
     * - s: move down
     * Each time a piece is going to move down, if it can, it will be placed into the big board and another process
     * will start until the piece can't move down anymore
     */
    static void userMovement() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n-> Select option: ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("a") && spaces_left > 0) {
            placeStagingPiece(-1);
        } else if (input.equalsIgnoreCase("d") && spaces_right > 0) {
            placeStagingPiece(1);
        } else if (input.equalsIgnoreCase("s")) {

            if (checkIfPieceCanBePlacedOnBigBoard(spaces_left, piece)) {
                clearBoard(stagingBoard);
                placePieceBoardBig(board, piece, piece.length - 1 - firstNonZeroFromAbove(piece));
                clearConsole();
                printPresentation(false, false, true);
                printBoard(stagingBoard);
                printBoard(board);
                movePieceOnBigBoard();
                piecesPlaced += 1;
                generateAndPlacePiece();
            } else {
                clearConsole();
                printPresentation(false, true, false);
                printBoard(stagingBoard);
                printBoard(board);
                userMovement();
            }

        } else if (input.equalsIgnoreCase("r")) {
            rotate();
            placeStagingPiece(0);

            if (pieceOutOfBounds) {
                readjustPiece();
            }
        }
        // TODO: remove this function
        else if (input.equalsIgnoreCase("q")) {
            placePieceBoardBig(board, piece, Arrays.stream(countSpacesDown()).min().getAsInt() - 1);
            generateAndPlacePiece();
        }
    }

    /**
     * Clears the console using ANSI escape codes
     */
    static void clearConsole() {
        // Clear the console using ANSI escape codes
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    /**
     * This function is used to update the spaces_left variable. It iterates over the 3 first lines of the board and
     * saves the position of the first 1 from left to right
     */
    static void updateSpacesLeft() {
        // Iterate over the 3 first lines of the board and save the position of the first 1 from left to right

        int counter = stagingBoard[0].length - 1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < stagingBoard[i].length; j++) {
                if (stagingBoard[i][j] == 1 && j < counter) {
                    counter = j;
                }
            }
        }
        spaces_left = counter;
    }

    /**
     * This function is used to update the spaces_right variable. It iterates over the 3 first lines of the board and
     * saves the position of the first 1 from right to left
     */
    static void updateSpacesRight() {
        // Iterate over the 3 first lines of the board and save the position of the first 1 from right to left


        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = stagingBoard[i].length - 1; j >= 0; j--) {
                if (stagingBoard[i][j] == 1 && j > counter) {
                    counter = j;
                }
            }
        }
        spaces_right = stagingBoard[1].length - 1 - counter;
    }

    /**
     * This function is used to readjust the piece if it's out of bounds. It replaces the piece in the staging board
     * -1 position to the left.
     */
    static void readjustPiece() {
        clearBoard(stagingBoard);
        pieceOutOfBounds = false;
        placeStagingPiece(-1);

    }

    /**
     * It returns the first column of a piece with a 1 from right to left
     *
     * @param piece Piece (matrix) to be checked
     * @return First column of a piece with a 1 from right to left
     */
    static int firstNonZeroFromRight(int[][] piece) {
        int counter = 0;
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1 && j > counter)
                    counter = j;
            }
        }
        return counter;
    }

    /**
     * It returns the first row of a piece with a 1 from above to below
     *
     * @param piece Piece (matrix) to be checked
     * @return First row of a piece with a 1 from above to below
     */
    static int firstNonZeroFromAbove(int[][] piece) {

        int counter = piece.length - 1;
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1 && i < counter)
                    counter = i;
            }
        }
        return counter;
    }

    // TODO: remove this function
    static int[] countSpacesDown() {
        int[] spacesDown = new int[firstNonZeroFromRight(piece) + 1];
        for (int j = spaces_left; j < spacesDown.length + spaces_left; j++) {

            for (int i = 0; i < board.length; i++) {

                if (board[i][j] == 0) {
                    spacesDown[j - spaces_left] += 1;
                }
                if (board[i][j] == 1) {
                    break;
                }
            }
        }
        return spacesDown;
    }

    //TODO: remove this function
    static int pieceSpacesDown() {
        // In the real world, this function is anecdotal if you don't want to add an extra piece
        // But if you want to add an extra piece, this function is crucial to know how many spaces
        // the piece can go down

        int minSpaceDown = 0;
        boolean stop = false;
        for (int i = piece.length - 1; i >= 0; i--) {
            for (int j = 0; j < piece[0].length; j++) {
                if (piece[i][j] == 1 && piece.length - i + 1 <= minSpaceDown) {
                    minSpaceDown += piece.length - i + 1;
                } else {
                    stop = true;
                    break;
                }
            }
        }
        return minSpaceDown;
    }

    /**
     * This function is used to place the piece in the main board. It iterates over the piece and places it in the
     * corresponding position below. It also updates the coordinates of the piece in the main board
     *
     * @param board Matrix to be updated with the piece (usually the main board)
     * @param piece Piece to be placed in the board
     * @param from  Row where the piece is going to be placed
     */
    //TODO refactor this function so we can set arbitrary spaces left
    static void placePieceBoardBig(int[][] board, int[][] piece, int from) {
        int xIterations = firstNonZeroFromAbove(piece);
        int pieceLength = piece.length - 1 - firstNonZeroFromAbove(piece);

        for (int i = from - pieceLength; i <= from; i++) {

            int yIterations = 0;
            for (int j = spaces_left; j < spaces_left + firstNonZeroFromRight(piece) + 1; j++) {
                board[i][j] += piece[xIterations][yIterations];
                setCoords(i, j);

                yIterations++;
            }
            xIterations++;

        }
    }

    /**
     * This function is used to set the coordinates of the piece in the main board
     *
     * @param x Row
     * @param y Column
     */
    static void setCoords(int x, int y) {
        yPosition = x;
        xPosition = y;

    }

    // TODO: remove this function
    static int[] onesPosition(int row) {
        // Iterate over the bottom row of the piece and save the position of the 1s
        // from left to right
        int[] onesPosition = new int[firstNonZeroFromRight(piece) + 1];
        for (int i = 0; i < onesPosition.length; i++) {
            if (piece[row][i] == 1) {
                onesPosition[i] = 1;
            }
        }
        return onesPosition;
    }


    // TODO: remove this function
    static int[] onesPositionAtTheRight() {
        // Iterate over the right column of the piece and save the position of the 1s
        // from top to bottom
        int[] onesPosition = new int[piece.length];
        int lastColumn = firstNonZeroFromRight(piece);
        for (int i = 0; i < onesPosition.length; i++) {
            if (piece[i][lastColumn] == 1) {
                onesPosition[i] = 1;
            }
        }
        return onesPosition;
    }

    /**
     * This function is used to check if the piece can move down. It iterates from the bottom row of the piece to the
     * last relevant row from the bottom. It checks if the next position of each populated cell of the piece will be
     * empty in the board. It takes into account the cases where the piece is only one row, and the cases where the piece
     * can encounter a 1 because of the shape of the piece itself.
     *
     * @return True if the piece can move down, false if it can't
     */
    static boolean checkIfPieceCanMoveDown() {

        boolean canMoveDown = true;

        for (int j = 0; j < firstNonZeroFromRight(piece) + 1; j++) {

            int nextBoardRow = yPosition + 1;

            for (int i = piece.length - 1; i >= lastRowToCheckFromBottom(); i--) {

                if (board[nextBoardRow][xPosition - firstNonZeroFromRight(piece) + j] == 1 && piece[i][j] == 1) {

                    if (i == 2) {
                        System.out.println("Can't move down");
                        return false;
                    }
                    if (piece[i + 1][j] != 1) {
                        System.out.println("Can't move down");
                        return false;
                    }
                }
                nextBoardRow--;

            }
        }

        return canMoveDown;

    }

    /**
     * This function is used to check if the piece can move right. It iterates from the first non-zero column from the
     * right to the last relevant column to check from the right. It checks if the next position of each populated cell
     * of the piece will be empty in the board. It takes into account the cases where the piece is only one column,
     * and the cases where the piece can encounter a 1 because of the shape of the piece itself.
     *
     * @return True if the piece can move right, false if it can't
     */
    static boolean checkIfPieceCanMoveRight() {

        boolean canMoveRight = true;
        int nextBoardColumn = xPosition + 1;


        for (int j = firstNonZeroFromRight(piece); j >= lastColumnToCheckFromRight(); j--) {

            int xIteration = 0;


            for (int i = piece.length - 1; i >= firstNonZeroFromAbove(piece); i--) {
                if (board[yPosition - xIteration][nextBoardColumn] == 1 && piece[i][j] == 1) {

                    if (j == 2) {
                        return false;
                    }
                    if (piece[i][j + 1] != 1) {
                        return false;
                    }
                }
                xIteration++;

            }
            nextBoardColumn--;


        }

        return canMoveRight;
    }

    /**
     * This function is used to check if the piece can move left. It iterates from the first column of the piece to
     * the last relevant column to check from the left. It checks if the next position of each populated cell of the
     * piece will be empty in the board. It takes into account the cases where the piece is only one column, and the
     * cases where the piece can encounter a 1 because of the shape of the piece itself.
     *
     * @return True if the piece can move left, false if it can't
     */
    static boolean checkIfPieceCanMoveLeft() {
        boolean canMoveLeft = true;
        int nextBoardColumn = spaces_left - 1;

        for (int j = 0; j < lastColumnToCheckFromLeft() + 1; j++) {
            int xIteration = 0;
            for (int i = piece.length - 1; i >= firstNonZeroFromAbove(piece); i--) {
                if (board[yPosition - xIteration][nextBoardColumn] == 1 && piece[i][j] == 1) {

                    if (j == 0) {
                        System.out.println("Can't move left");
                        return false;
                    }
                    if (piece[i][j - 1] != 1) {
                        System.out.println("Can't move left");
                        return false;
                    }
                }
                xIteration++;

            }
            nextBoardColumn++;
        }
        return canMoveLeft;
    }


    /**
     * This function is a hub for the piece movement. It catches the input and calls the corresponding function
     * depending on the input. It's also recursive, so it will call itself until the piece can't move down anymore
     */
    static void movePieceOnBigBoard() {
        if (checkIfPieceCanMoveDown()) {
            // catch input
            clearConsole();
            printPresentation(false, false, true);
            printBoard(stagingBoard);
            printBoard(board);
            Scanner scanner = new Scanner(System.in);
            System.out.print("\n⤍ Select option: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("s")) {
                movePieceDown();
            } else if (input.equalsIgnoreCase("d")) {
                movepiecerightonbigboard();
            } else if (input.equalsIgnoreCase("a")) {
                movePieceLeftOnBigBoard();
            } else {
                movePieceOnBigBoard();
            }
        }
    }


    /**
     * This function is used to move the piece down. It iterates over the piece 1's and moves them down one position
     * if they can be moved down. Once the row is moved, it clears the row above. It also updates the coordinates of
     * the piece in the main board
     */
    static void movePieceDown() {
        try {
            int nextRow = yPosition + 1;
            int xIterations = piece.length - 1;
            for (int i = nextRow; i >= nextRow - piece.length + firstNonZeroFromAbove(piece) + 1; i--) {
                int yIterations = 0;

                for (int j = spaces_left; j < spaces_left + firstNonZeroFromRight(piece) + 1; j++) {
                    // Displace only the 1s and check the next block isn't a 1
                    if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                        board[i][j] = piece[xIterations][yIterations];
                        board[i - 1][j] = 0;


                    }
                    yIterations++;
                }
                xIterations--;
            }


            yPosition += 1;
            movePieceOnBigBoard();
        } catch (Exception e) {
            System.out.println("End reached");
        }
    }

    /**
     * This function is used to move the piece right. It iterates over the piece 1's and moves them right one position
     * if they can be moved right. Once the row is moved, it clears the row to the left. It also updates the coordinates
     * of the piece in the main board
     */
    static void movepiecerightonbigboard() {
        try {
            if (checkIfPieceCanMoveRight()) {
                // catch input

                int xIterations = piece.length - 1;
                int nextColumn = xPosition + 1;
                for (int i = yPosition; i >= yPosition - piece.length + firstNonZeroFromAbove(piece) + 1; i--) {
                    int yIterations = firstNonZeroFromRight(piece);
                    for (int j = nextColumn; j >= nextColumn - firstNonZeroFromRight(piece); j--) {
                        if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                            board[i][j] = board[i][j - 1];
                            board[i][j - 1] = 0;

                        }
                        yIterations--;

                    }
                    xIterations--;

                }

                spaces_left += 1;
                spaces_right -= 1;

                xPosition += 1;
                movePieceOnBigBoard();
            }
            else{
                movePieceOnBigBoard();
            }
        } catch (Exception ignored) {
            movePieceOnBigBoard();
        }
    }

    /**
     * This function is used to move the piece left. It iterates over the piece 1's and moves them left one position
     * if they can be moved left. Once the row is moved, it clears the row to the right. It also updates the coordinates
     * of the piece in the main board
     */
    static void movePieceLeftOnBigBoard() {
        try {
            if (checkIfPieceCanMoveLeft()) {
                // catch input
                System.out.println("Can move left");

                int xIterations = piece.length - 1;
                int nextColumn = spaces_left - 1;
                for (int i = yPosition; i >= yPosition - piece.length + firstNonZeroFromAbove(piece) + 1; i--) {
                    int yIterations = 0;
                    System.out.println(firstNonZeroFromRight(piece));
                    for (int j = nextColumn; j <= nextColumn + firstNonZeroFromRight(piece); j++) {
                        if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                            board[i][j] = board[i][j + 1];
                            board[i][j + 1] = 0;

                        }
                        yIterations++;

                    }
                    xIterations--;

                }

                spaces_left -= 1;
                spaces_right += 1;
                xPosition -= 1;
                movePieceOnBigBoard();
            }
            else{
                movePieceOnBigBoard();
            }
        } catch (Exception ignored) {
            movePieceOnBigBoard();
        }
    }

    /**
     * This function is used to set the spaces_left variable to an arbitrary value.
     *
     * @param spaces Arbitrary value to be set to spaces_left
     */
    static void setArbitrarySpacesLeft(int spaces) {
        spaces_left = spaces;
    }

    /**
     * This function is used to get the last relevant row to check when displacing the piece down. It iterates from
     * below to above and checks if the row is full. If it is, it returns the row number. Returns the row number - 1
     * because is more convenient when iterating over the piece
     *
     * @return Last non-full row - 1
     */
    private static int lastRowToCheckFromBottom() {
        // Iterate from below to above and check if the row is full. If it is, return the row number. Iterate
        // only from 0 to fullnesOfPiece() + 1
        int lastRowToCheck = piece.length - 1;
        for (int j = piece.length - 1; j >= firstNonZeroFromAbove(piece); j--) {
            boolean isFull = true;
            for (int i = 0; i < firstNonZeroFromRight(piece) + 1; i++) {

                if (piece[j][i] == 0 && lastRowToCheck >= j) {
                    lastRowToCheck = j;
                    isFull = false;
                }

            }
            if (isFull) {
                break;
            }
        }
        return lastRowToCheck - 1;
    }

    /**
     * This function is used to get the last relevant column to check when displacing the piece right. It iterates from
     * right to left and checks if the column is full. If it is, it returns the column number - 1. Returns the column
     * number - 1 because is more convenient when iterating over the piece
     *
     * @return Last non-full from right to left - 1
     */

    private static int lastColumnToCheckFromRight() {
        int lastColumnToCheck = 2;
        for (int j = firstNonZeroFromRight(piece); j >= 0; j--) {
            boolean isFull = true;
            for (int i = firstNonZeroFromAbove(piece); i < piece.length; i++) {

                if (piece[i][j] == 0 && lastColumnToCheck >= j) {
                    lastColumnToCheck = j;
                    isFull = false;
                }

            }

            if (j == 0) {
                return 0;
            }

            if (isFull) {
                break;
            }

        }
        return lastColumnToCheck - 1;
    }

    /**
     * This function is used to get the last relevant column to check when displacing the piece left. It iterates from
     * left to right and checks if the column is full. If it is, it returns the column number + 1. Returns the column
     * number + 1 because is more convenient when iterating over the piece
     *
     * @return Last non-full from left to right + 1
     */
    private static int lastColumnToCheckFromLeft() {
        int lastColumnToCheck = 0;
        for (int j = 0; j < firstNonZeroFromRight(piece) + 1; j++) {
            boolean isFull = true;
            for (int i = firstNonZeroFromAbove(piece); i < piece.length; i++) {
                if (piece[i][j] == 0 && lastColumnToCheck <= j) {
                    lastColumnToCheck = j;
                    isFull = false;
                }

            }

            if (j == 2) {
                return 2;
            }

            if (isFull) {
                break;
            }

        }
        return lastColumnToCheck + 1;
    }

    /**
     * This function is used to clear a row and displace all the rows above one position down
     *
     * @param row Row to be cleared
     */
    static void clearRowAndDisplace(int row) {
        Arrays.fill(board[row], 0);
        for (int i = row; i > 0; i--) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = board[i - 1][j];
            }
        }
        points += 1;
    }

    /**
     * This function is used to check if a row is full. It iterates over the row and checks if there is a 0. If there
     * is, continue iterating. If there isn't, calls clearRowAndDisplace() to clear the row and displace all the rows
     *
     * @return True if the row is full, false if it isn't
     */
    static void checkIfRowIsFull() {
        for (int i = 0; i < board.length; i++) {
            boolean isFull = true;
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0) {
                    isFull = false;
                    break;
                }

            }
            if (isFull) {
                clearRowAndDisplace(i);
            }

        }
    }

    /**
     * This function is used to check if a piece can be placed in the main board. It iterates over the piece and checks
     * if there is a 1 in the board in the same position as the piece
     *
     * @param from  Row where the piece is going to be placed
     * @param piece Piece to be placed in the board
     * @return True if the piece can be placed, false if it can't
     */
    static boolean checkIfPieceCanBePlacedOnBigBoard(int from, int[][] piece) {
        boolean canMoveDown = true;
        // Iterate from 0 to piece.length - 1 - firstNonZeroFromAbove(piece) and check if there is a 1 in the
        // board in the same position as the piece
        int fromBoardVertical = 0;

        for (int i = firstNonZeroFromAbove(piece); i < piece.length; i++) {
            int fromBoardHorizontal = from;
            for (int j = 0; j <= firstNonZeroFromRight(piece); j++) {
                if (board[fromBoardVertical][fromBoardHorizontal] == 1 && piece[i][j] == 1) {
                    return false;
                }
                fromBoardHorizontal++;
            }
            fromBoardVertical++;
        }
        return canMoveDown;
    }

    /**
     * This function is used to check if the game is over. It simulates the placement of the next piece in the board
     * in all the posible positions at the beginning. If the piece can be placed, it returns false. If it can't, it will
     * check for the rotated version of the piece. If the rotated version can be placed, it returns false. If it can't,
     * it will return true and the game will be over
     *
     * @param complexityFlag If true, it will check for the rotated version of the piece because the game is in
     *                       advanced mode
     * @return True if the game is over, false if the piece can be placed
     */
    static boolean checkIfIsGameOver(boolean complexityFlag) {
        boolean gameOver = true;
        for (int i = 0; i < board[0].length - firstNonZeroFromRight(bundleOfPieces[0]); i++) {
            if (checkIfPieceCanBePlacedOnBigBoard(i, bundleOfPieces[0])) return false;
        }
        if (complexityFlag) {
            for (int i = 0; i < board[0].length - firstNonZeroFromRight(bundleOfPieces[1]); i++) {
                if (checkIfPieceCanBePlacedOnBigBoard(i, bundleOfPieces[1])) return false;
            }
        }


        return gameOver;
    }

    /**
     * Giver a starting point in the board, which is the upper left corner of the piece, this function will check if
     * the piece can be placed in the board. It will iterate over the piece and check if there is a 1 in the board
     * in the same position as the piece.
     *
     * @param fromVertical   Upper row of the space to be checked
     * @param fromHorizontal Left column of the space to be checked
     * @param piece          Piece to be placed in the board
     * @return True if the piece can be placed, false if it can't
     */
    static boolean individualBasicCheckingAlgorithm(int fromVertical, int fromHorizontal, int[][] piece) {
        boolean canMoveDown = true;


        for (int i = firstNonZeroFromAbove(piece); i < piece.length; i++) {
            int fromBoardHorizontal = fromHorizontal;
            for (int j = 0; j <= firstNonZeroFromRight(piece); j++) {

                if (board[fromVertical][fromBoardHorizontal] == 1 && piece[i][j] == 1) {
                    return false;
                }

                fromBoardHorizontal++;
            }
            fromVertical++;
        }
        return canMoveDown;
    }

    /**
     * Iterates over the board from the bottom to the top, from the left to the right. It checks if the piece can be
     * placed in each position. If it can, it will place the piece in the lowest position possible.
     *
     * @param piece Piece to be placed in the board
     */

    static void wholeBoardBasicCheckingAlgorithm2(int[][] piece) {
        boolean canBePlaced = false;
        /*  Each time a piece can be placed, the coordinates are updated. Once the checking method returns false,
            the piece is placed in the last position where it could be placed
         */
        int lastXCoord = 0;
        int lastYCoord = 0;
        for (int i = 0; i < board.length - piece.length + 1 + firstNonZeroFromAbove(piece); i++) {
            for (int j = board[i].length - firstNonZeroFromRight(piece) - 1; j >= 0; j--) {
                if (individualBasicCheckingAlgorithm(i, j, piece) && !checkIfSpaceIsBlocked(i, j)) {
                    canBePlaced = true;
                    lastYCoord = j;
                    lastXCoord = i;
                }
                ;
            }
        }
        if (canBePlaced) {
            placePieceOnBigBoardBasicAlgorithm(piece, lastXCoord, lastYCoord);
            piecesPlaced += 1;
        }

    }

    /**
     * This function is used to place a piece in the board. It iterates over the piece and places each 1 in the
     * corresponding position in the board.
     *
     * @param piece          Piece to be placed in the board
     * @param fromVertical   Upper row of the space to be checked
     * @param fromHorizontal Left column of the space to be checked
     */
    static void placePieceOnBigBoardBasicAlgorithm(int[][] piece, int fromVertical, int fromHorizontal) {
        int xIterations = firstNonZeroFromAbove(piece);
        int pieceLength = piece.length - 1 - firstNonZeroFromAbove(piece);

        for (int i = fromVertical; i <= fromVertical + pieceLength; i++) {

            int yIterations = 0;
            for (int j = fromHorizontal; j < fromHorizontal + firstNonZeroFromRight(piece) + 1; j++) {

                if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                    board[i][j] += piece[xIterations][yIterations];
                }

                yIterations++;
            }
            xIterations++;

        }
    }

    /**
     * This function is used to check if a space is blocked when the piece were to be lowered. It iterates over the
     * board from the bottom to the top, from the left to the right. It checks if there is a 1 in the board in the
     * same column as the piece.
     *
     * @param fromVertical   Upper row of the space to be checked
     * @param fromHorizontal Left column of the space to be checked
     * @return True if the space is blocked, false if it isn't
     */
    public static boolean checkIfSpaceIsBlocked(int fromVertical, int fromHorizontal) {
        for (int i = fromVertical; i >= 0; i--) {
            for (int j = fromHorizontal; j <= fromHorizontal + firstNonZeroFromRight(piece); j++) {
                if (board[i][j] == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void printPresentation(boolean gameOverFlag, boolean cantPlacePieceFlag, boolean complexityFlag) {
        // Make a line banner with the name of the game and some decoratios around
        System.out.println("▄▀▀▀▀▀▀▀▀▀  TETRIS  ▀▀▀▀▀▀▀▀▀▀▀▄");
        System.out.println();

        // Print the instructions
        System.out.println("  Instructions:            \n");
        if(complexityFlag)
        {
            System.out.println("  a: move left             ");
            System.out.println("  d: move right            ");
            System.out.println("  s: move down             ");
            System.out.println("  r: rotate                ");
        }
        else{
            System.out.println("  -> Press enter to place a piece             ");
        }
        System.out.println();
        System.out.println("  Points: " + points + "                ");
        System.out.println("  Pieces placed: " + piecesPlaced + "         ");
        if (cantPlacePieceFlag) {
            // Print the game over message in red
            System.out.println("  \u001B[93mCan't place piece\u001B[0m        ");
        }
        if (gameOverFlag) {
            // Print the game over message in red
            System.out.println("  \u001B[31mGame Over\u001B[0m                ");
        }
        System.out.println();
        System.out.println("▀▄▄▄▄▄▄▄▄▄▄ ▄▄ ▄▄ ▄▄ ▄▄▄▄▄▄▄▄▄▄▀");
        System.out.println();
    }

}





