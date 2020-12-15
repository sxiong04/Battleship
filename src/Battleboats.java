// written by xion1884
import java.util.Scanner;

public class Battleboats {
    private String[][] playerBoard = new String[8][8];
    private String[][] computerBoard = new String[8][8];
    private int[] boats = new int[]{5, 4, 3, 3, 2};
    private int[][] boatInfo = new int[5][4];
    private int count = 0;
    private int boatSunk = 0;
    private int turnCount = 0;
    private int canonShots = 0;

    public String gameLevel(String level) {
        if (level.equalsIgnoreCase("standard")) {
            fillEmptyBoatSpace();
            return "Boats:" + "\n One boat of length 5" + "\n One boat of length 4" + "\n Two boats of length 3" +
                    "\n One boat of length 2" + "\n" + printBoard("player");
        } else if (level.equalsIgnoreCase("expert")) {
            playerBoard = new String[12][12];
            computerBoard = new String[12][12];
            boats = new int[]{5, 5, 4, 4, 3, 3, 3, 3, 2, 2};
            boatInfo = new int[10][4];
            fillEmptyBoatSpace();
            return "Boats:" + "\n Two boat of length 5" + "\n Two boat of length 4" + "\n Four boats of length 3" +
                    "\n Two boat of length 2" + "\n" + printBoard("player");
        } else return gameLevelNotSelected();
    }

    public String gameLevelNotSelected() {
        Scanner s = new Scanner(System.in);
        System.out.print("Input invalid! Please input standard or expert: ");
        return gameLevel(s.next());
    }

    public void makeBoats(int row, int col, int length, int orientation) {
        if (orientation == 0)
            for (int i = 0; i < length; i++) {
                computerBoard[row + i][col] = String.valueOf(length);
            }
        else {
            for (int i = 0; i < length; i++) {
                computerBoard[row][col + i] = String.valueOf(length);
            }
        }
        boatInfo[count][0] = col;
        boatInfo[count][1] = row;
        boatInfo[count][2] = length;
        boatInfo[count][3] = orientation;
        count += 1;
    }

    public int[] placeBoats(int[] boats) {
        int countBoats = 0;
        int[] boatsMade = new int[boats.length];
        if ((boats[0] > 0)) {
            for (int i = 0; i < boats.length; i++) {
                int orientation = (int) Math.floor((Math.random() * 2));
                int row;
                int col;
                if (orientation == 0) {
                    row = (int) Math.floor((Math.random()) * (playerBoard.length-(boats[i]-1)));
                    col = (int) Math.floor((Math.random()) * 8);
                } else {
                    row = (int) Math.floor((Math.random()) * 8);
                    col = (int) Math.floor((Math.random()) * (playerBoard.length-(boats[i]-1)));
                }
                if (checkRoomForBoat(row, col, boats[i], orientation)) {
                    boatsMade[i] = boats[i];
                    makeBoats(row, col, boats[i], orientation);
                    countBoats += 1;
                }
            }
        }
        int count = 0;
        int[] boatsNotMade = new int[boats.length - countBoats];
        for (int i = 0; i < boatsMade.length; i++) {
            if (boatsMade[i] == 0) {
                boatsNotMade[count] = boats[i];
                count += 1;
            }
        }
        return boatsNotMade;
    }

    public boolean checkRoomForBoat(int row, int col, int length, int orientation) {
        int count = 0;
        if (orientation == 0) {
            for (int i = 0; i < length; i++) {
                if (!(computerBoard[row + i][col].equals("5") ||
                        computerBoard[row + i][col].equals("4") ||
                        computerBoard[row + i][col].equals("3") ||
                        computerBoard[row + i][col].equals("2"))) {
                    count += 1;
                }
            }
        } else if (orientation == 1) {
            for (int i = 0; i < length; i++) {
                if (!(computerBoard[row][col + i].equals("5") ||
                        computerBoard[row][col + i].equals("4") ||
                        computerBoard[row][col + i].equals("3") ||
                        computerBoard[row][col + i].equals("2"))) {
                    count += 1;
                }
            }
        }
        return count == length;
    }

    public void fillEmptyBoatSpace() {
        for (int i = 0; i < computerBoard.length; i++) {
            for (int j = 0; j < computerBoard[i].length; j++) {
                computerBoard[i][j] = "-";
                playerBoard[i][j] = "-";
            }
        }
    }

    public String printBoard(String s) {
        String out = "[0][1][2][3][4][5][6][7]\n";
        if (playerBoard.length > 9) {
            out = "[0][1][2][3][4][5][6][7][8][9][10][11]\n";
        }
        if (s.equalsIgnoreCase("player")) {
            for (int i = 0; i < playerBoard.length; i++) {
                for (int j = 0; j < playerBoard[i].length; j++) {
                    out += "[" + playerBoard[i][j] + "]";
                }
                out += "[" + i + "]" + "\n";
            }
        } else {
            for (int i = 0; i < computerBoard.length; i++) {
                for (int j = 0; j < computerBoard[i].length; j++) {
                    out += "[" + computerBoard[i][j] + "]";
                }
                out += "[" + i + "]" + "\n";
            }
        }
        return out;
    }

