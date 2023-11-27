
// Tetris game

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // create a global variable
    private static int spaces_left = 0;
    private static int spaces_right = 0;

    private static int xPosition = 0;
    private static int yPosition = 0;
    //TODO: update spaces_right everytime a piece is placed in the board
    private static boolean rotated = false;

    private static int[][] board = new int[8][8];
    private static int[][] stagingBoard = new int[3][8];

    private static int[][][] bundleOfPieces;
    private static int[][] piece;

    private static boolean pieceOutOfBounds = false;


    public static void main(String[] args) {
        generateAndPlacePiece();
        while (true) {
            clearConsole();
            updateSpacesLeft();
            updateSpacesRight();
            print_board(stagingBoard);
            print_board(board);
            userMovement();
            System.out.println(xPosition + " " + yPosition);
        }
//        generate_piece();
//        piece = bundleOfPieces[0];
//        placeStagingPiece(0);
//        print_board(stagingBoard);
//        print_board(board);
//        updateSpacesLeft();
//        updateSpacesRight();
//        while (true) {
//            userMovement();
//            updateSpacesLeft();
//            updateSpacesRight();
//            print_board(stagingBoard);
//            print_board(board);
//
//            System.out.println("Spaces right: " + spaces_right);
//            System.out.println("Spaces left: " + spaces_left);
//            System.out.println("Piece out of bounds: " + pieceOutOfBounds);
////            System.out.println("Spaces down: " + Arrays.toString(countSpacesDown()));
//        }


    }

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
        int[][][][] pieces = {{piece_1, piece_1_rotated}, {piece_2, piece_2_rotated}, {piece_3, piece_3_rotated}, {piece_4, piece_4_rotated}, {piece_5, piece_5_rotated}, {piece_6}};

        // generate random number between 0 and 5
        int random_number = (int) (Math.random() * 6);

        bundleOfPieces = pieces[random_number];
