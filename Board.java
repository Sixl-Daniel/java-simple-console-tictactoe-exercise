package tictactoe;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Board {

    private char[][] fields;
    private String symbolsAllowed = "oO0xX_";
    private int round;
    private String status;

    public boolean active;


    public Board() {
        setupBoard("_________");
    }
    public Board(String configuration) {
        setupBoard(configuration);
    }

    public String toString() {
        String tableLine = "═══════", tableTL = "╔", tableTR = "╗", tableBL = "╚", tableBR = "╝", tableV = "║", nl = "\n";
        String row1 = new String(this.fields[0]).replace("", " ").replace("_", " ");
        String row2 = new String(this.fields[1]).replace("", " ").replace("_", " ");
        String row3 = new String(this.fields[2]).replace("", " ").replace("_", " ");
        return
            tableTL + tableLine + tableTR + nl +
            tableV + row1 + tableV + nl +
            tableV + row2 + tableV + nl +
            tableV + row3 + tableV + nl +
            tableBL + tableLine + tableBR + nl;
    }

    public void print() {
        System.out.print(this);
    }

    public void printStatus() {
        System.out.print(this.status);
    }

    private void setupBoard(String configuration) {

        this.active = true;
        this.round = 0;

        configuration = configuration.toUpperCase().replaceAll(" ", "_").replaceAll("0", "O").replaceAll("[^OX_]", "");

        if (configuration.length() < 9) {
            throw new IllegalArgumentException("Error in board configuration string.");
        }

        configuration = configuration.substring(0, 9);

        this.fields = new char[3][3];
        int position = 0;

        for (int row = 0; row < this.fields.length; row++) {
            for (int col = 0; col < this.fields[row].length; col++) {
                this.fields[row][col] = configuration.charAt(position);
                position++;
            }
        }

        this.determineBoardStatus();

    }

    private void determineBoardStatus() {

        int countX = 0;
        int countO = 0;
        int countE = 0;

        int[] rowCountX = {0, 0, 0};
        int[] rowCountO = {0, 0, 0};
        int[] colCountX = {0, 0, 0};
        int[] colCountO = {0, 0, 0};

        boolean xHasLine = false;
        boolean oHasLine = false;

        for (int row = 0; row < this.fields.length; row++) {
            for (int col = 0; col < this.fields[row].length; col++) {
                char field = this.fields[row][col];
                if (field == 'X') {
                    countX++;
                    rowCountX[row]++;
                    colCountX[col]++;
                } else if (field == 'O') {
                    countO++;
                    rowCountO[row]++;
                    colCountO[col]++;
                } else {
                    countE++;
                }
            }
        }

        /* checks */

        for(int e : rowCountX) {
            if(e == 3) xHasLine =  true;
        }

        for(int e : colCountX) {
            if(e == 3) xHasLine =  true;
        }

        for(int e : rowCountO) {
            if(e == 3) oHasLine =  true;
        }

        for(int e : colCountO) {
            if(e == 3) oHasLine =  true;
        }

        if ( fields[1][1] == 'X' ) {
            if ( fields[0][0] == 'X' && fields[2][2] == 'X' || fields[2][0] == 'X' && fields[0][2] == 'X' ) {
                xHasLine =  true;
            }
        }

        if ( fields[1][1] == 'O' ) {
            if ( fields[0][0] == 'O' && fields[2][2] == 'O' || fields[2][0] == 'O' && fields[0][2] == 'O' ) {
                oHasLine =  true;
            }
        }

        /* determine and return status */

        String status = "Error determining status";

        if (xHasLine && oHasLine || Math.abs(countX - countO) > 1) {
            status = "Impossible";
        } else if (xHasLine) {
            status = "X wins";
        } else if (oHasLine) {
            status = "O wins";
        } else if(countE > 0) {
            status = "Game not finished";
        } else if(countE == 0) {
            status = "Draw";
        }

        this.status = status;

    }

    private void setField(int col, int row, char c) throws SetFieldException {

        // check parameter

        if(this.symbolsAllowed.indexOf(c) == -1) throw new SetFieldException("Symbol not allowed. Please use [X] or [O] as third parameter.");
        if(col < 1 || col > 3) throw new SetFieldException("Column must be between [1] and [3].");
        if(row < 1 || row > 3) throw new SetFieldException("Row must be between [1] and [3].");

        // change row logic for user (turn board)

        row = 4 - row;
        char field = this.fields[row-1][col-1];

        // check if field is taken

        if (field != '_') throw new SetFieldException("The field [" + row + "/" + col + "] is already taken.");

        // move

        c = Character.toUpperCase(c);
        c =  (c == '0') ? 'O' : c;
        this.fields[row-1][col-1] = c;

        // get status and end game (set active to false) if necessary

        this.determineBoardStatus();
        if(this.status != "Game not finished") {
            this.active = false;
        }

        // next round

        this.round++;

    }

    public void getUserInput() throws SetFieldException {
        String player = this.round % 2 == 0 ? "X" : "O";
        System.out.print("Player " + player + ", enter your coordinates: ");
        Scanner s = new Scanner(System.in);
        String[] input = s.nextLine().split(" ");
        this.setField(Integer.valueOf(input[0]), Integer.valueOf(input[1]));
    }

    public void setField(int col, int row) throws SetFieldException {
        if(this.round % 2 == 0) {
            this.setField(col, row, 'X');
        } else {
            this.setField(col, row, 'O');
        }
    }

}
