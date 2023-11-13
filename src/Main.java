
// Tetris game

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // create a global variable
    private static int spaces_left = 0;
    private static int spaces_right = 0;

    //TODO: update spaces_right everytime a piece is placed in the board
    private static boolean rotated = false;

    private static int[][] board = new int[11][8];

    private static int[][] piece;

    private static boolean pieceOutOfBounds = false;


    public static void main(String[] args) {
        generate_piece();
        placeStagingPiece(0);
        print_board();
        updateSpacesLeft();
        while (true) {
            userMovement();
            print_board();
            updateSpacesRight();
            System.out.println("Spaces right: " + spaces_right);
            System.out.println("Spaces left: " + spaces_left);
            System.out.println("Piece out of bounds: " + pieceOutOfBounds);
        }


    }

    private static void generate_piece() {
        // *
        // *
        // *
        int[][] piece_1 = {{1, 0, 0}, {1, 0, 0}, {1, 0, 0}};

        //
        // *
        // * * *
        int[][] piece_2 = {{0, 0, 0}, {1, 0, 0}, {1, 1, 1}};

        //
        //     *
        // * * *
        int[][] piece_3 = {{0, 0, 0}, {0, 0, 1}, {1, 1, 1}};

        //
        //   *
        // * * *
        int[][] piece_4 = {{0, 0, 0}, {0, 1, 0}, {1, 1, 1}};

        //
        //   * *
        // * *
        int[][] piece_5 = {{0, 0, 0}, {0, 1, 1}, {1, 1, 0}};

        //
        // * *
        // * *
        int[][] piece_6 = {{1, 1}, {1, 1}};

        // create array of int [][] pieces
        int[][][] pieces = {piece_1, piece_2, piece_3, piece_4, piece_5, piece_6};

        // generate random number between 0 and 5
        int random_number = (int) (Math.random() * 6);

        piece = pieces[random_number];
    }


    private static void placeStagingPiece(int added_spaces) {
        // sum all the values in the piece array to the board array. Take into account the spaces_left and spaces_right
        spaces_left += added_spaces;
        clearBoard();
        for (int i = 0; i < piece.length; i++) {
            for (int j = 0; j < piece[i].length; j++) {
                try {
                    board[i][j + spaces_left] += piece[i][j];
                } catch (Exception ignored) {
                    pieceOutOfBounds = true;
                }
            }
        }

    }

    private static void print_board() {
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
        int rows = piece.length;
        int column = piece[0].length;

        // Crear una nueva matriz para almacenar la transpuesta
        int[][] rotatedMatrix = new int[column][rows];

        if (!rotated) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < column; j++) {
                    rotatedMatrix[i][j] = piece[rows - j - 1][i];
                }
            }
            rotated = true;

        } else {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < column; j++) {
                    rotatedMatrix[i][j] = piece[j][rows - i - 1];
                }
            }
            rotated = false;

        }


        piece = rotatedMatrix;
    }

    static void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            Arrays.fill(board[i], 0);
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
        int counter = board.length - 1;
        // Iterate over the 3 first lines of the board and save the position of the first 1 from left to right
        // taking into account the thre columns
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 1 && j < counter) {
                    counter = j;
                }
            }
        }

    }

    static void updateSpacesRight() {
        int counter = 0;
        // Iterate over the 3 first lines of the board and save the position of the first 1 from right to left
        // taking into account the thre columns
        for (int i = 0; i < 3; i++) {
            for (int j = board[i].length - 1; j >= 0; j--) {
                if (board[i][j] == 1 && j > counter) {
                    counter = j;
                }
            }
        }
        spaces_right = board[1].length - 1 - counter;
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

    static void shitfDown() {
        // Count the number of columns with 1s in the piece
        int counter = 0;

    }

}