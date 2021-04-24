package core;

import java.util.concurrent.atomic.AtomicInteger;

public class Crossword {
    private static final AtomicInteger stepCount = new AtomicInteger(0);
    private static final int wall = -99;
    private static final int empty = -95;
    private int[][] board = { { 1, empty,-2, empty,-3}, {wall, wall, empty, wall, empty}, {wall, 4, empty, -5, empty}, { -6, wall, 7, empty, empty}, { 8, empty, empty, empty, empty}, {empty, wall, wall, empty, wall} };
    private int placesToFill = 8;
    private final Alphabet[][] selectedCandidates;
    private String[] candidate = {"aft","ale","eel","heel","hike","hoses","keel","knot","laser","lee","line","sails","sheet","steer","tie"};

    /**
     * Default constructor for Crossword
     * */
    public Crossword() {
        selectedCandidates = new Alphabet[6][5];
    }

    public int availableSpace(int candidateNo) {

        int[] coordinates = findStartingPoint(candidateNo);
        int x = coordinates[0];
        int y = coordinates[1];
        boolean checkHorizontal = board[x][y] > 0;
        int max;

        if (checkHorizontal) {
            max = board[0].length - y;
        } else {
            max = board.length - x;
        }

        int i;
        for (i = 0; i < max; i++) {
            if (checkHorizontal && board[x][y + i] == wall)
            {
                break;
            }
            if (!checkHorizontal && board[x + i][y] == wall)
            {
                break;
            }
        }
        return i;
    }

    private int[] findStartingPoint(int candidateNo) {
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[0].length; y++) {
                if (board[x][y] == empty || board[x][y] == wall) {
                    continue;
                }

                if (board[x][y] == candidateNo) {
                    int[] coordinateValues = new int[2];
                    coordinateValues[0] = x;
                    coordinateValues[1] = y;
                    return coordinateValues;
                }
                else if (board[x][y] == -candidateNo) {
                    int[] coordinateValues = new int[2];
                    coordinateValues[0] = x;
                    coordinateValues[1] = y;
                    return coordinateValues;
                }
            }
        }
        return null;
    }

    private boolean isSafe(int placeHolder, int candidateNo) {
        int counter = availableSpace(candidateNo);

        return candidate[placeHolder].length() == counter && isMatch(placeHolder, candidateNo);
    }


    private void insertElement(int placeHolder, int spaceCounter) {

        int[] coordinates = findStartingPoint(spaceCounter);
        int x = coordinates[0];
        int y = coordinates[1];
        int counter = availableSpace(spaceCounter);
        boolean checkHorizontal = board[x][y] > 0;

        Alphabet[] alphabets = new Alphabet[candidate[placeHolder].length()];
        for (int i = 0; i < candidate[placeHolder].length(); i++) {
            alphabets[i] = new Alphabet(placeHolder, candidate[placeHolder].charAt(i));
        }

        for (int i = 0; i < counter; i++) {
            if (selectedCandidates[x][y] == null) {
                selectedCandidates[x][y] = alphabets[i];
                displayBoard();
            }

            if (checkHorizontal) {
                y = y + 1;
            } else {
                x = x + 1;
            }
        }
    }

    private void deleteElement(int placeHolder, int candidateNo) {

        int[] coordinates = findStartingPoint(candidateNo);
        int x = coordinates[0];
        int y = coordinates[1];
        int counter = availableSpace(candidateNo);
        boolean checkHorizontal = board[x][y] > 0;

        for (int i = 0; i < counter; i++) {
            if (selectedCandidates[x][y] != null && selectedCandidates[x][y].placeNo == placeHolder) {
                selectedCandidates[x][y] = null;
                displayBoard();
            }

            if (checkHorizontal) {
                y = y + 1;
            } else {
                x = x + 1;
            }
        }

    }

    private boolean isMatch(int placeHolder, int candidateNo) {

        int[] coordinates = findStartingPoint(candidateNo);
        int x = coordinates[0];
        int y = coordinates[1];
        int counter = availableSpace(candidateNo);
        boolean checkHorizontal = board[x][y] > 0;
        String word = candidate[placeHolder];

        for (int i = 0; i < counter; i++) {
            if (selectedCandidates[x][y] != null && selectedCandidates[x][y].ch != word.charAt(i)) {
                return false;
            }

            if (checkHorizontal) {
                y = y + 1;
            } else {
                x = x + 1;
            }
        }
        return true;
    }

    public void solveCrosswordGame(int candidateNo) {
        if (candidateNo > placesToFill) {
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("-----------FINAL SOLUTION-----------");
            System.exit(0);
            return;
        }
        for (int i = 0; i < candidate.length; i++) {
            if (isSafe(i, candidateNo)) {
                insertElement(i, candidateNo);
                solveCrosswordGame(candidateNo + 1);
                deleteElement(i, candidateNo);
            }
        }
    }

    private void displayBoard(){
        System.out.println(" ");
        System.out.println(" ");
        System.out.println("------- STEP: "+ stepCount.incrementAndGet()+" -------");
        System.out.println(" ");
        System.out.print("    ");
        for (int x = 1; x <= board.length-1; x++)
        {
            System.out.print(x);
            System.out.print("   ");

        }
        System.out.println("  ");
        for (int x = 0; x < board.length; x++)
        {
            System.out.print("  ");
            for (int j = 0; j < board.length-1; j++)
            {
                System.out.print("+---");
            }
            System.out.println("+");
            System.out.print(x+1);

            for (int y = 0; y < board.length-1; y++)
            {
                if(board[x][y] == wall){
                    System.out.print(" | $");
                }
                else {
                     if (selectedCandidates[x][y] == null) {
                    System.out.print(" |  ");
                }
                     else
                         {
                    System.out.print(" | "+ selectedCandidates[x][y].ch);
                     }
                }
            }
            System.out.println(" | ");
        }
        System.out.print("  ");
        for (int j = 0; j < board.length-1; j++)
        {
            System.out.print("+---");
        }
        System.out.print("+");
    }

}