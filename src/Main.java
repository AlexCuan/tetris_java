
// Tetris game

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // create a global variable
    private static int spaces_left = 0;
    private static int spaces_right = 0;

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
            updateSpacesLeft();
            updateSpacesRight();
            print_board(stagingBoard);
            print_board(board);
            userMovement();
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

        piece = pieces[random_number];
    }

    private static void generateAndPlacePiece() {
        generate_piece();
        piece = bundleOfPieces[0];
        placeStagingPiece(0);
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
        for (int i = 0; i < board.length; i++) {
            // Replace 1 with *, and 0 with -
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
            placeStagingPiece(1);
        } else if (input.equalsIgnoreCase("r")) {
            rotate();
            if (pieceOutOfBounds) {
                readjustPiece();
            } else {
                placeStagingPiece(0);

            }
        }
    }


    static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }


    static void updateSpacesLeft() {
        int counter = stagingBoard[0].length - 1;
        // Iterate over the 3 first lines of the board and save the position of the first 1 from left to right
        // taking into account the thre columns
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
        int counter = 0;
        // Iterate over the 3 first lines of the board and save the position of the first 1 from right to left
        // taking into account the thre columns
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
        clearBoard();
        pieceOutOfBounds = false;
        placeStagingPiece(-1);

    }

    static int fullnesOfPiece() {
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

    static int[] countSpacesDown() {
        int[] spacesDown = new int[fullnesOfPiece() + 1];
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

//    static void shitfDown() {
//        // Shift the piece down the minimum number of countSpacesDown
//        int [] spacesDown = countSpacesDown();
//        int min = Arrays.stream(spacesDown).min().getAsInt();
//        for (int i = spaces_left; i < spacesDown.length + spaces_left; i++) {
//            for (int j = 0; j < min; j++) {
//                board[j][i] = ;
//
//            }
//        }


    }

}