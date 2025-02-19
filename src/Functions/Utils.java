package Functions;

import java.util.Scanner;

public class Utils {

    private static int possibilitiesExplored = 0;
    private static long startTime;

    public static boolean initialCheck(Board board, Block[] blocks) {
        int boardSize = board.getSize();
        int sumBlockSizes = 0;

        for (Block block : blocks) {
            sumBlockSizes += block.getSize();
        }

        if (sumBlockSizes != boardSize) {
            System.out.println("\n\nSum of block sizes isn't the same as the board size. No Solution.\n");
            System.out.println("Waktu pencarian: 0 ms\n");
            System.out.println("Banyak kasus ditinjau: 0\n");
            return false;
        } else {
            return true;
        }
    }

    public static void solve(Board board, Block[] blocks) {
        possibilitiesExplored = 0;
        startTime = System.currentTimeMillis(); // Start tracking time
        boolean solved = false;

        if (backtrack(board, blocks)) {
            System.out.println("\nSolution Found!\n");
            solved = true;
            board.printBoard();
        } else {
            System.out.println("\nNo Solution Found.\n");
        }

        long endTime = System.currentTimeMillis(); // End tracking time
        System.out.println("\nWaktu pencarian: " + (endTime - startTime) + " ms\n");
        System.out.println("Banyak kasus ditinjau: " + possibilitiesExplored + "\n");

        if(solved) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak)");
        } 
    }

    private static boolean backtrack(Board board, Block[] blocks) {
        possibilitiesExplored++;

        // Step 1: Find the first empty space (top-left priority)
        int[] emptyPos = findEmptySpace(board);
        if (emptyPos == null) {
            return true; // All spaces filled → Solution found!
        }
        int row = emptyPos[0], col = emptyPos[1];

        // Step 2: Try placing each unused block
        for (Block block : blocks) {
            if (!block.getIsPlaced()) { // If block is not yet placed
                
                // Try placing in 4 different rotations
                for (int rotation = 0; rotation < 4; rotation++) {
                    if (isBlockFit(board, block, row, col)) {
                        board.addBlock(block, row, col); // Place block
                        block.setIsPlaced(true);

                        // Print board after placing block
                        //board.printBoard();
                        //System.out.println("Placed Block: " + block.getBlockID() + " at (" + row + ", " + col + ")");

                        // Recur to place the next block
                        if (backtrack(board, blocks)) {
                            return true; // Found a solution
                        }

                        // Backtrack: Remove the block and try another
                        board.removeBlock(block, row, col);
                        block.setIsPlaced(false);
                    }
                    block.rotate90(); // Rotate the block
                }
            }
        }
        return false; // No valid placement found, trigger backtracking
    }

    private static int[] findEmptySpace(Board board) {
        char[][] visualBoard = board.getVisualBoard();
        for (int r = 0; r < board.getRow(); r++) {
            for (int c = 0; c < board.getColumn(); c++) {
                if (visualBoard[r][c] == ' ') {
                    return new int[]{r, c}; // Return first found empty space
                }
            }
        }
        return null; // No empty space left, means solution found
    }

    public static boolean isBlockFit(Board board, Block block, int rowCoord, int colCoord) {
        int[] bitmaskBoard = board.getBitmaskBoard();
        int[] bitmaskBlock = block.getBitmaskBlock();
        int boardCol = board.getColumn();
        int boardRow = board.getRow();
        int blockCol = block.getColumn();
        int blockRow = block.getRow();

        // Check if block goes out of bounds
        if (blockRow + rowCoord > boardRow || blockCol + colCoord > boardCol) {
            return false;
        } else {
            // Shift the block left by colCoord
            for (int i = 0; i < blockRow; i++) {
                int shiftedBlockRow = bitmaskBlock[i] << (boardCol - blockCol - colCoord);

                // Check if the shifted block row overlaps with the board row
                if ((bitmaskBoard[rowCoord + i] & shiftedBlockRow) != 0) {
                    return false; // Overlap detected, block cannot be placed
                }
            }
            return true; // No overlap, block fits
        }
    }
}
