
// Tetris game

public class Main {

    // create a global variable
    private static int spaces_left = 0;

    //TODO: update spaces_right everytime a piece is placed in the board
    private static int spaces_right = 0;
    private static boolean rotated = false;




    public static void main(String[] args) {
        int[][] board = generate_piece();
    }

    private static void test() {
        System.out.println("Test");
    }

    private static int [][] generate_piece(){
        // *
        // *
        // *
        int [][] piece_1 = {{1,0,0}, {1,0,0}, {1,0,0}};

        //
        // *
        // * * *
        int [][] piece_2 = {{0,0,0}, {1,0,0}, {1,1,1}};

        //
        //     *
        // * * *
        int [][] piece_3 = {{0,0,0}, {0,0,1}, {1,1,1}};

        //
        //   *
        // * * *
        int [][] piece_4 = {{0,0,0}, {0,1,0}, {1,1,1}};

        //
        //   * *
        // * *
        int [][] piece_5 = {{0,0,0}, {0,1,1}, {1,1,0}};

        //
        // * *
        // * *
        int [][] piece_6 = {{0,0,0}, {1,1,0}, {1,1,0}};

        // create array of int [][] pieces
        int [][][] pieces = {piece_1, piece_2, piece_3, piece_4, piece_5, piece_6};

        // generate random number between 0 and 5
        int random_number = (int) (Math.random() * 6);

        return pieces[random_number];
    }
}