//        bundleOfPieces = pieces[3];
    }

    private static void generateAndPlacePiece() {
        generate_piece();
        piece = bundleOfPieces[0];
        setArbitrarySpacesLeft(0);
        placeStagingPiece(0);
        setCoords(0, 0);
    }


    private static void placeStagingPiece(int added_spaces) {
        // Iterate over the stagging board and place the piece in the correct position
        // Sum the added spaces to the left_spaces and place the piece in "j" column + added_spaces
        // E.g: if added_spaces = 1, start placing the piece in the "j + 1 + left_spaces" column
        clearBoard(stagingBoard);
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                try {
                    if (piece[i][j] == 1) {
                        stagingBoard[i][j + spaces_left + added_spaces] = piece[i][j];
                    }

                    pieceOutOfBounds = false;

                } catch (Exception ignored) {
                    pieceOutOfBounds = true;
                }
            }
        }

    }

    private static void print_board(int board[][]) {

        // Iterate over the board and print it

        for (int i = 0; i < board.length; i++) {
            // Replace 1 with ◼, and 0 with -
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1) {
                    System.out.print(" ◼ ");
                } else {
                    System.out.print(" - ");
                }
            }
            // Print new empty line to separate rows
            System.out.println();
        }
    }

    public static void rotate() {
        if (rotated) {
            rotated = false;
            piece = bundleOfPieces[0];
        } else {
            rotated = true;
            piece = bundleOfPieces[1];
        }
    }

    static void clearBoard(int[][] usedBoard) {
        // Iterate over the board and set all the values to 0
        for (int i = 0; i < usedBoard.length; i++) {
            Arrays.fill(usedBoard[i], 0);
        }
    }

    static void userMovement() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if (input.equalsIgnoreCase("a") && spaces_left > 0) {
            placeStagingPiece(-1);
        } else if (input.equalsIgnoreCase("d") && spaces_right > 0) {
            placeStagingPiece(1);
        } else if (input.equalsIgnoreCase("w")) {
            rotate();
        } else if (input.equalsIgnoreCase("s")) {
            clearBoard(stagingBoard);
            PlacePieceBoardBig(board, piece, 2);
            clearConsole();
            print_board(stagingBoard);
            print_board(board);
//            movePieceRightOnBigBoard();
            movePieceOnBigBoard();
            generateAndPlacePiece();
        } else if (input.equalsIgnoreCase("r")) {
            rotate();
            placeStagingPiece(0);

            if (pieceOutOfBounds) {
                readjustPiece();
            }
        } else if (input.equalsIgnoreCase("q")) {
            PlacePieceBoardBig(board, piece, Arrays.stream(countSpacesDown()).min().getAsInt() - 1);
        }
    }


    static void clearConsole() {
        // Clear the console using ANSI escape codes
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


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

    static void readjustPiece() {
        clearBoard(stagingBoard);
        pieceOutOfBounds = false;
        placeStagingPiece(-1);

    }

    static int firstNonZeroFromRight() {
        // Count the number of columns with 1s in the piece
        int counter = 0;
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1 && j > counter)
                    counter = j;
            }
        }
        return counter;
    }

    static int firstNonZeroFromAbove() {
        int counter = piece.length - 1;
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                if (piece[i][j] == 1 && i < counter)
                    counter = i;
            }
        }
        return counter;
    }

    static int[] countSpacesDown() {
        int[] spacesDown = new int[firstNonZeroFromRight() + 1];
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

    static void PlacePieceBoardBig(int[][] board, int[][] piece, int from) {
        int xIterations = 0;
        int pieceLength = piece.length - 1;

        for (int i = from - pieceLength; i <= from; i++) {

            int yIterations = 0;
            for (int j = spaces_left; j < spaces_left + firstNonZeroFromRight() + 1; j++) {
                board[i][j] += piece[xIterations][yIterations];
                setCoords(i, j);

                yIterations++;
            }
            xIterations++;

        }
    }

    static void setCoords(int x, int y) {
        xPosition = x;
        yPosition = y;

    }

    static int[] onesPosition(int row) {
        // Iterate over the bottom row of the piece and save the position of the 1s
        // from left to right
        int[] onesPosition = new int[firstNonZeroFromRight() + 1];
        for (int i = 0; i < onesPosition.length; i++) {
            if (piece[row][i] == 1) {
                onesPosition[i] = 1;
            }
        }
        return onesPosition;
    }

    static int[] onesPositionAtTheRight() {
        // Iterate over the right column of the piece and save the position of the 1s
        // from top to bottom
        int[] onesPosition = new int[piece.length];
        int lastColumn = firstNonZeroFromRight();
        for (int i = 0; i < onesPosition.length; i++) {
            if (piece[i][lastColumn] == 1) {
                onesPosition[i] = 1;
            }
        }
        return onesPosition;
    }

    static boolean checkIfPieceCanMoveDown() {
        // Iterate over the bottom row of the piece and check if the position of the 1s
        // from left to right is empty in the board


        boolean canMoveDown = true;

        for (int j = 0; j < firstNonZeroFromRight() + 1; j++) {

            int nextBoardRow = xPosition + 1;

            for (int i = piece.length - 1; i >= lastRowToCheckFromBottom(); i--) {

                if (board[nextBoardRow][yPosition - firstNonZeroFromRight() + j] == 1 && piece[i][j] == 1) {
                    // I'm not proud of this, but it works

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

    static boolean checkIfPieceCanMoveRight() {
        // Iterate over the right column of the piece and check if the position of the 1s
        // from top to bottom is empty in the board
        boolean canMoveRight = true;
        int nextBoardColumn = yPosition + 1;

        System.out.println("Last column to check: " + lastColumnToCheckFromRight());


        for (int j = firstNonZeroFromRight(); j >= lastColumnToCheckFromRight(); j--) {
            System.out.println("Checking piece column: " + j);
            // lastColumnToCheckFromRight()
            int xIteration = 0;


            for (int i = piece.length - 1; i >= firstNonZeroFromAbove(); i--) {
                System.out.println(firstNonZeroFromAbove());
                System.out.println("Checking board piece: " + (xPosition - xIteration) + " " + nextBoardColumn);
                System.out.println("Checking piece position: " + i + " " + j);
                if (board[xPosition - xIteration][nextBoardColumn] == 1 && piece[i][j] == 1) {
                    // I'm not proud of this, but it works

                    if (j == 2) {
                        System.out.println("Can't move right");
                        return false;
                    }
                    if (piece[i][j + 1] != 1) {
                        System.out.println("Can't move right");
                        return false;
                    }
                }
                xIteration++;

            }
            nextBoardColumn--;


        }

        return canMoveRight;
    }


    static void movePieceOnBigBoard() {
        if (checkIfPieceCanMoveDown()) {
            // catch input
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("s")) {
                movePieceDown();
            } else if (input.equalsIgnoreCase("d")) {
                movepiecerightonbigboard();
            }
        }
    }


    static void movePieceDown() {
        try {
            int nextRow = xPosition + 1;
            int xIterations = piece.length - 1;
            for (int i = nextRow; i > nextRow - piece.length; i--) {
                int yIterations = 0;

                for (int j = spaces_left; j < spaces_left + firstNonZeroFromRight() + 1; j++) {
                    // Displace only the 1s and check the next block isn't a 1
                    if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                        board[i][j] = piece[xIterations][yIterations];
                        board[i - 1][j] = 0;


                    }
                    yIterations++;
                }
                xIterations--;
            }

            clearConsole();
            print_board(stagingBoard);
            print_board(board);
            xPosition += 1;
            movePieceOnBigBoard();
        } catch (Exception e) {
            System.out.println("End reached");
        }
    }

    static void movepiecerightonbigboard() {
        try {
            if (checkIfPieceCanMoveRight()) {
                // catch input
                System.out.println("Can move right");

                int xIterations = piece.length - 1;
                int nextColumn = yPosition + 1;
                for (int i = xPosition; i >= xPosition - piece.length + 1; i--) {
                    int yIterations = firstNonZeroFromRight();
                    for (int j = nextColumn; j >= nextColumn - firstNonZeroFromRight(); j--) {
                        if (board[i][j] == 0 && piece[xIterations][yIterations] == 1) {
                            board[i][j] = board[i][j - 1];
                            board[i][j - 1] = 0;

                        }
                        yIterations--;

                    }
                    xIterations--;

                }
                clearConsole();
                spaces_left += 1;
                spaces_right -= 1;
                print_board(stagingBoard);
                print_board(board);
                yPosition += 1;
                movePieceOnBigBoard();
            }
        } catch (Exception ignored) {
        }
    }

    static void setArbitrarySpacesLeft(int spaces) {
        spaces_left = spaces;
    }


    private static int lastRowToCheckFromBottom() {
        // Iterate from below to above and check if the row is full. If it is, return the row number. Iterate
        // only from 0 to fullnesOfPiece() + 1
        int lastRowToCheck = piece.length - 1;
        for (int j = piece.length - 1; j >= firstNonZeroFromAbove(); j--) {
            boolean isFull = true;
            for (int i = 0; i < firstNonZeroFromRight() + 1; i++) {

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
}