    public void start() {
        int[] boatsNotMade = placeBoats(boats);
        while (boatsNotMade.length > 0 && boatsNotMade[0] > 0)
            boatsNotMade = placeBoats(boatsNotMade);
        int droneCount = 0;
        int missileCount = 0;
        Scanner s = new Scanner(System.in);
        System.out.print("What would you like to do? Shoot/Drone/Missile/Print/End: ");
        while (s.hasNext()) {
            String command = s.next();
            if (command.equalsIgnoreCase("shoot")) {
                System.out.print("Please input desired coordinates x,y: ");
                int[] coordinates = new int[2];
                String[] coordinatesString = (s.next().split(","));
                for (int i = 0; i < coordinatesString.length; i++) {
                    coordinates[i] = Integer.parseInt(coordinatesString[i]);
                }
                shoot(coordinates);
                turnCount += 1;
                if (boatSunk == boats.length) {
                    break;
                } else
                    System.out.print("What would you like to do? Shoot/Drone/Missile/Print/End: ");
            }
            else if (command.equalsIgnoreCase("drone")) {
                if ((droneCount > 0 && playerBoard.length < 9) || (droneCount > 1 && playerBoard.length > 9))
                    System.out.print("Drone has been used the max amount of times. What would you like to do? Shoot/Drone/Missile/Print/End: ");
                else {
                    String[] droneValues = new String[2];
                    droneCount += 1;
                    System.out.print("Would you like to scan a row or column? r/c: ");
                    String rowCol = s.next();
                    while (!(rowCol.equalsIgnoreCase("r") || rowCol.equalsIgnoreCase("c"))) {
                        System.out.print("Invalid input! Please enter r/c: ");
                        rowCol = s.next();
                    }
                    droneValues[0] = rowCol;
                    System.out.print("Which row or column would you like to scan: ");
                    String num = s.next();
                    while (Integer.parseInt(num) < 0 || Integer.parseInt(num) > playerBoard.length) {
                        System.out.print("Invalid input! Please enter correct number: ");
                        num = s.next();
                    }
                    droneValues[1] = num;
                    drone(droneValues);
                    turnCount += 1;
                    System.out.print("What would you like to do? Shoot/Drone/Missile/Print/End: ");
                }
            }
            else if (command.equalsIgnoreCase("missile")) {
                if ((missileCount > 0 && playerBoard.length < 9) || (missileCount > 1 && playerBoard.length > 9))
                    System.out.print("Missile has been used the max amount of times. What would you like to do? Shoot/Drone/Missile/Print/End: ");
                else {
                    missileCount += 1;
                    System.out.print("Which x,y coordinates would you like to hit: ");
                    int[] coordinates = new int[2];
                    String[] coordinatesString = (s.next().split(","));
                    for (int i = 0; i < coordinatesString.length; i++) {
                        coordinates[i] = Integer.parseInt(coordinatesString[i]);
                    }
                    while (coordinates[0] < 0 || coordinates[1] < 0 || coordinates[0] > playerBoard.length || coordinates[1] > playerBoard.length) {
                        System.out.print("Invalid Input! Please input correct coordinates x,y: ");
                        coordinatesString = (s.next().split(","));
                        for (int i = 0; i < coordinatesString.length; i++) {
                            coordinates[i] = Integer.parseInt(coordinatesString[i]);
                        }
                    }
                    missile(coordinates);
                    turnCount += 1;
                    if (boatSunk == boats.length) {
                        break;
                    } else
                        System.out.print("What would you like to do? Shoot/Drone/Missile/Print/End: ");
                }
            }
            else if (command.equalsIgnoreCase("end")) {
                System.out.print("Game ended.\nAmount of moves: " + turnCount + "\nCanon shots including missile total: " + canonShots + "\n" + printBoard("player") + "\n" + printBoard("computer"));
                break;
            }
            else if (command.equalsIgnoreCase("print")) {
                System.out.println(printBoard("computer"));
                System.out.print("Invalid input! What would you like to do? Shoot/Drone/Missile/Print/End: ");
            }
            else {
                System.out.print("Invalid input! What would you like to do? Shoot/Drone/Missile/Print/End: ");
            }
        }
    }

