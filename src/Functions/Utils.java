package Functions;

import java.io.File;
import java.io.PrintWriter;
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
            System.out.println("\n\nJumlah titik pada blok tidak sesuai dengan jumlah space kosong di papan.\n");
            System.out.println("Waktu pencarian: 0 ms\n");
            System.out.println("Banyak kasus ditinjau: 0\n");
            return false;
        } else {
            return true;
        }
    }

    public static void solve(Board board, Block[] blocks) {
        possibilitiesExplored = 0;
        startTime = System.currentTimeMillis();
        boolean solved = false;

        if (backtrack(board, blocks)) {
            System.out.println("\nSolusi ditemukan!\n");
            solved = true;
            board.printBoard();
        } else {
            System.out.println("\nTidak ada solusi valid.\n");
        }

        long endTime = System.currentTimeMillis(); // End tracking time
        System.out.println("\nWaktu pencarian: " + (endTime - startTime) + " ms\n");
        System.out.println("Banyak kasus ditinjau: " + possibilitiesExplored + "\n");

        // Output handling
        if (solved) {
            Scanner scanner = new Scanner(System.in); 
            
            while (true) {
                System.out.print("Apakah anda ingin menyimpan solusi? (ya/tidak): ");
                
                if (!scanner.hasNextLine()) {
                    break; 
                }
        
                String save = scanner.nextLine().trim().toLowerCase().split(" ")[0]; 
        
                if (save.equals("ya")) {
                    System.out.print("Masukkan nama file (tanpa .txt): ");
                    
                    if (!scanner.hasNextLine()) {
                        break; 
                    }
        
                    String filename = scanner.nextLine().trim();
        
                    File solutionFolder = new File("test/Solutions/");
                    if (!solutionFolder.exists()) {
                        solutionFolder.mkdirs(); // Make the folder if not exists yet
                    }
        
                    File solutionFile = new File(solutionFolder, filename + ".txt");
        
                    try (PrintWriter writer = new PrintWriter(solutionFile)) {
                        // Write the visualBoardd to the file
                        writer.println("Solusi IQ Puzzler Pro:");
                        for (char[] row : board.getVisualBoard()) {
                            writer.println(new String(row));
                        }
                        System.out.println("Solusi berhasil disimpan di test/Solutions/" + filename + ".txt");
                    } catch (Exception e) {
                        System.out.println("Gagal menyimpan solusi: " + e.getMessage());
                    }
        
                    break; 
                } 
                else if (save.equals("tidak")) {
                    System.out.println("Solusi tidak disimpan.");
                    break; 
                } 
                else {
                    System.out.println("Input tidak valid. Input hanya dapat 'ya' atau 'tidak', ulang.\n");
                }
            }
        
            scanner.close(); 
        }
    }

    private static boolean backtrack(Board board, Block[] blocks) {
        possibilitiesExplored++;

        // Find the first empty space that's most top and most left
        int[] emptyPos = findEmptySpace(board);
        if (emptyPos == null) {
            return true; // All spaces filled = Solution found!
        }
        int row = emptyPos[0], col = emptyPos[1];

        // Try placing each unused block
        for (Block block : blocks) {
            if (!block.getIsPlaced()) { // If block is not yet placed
                
                // Try placing in 4 different rotations (0, 90, 180, 270)
                for (int rotation = 0; rotation < 4; rotation++) {
                    if (isBlockFit(board, block, row, col)) {
                        board.addBlock(block, row, col); 
                        block.setIsPlaced(true);

                        if (backtrack(board, blocks)) {
                            return true; // Found a solution
                        }

                        // Backtrack: Remove the block and try another
                        board.removeBlock(block, row, col);
                        block.setIsPlaced(false);
                    }
                    block.rotate90();
                }
            }
        }
        return false; 
    }


    // use visualBoard instead because the checking more computationally cheap than using bitmaskBoard
    private static int[] findEmptySpace(Board board) {
        char[][] visualBoard = board.getVisualBoard();
        for (int r = 0; r < board.getRow(); r++) {
            for (int c = 0; c < board.getColumn(); c++) {
                if (visualBoard[r][c] == ' ') {
                    return new int[]{r, c}; 
                }
            }
        }
        return null; 
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

                // Check if the shifted block row overlaps with the existing block inside the board
                if ((bitmaskBoard[rowCoord + i] & shiftedBlockRow) != 0) {
                    return false; // Overlap detected, block cannot be placed
                }
            }
            return true; // No overlap, block fits
        }
    }
}
