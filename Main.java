package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Scanner s = new Scanner(System.in);

        /* create and print initial board */
        Board board = new Board();
        board.print();

        /* game loop */

        while(board.active) {
            try {
                // get move from user
//                System.out.print("Enter the coordinates: ");
//                String[] input = s.nextLine().split(" ");
                // set and print board
                //board.setField(Integer.valueOf(input[0]), Integer.valueOf(input[1]));
                board.getUserInput();
                board.print();
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("You should enter two numbers between [1] and [3].");
            } catch (SetFieldException e) {
                System.out.println(e.getMessage());
            } finally {
            }
        }

        board.printStatus();

        // s.close();

    }
}