    public void shoot(int[] coordinates) {
        int col = coordinates[0];
        int row = coordinates[1];
        if (row < 0 || col < 0 || row >= playerBoard.length || col >= playerBoard.length) {
            System.out.println("Penalty!");
        }
        else if (computerBoard[row][col].equals("5") ||
                computerBoard[row][col].equals("4") ||
                computerBoard[row][col].equals("3") ||
                computerBoard[row][col].equals("2")) {
            playerBoard[row][col] = computerBoard[row][col];
            System.out.println("Hit!");
            if (checkSunk(computerBoard[row][col], row, col))
                System.out.println("Boat Sunk!");
            if (boatSunk == boats.length)
                System.out.print("Game ended.\nAmount of moves: " + turnCount + "\n Canon shots including missile total: " + canonShots + "\n" + printBoard("player") + "\n" + printBoard("computer"));
            canonShots += 1;
        }
        else if (computerBoard[row][col].equals("-")) {
            computerBoard[row][col] = "M";
            playerBoard[row][col] = "O";
            System.out.println("Miss!");
            canonShots += 1;
        }
        else
            System.out.println("Penalty!");
        System.out.println(printBoard("player"));
    }

    public void drone(String[] droneValues) {
        int boatCount = 0;
        if (droneValues[0].equalsIgnoreCase("c")) {
            for (int i = 0; i < playerBoard.length; i++) {
                if (computerBoard[i][Integer.parseInt(droneValues[1])].equals("5") ||
                        computerBoard[i][Integer.parseInt(droneValues[1])].equals("4") ||
                        computerBoard[i][Integer.parseInt(droneValues[1])].equals("3") ||
                        computerBoard[i][Integer.parseInt(droneValues[1])].equals("2") ||
                        computerBoard[i][Integer.parseInt(droneValues[1])].equals("H"))
                    boatCount += 1;
            }
        }
        if (droneValues[0].equalsIgnoreCase("r")) {
            for (int i = 0; i < playerBoard.length; i++) {
                if (computerBoard[Integer.parseInt(droneValues[1])][i].equals("5") ||
                        computerBoard[Integer.parseInt(droneValues[1])][i].equals("4") ||
                        computerBoard[Integer.parseInt(droneValues[1])][i].equals("3") ||
                        computerBoard[Integer.parseInt(droneValues[1])][i].equals("2") ||
                        computerBoard[Integer.parseInt(droneValues[1])][i].equals("H"))
                    boatCount += 1;
            }
        }
        System.out.println("Number of boats in " + droneValues[0] + droneValues[1] + " is: " + boatCount);
    }

    public void missile(int[] coordinates) {
        int count = 0;
        int[][] works = new int[9][1];
        int[][] xy = new int[][]{{coordinates[0], coordinates[1]},
                {coordinates[0] + 1, coordinates[1]},
                {coordinates[0], coordinates[1] + 1},
                {coordinates[0] - 1, coordinates[1]},
                {coordinates[0], coordinates[1] - 1},
                {coordinates[0] + 1, coordinates[1] + 1},
                {coordinates[0] + 1, coordinates[1] - 1},
                {coordinates[0] - 1, coordinates[1] + 1},
                {coordinates[0] - 1, coordinates[1] - 1}};
        for (int i = 0; i < xy.length; i++) {
            if (xy[i][0] >= 0 && xy[i][0] <= playerBoard.length && xy[i][1] >= 0 && xy[i][1] <= playerBoard.length) {
                works[count] = xy[i];
                count += 1;
            }
        }
        int[][] worksFinal = new int[count][1];
        for (int i = 0; i < worksFinal.length; i++)
            worksFinal[i] = works[i];
        for (int i = 0; i < worksFinal.length; i++)
            shoot(worksFinal[i]);
    }

    public boolean checkSunk(String boatLength, int row, int col) {
        int boatLengthInt = Integer.parseInt(boatLength);
        computerBoard[row][col] = "H";
        for (int i = 0; i < boatInfo.length; i++) {
            int count = 0;
            if (boatInfo[i][2] == boatLengthInt) {
                if (boatInfo[i][3] == 0) {
                    for (int j = 0; j < boatLengthInt; j++) {
                        if (computerBoard[boatInfo[i][1] + j][boatInfo[i][0]].equalsIgnoreCase("H")) {
                            count += 1;
                        }
                    }
                }
                if (boatInfo[i][3] == 1) {
                    for (int j = 0; j < boatLengthInt; j++) {
                        if (computerBoard[boatInfo[i][1]][boatInfo[i][0] + j].equalsIgnoreCase("H")) {
                            count += 1;
                        }
                    }
                }
                if (count == boatInfo[i][2]) {
                    boatSunk += 1;
                    boatInfo[i] = new int[]{0, 0, 0, 0};
                    return true;
                }
            }
        }
        return false;
    }
}

class Main {
    public static void main(String[] args) {
        Battleboats game = new Battleboats();
        Scanner s = new Scanner(System.in);
        System.out.print("Would you like to play standard or expert mode: ");
        System.out.println(game.gameLevel(s.next()));
        game.start();
    }
}
