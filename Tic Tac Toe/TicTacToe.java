import java.util.Scanner;

public class TicTacToe {

    static final int COMPUTER = 1;
    static final int HUMAN = 2;
    static final int SIDE = 3;
    static final char COMPUTERMOVE = 'O';
    static final char HUMANMOVE = 'X';

    static Scanner scanner = new Scanner(System.in);

    // Show the board in 3x3 grid format
    static void show_board(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            System.out.print("\t");
            for (int j = 0; j < SIDE; j++) {
                System.out.print(board[i][j]);
                if (j < SIDE - 1)
                    System.out.print(" | ");
            }
            System.out.println();
            if (i < SIDE - 1)
                System.out.println("\t" + "---------");
        }
    }

    // Show cell numbers guide to users
    static void show_instructions() {
        System.out.println("\nChoose a cell numbered from 1 to 9 as below and play\n");
        System.out.println("\t 1 | 2 | 3 ");
        System.out.println("\t-----------");
        System.out.println("\t 4 | 5 | 6 ");
        System.out.println("\t-----------");
        System.out.println("\t 7 | 8 | 9 \n");
    }

    // Initialize a 3x3 board filled with '*'
    static char[][] initialize() {
        char[][] board = new char[SIDE][SIDE];
        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                board[i][j] = '*';
            }
        }
        return board;
    }

    // Announce winner
    static void declare_winner(int whose_turn) {
        if (whose_turn == COMPUTER) {
            System.out.println("COMPUTER has won\n");
        } else {
            System.out.println("HUMAN has won\n");
        }
    }

    static boolean row_crossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[i][0] == board[i][1] &&
                board[i][1] == board[i][2] &&
                board[i][0] != '*') {
                return true;
            }
        }
        return false;
    }

    static boolean column_crossed(char[][] board) {
        for (int i = 0; i < SIDE; i++) {
            if (board[0][i] == board[1][i] &&
                board[1][i] == board[2][i] &&
                board[0][i] != '*') {
                return true;
            }
        }
        return false;
    }

    static boolean diagonal_crossed(char[][] board) {
        if (board[0][0] == board[1][1] &&
            board[1][1] == board[2][2] &&
            board[0][0] != '*') {
            return true;
        }
        if (board[0][2] == board[1][1] &&
            board[1][1] == board[2][0] &&
            board[0][2] != '*') {
            return true;
        }
        return false;
    }

    static boolean game_over(char[][] board) {
        return row_crossed(board) || column_crossed(board) || diagonal_crossed(board);
    }

    // Minimax algorithm for finding the optimal move
    static int minimax(char[][] board, int depth, boolean is_ai) {
        if (game_over(board))
            return is_ai ? -10 : 10;

        if (depth == 9)
            return 0;

        int best_score = is_ai ? -9999 : 9999;

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == '*') {
                    board[i][j] = is_ai ? COMPUTERMOVE : HUMANMOVE;

                    int score = minimax(board, depth + 1, !is_ai);
                    board[i][j] = '*';

                    best_score = is_ai ? Math.max(best_score, score)
                                       : Math.min(best_score, score);
                }
            }
        }

        return best_score;
    }

    // Calculate the best move for COMPUTER
    static int[] best_move(char[][] board, int move_index) {
        int best_score = -9999;
        int x = -1, y = -1;

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                if (board[i][j] == '*') {
                    board[i][j] = COMPUTERMOVE;
                    int score = minimax(board, move_index + 1, false);
                    board[i][j] = '*';

                    if (score > best_score) {
                        best_score = score;
                        x = i;
                        y = j;
                    }
                }
            }
        }

        return new int[]{x, y};
    }

    // Main gameplay function between HUMAN and COMPUTER
    static void play_tic_tac_toe(int whose_turn) {
        char[][] board = initialize();
        int move_index = 0;
        show_instructions();

        while (!game_over(board) && move_index < SIDE * SIDE) {
            if (whose_turn == COMPUTER) {
                int[] move = best_move(board, move_index);
                int x = move[0], y = move[1];
                board[x][y] = COMPUTERMOVE;

                System.out.println("COMPUTER has put an " + COMPUTERMOVE + " in cell " + (x * 3 + y + 1) + "\n");
                show_board(board);

                whose_turn = HUMAN;
            } else {
                System.out.print("Available positions: ");
                for (int i = 0; i < SIDE; i++) {
                    for (int j = 0; j < SIDE; j++) {
                        if (board[i][j] == '*') {
                            System.out.print((i * 3 + j + 1) + " ");
                        }
                    }
                }
                System.out.println();

                try {
                    System.out.print("Enter the position: ");
                    int n = Integer.parseInt(scanner.nextLine()) - 1;
                    int x = n / SIDE;
                    int y = n % SIDE;

                    if (n >= 0 && n < 9 && board[x][y] == '*') {
                        board[x][y] = HUMANMOVE;
                        System.out.println("\nHUMAN has put an " + HUMANMOVE + " in cell " + (n + 1) + "\n");
                        show_board(board);

                        whose_turn = COMPUTER;
                    } else {
                        System.out.println("Invalid or occupied position, try again.");
                        continue;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input, enter a number between 1-9.");
                    continue;
                }
            }
            move_index++;
        }

        if (!game_over(board) && move_index == SIDE * SIDE) {
            System.out.println("It's a draw\n");
        } else {
            declare_winner(whose_turn == HUMAN ? COMPUTER : HUMAN);
        }
    }

    // Entry point
    public static void main(String[] args) {
        System.out.println("\n-------------------- Tic-Tac-Toe --------------------\n");

        while (true) {
            System.out.print("Do you want to start first? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            if (choice.equals("y")) {
                play_tic_tac_toe(HUMAN);
            } else if (choice.equals("n")) {
                play_tic_tac_toe(COMPUTER);
            } else {
                System.out.println("Invalid choice");
                continue;
            }

            System.out.print("Do you want to quit? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                break;
            }
        }
    }
}