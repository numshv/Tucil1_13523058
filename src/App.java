import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Functions.Board;
import Functions.Block;
import Functions.Utils;

public class App {
    public static void main(String[] args) throws Exception {

        // INPUT PART OF THE PROGRAM
        System.out.println("Welcome to the IQ Puzzler Pro Game!\n");
        Scanner scanner = new Scanner(System.in);
        File file = null;
        int row = 0, col = 0, P = 0;
        String boardType = "";

        // Loop until a valid file with correct format is provided
        while (true) {
            System.out.print("Masukkan nama file input (dalam folder test, ex: tes1.txt): ");
            String filename = scanner.nextLine();
            file = new File("test/" + filename); // Check file inside test folder

            if (!file.exists()) {
                System.out.println("File tidak ditemukan, coba input lain.");
                continue; 
            }

            try (Scanner fileScanner = new Scanner(file)) {
                if (fileScanner.hasNextInt()) row = fileScanner.nextInt();
                else {
                    System.out.println("File tidak valid, baris pertama harus berisi 3 integer.");
                    continue;
                }

                if (fileScanner.hasNextInt()) col = fileScanner.nextInt();
                else {
                    System.out.println("File tidak valid, baris pertama harus berisi 3 integer.");
                    continue;
                }

                if (fileScanner.hasNextInt()) P = fileScanner.nextInt();
                else {
                    System.out.println("File tidak valid, baris pertama harus berisi 3 integer.");
                    continue;
                }

                fileScanner.nextLine();

                // Read board type
                if (fileScanner.hasNextLine()) {
                    boardType = fileScanner.nextLine().trim().toLowerCase();
                } else {
                    System.out.println("File tidak valid, baris kedua harus berisi tipe papan.");
                    continue;
                }

                if (!boardType.equals("default") && !boardType.equals("custom")) {
                    System.out.println("Tipe papan hanya bisa DEFAULT dan CUSTOM.");
                    continue;
                }
                break;
            } catch (FileNotFoundException e) {
                System.out.println("Unexpected error: File not found.");
            }
        }

        Board board;
        char[][] customBoard = new char[row][col];

        try (Scanner fileScanner = new Scanner(file)) {
            // Skip first two lines (N M P and board type)
            fileScanner.nextLine();
            fileScanner.nextLine();

            // Handle CUSTOM board input
            if (boardType.equals("custom")) {
                for (int i = 0; i < row; i++) {
                    if (!fileScanner.hasNextLine()) {
                        System.out.println("File tidak valid, baris papan custom tidak sesuai.");
                        System.exit(1);
                    }
                    String line = fileScanner.nextLine();
                    line = line.toUpperCase(); 
                    char[] rowChars = line.toCharArray();

                    for (int j = 0; j < col; j++) {
                        customBoard[i][j] = (j < rowChars.length) ? rowChars[j] : ' ';
                    }
                }
            }

            // Initialize the Board
            if (boardType.equals("default")) {
                board = new Board(col, row);
            } else {
                board = new Board(col, row, customBoard);
                System.out.println("\nBentuk papan CUSTOM: ");
                board.printBoard();
            }

            Block[] blocks = new Block[P];

            // Process the block input
            Map<Character, List<String>> blockMap = new LinkedHashMap<>();
            Character currentBlockID = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isEmpty()) continue; // Ignore empty lines

                line = line.toUpperCase();

                char blockID = line.replace(" ", "").charAt(0); // Ignore spaces when detecting block ID

                if (currentBlockID == null || currentBlockID != blockID) {
                    currentBlockID = blockID;
                    blockMap.putIfAbsent(blockID, new ArrayList<>());
                }

                blockMap.get(blockID).add(line);
            }

            if (blockMap.size() != P) {
                System.out.println("Jumlah blok tidak sesuai dengan P");
                System.exit(1);
            }

            int blockIndex = 0;
            for (Map.Entry<Character, List<String>> entry : blockMap.entrySet()) {
                char blockID = entry.getKey();
                List<String> shapeLines = entry.getValue();

                int blockRows = shapeLines.size();
                int blockCols = 0;

                for (String s : shapeLines) {
                    blockCols = Math.max(blockCols, s.length());
                }

                char[][] visualBlock = new char[blockRows][blockCols];

                for (int i = 0; i < blockRows; i++) {
                    char[] rowChars = shapeLines.get(i).toCharArray();
                    for (int j = 0; j < blockCols; j++) {
                        visualBlock[i][j] = (j < rowChars.length) ? rowChars[j] : ' ';
                    }
                }
                blocks[blockIndex++] = new Block(blockID, blockRows, blockCols, visualBlock);
            }

            // // print the blocks
            // for (Block block : blocks) {
            //     if (block != null) {
            //         block.printBlock();
            //         System.out.println(block.getSize());
            //     }
            // }

            if (!Utils.initialCheck(board, blocks)) {
                System.exit(1);
            }

            // Solve the puzzle
            Utils.solve(board, blocks);

        } catch (FileNotFoundException e) {
            System.out.println("Unexpected error: File not found.");
        }

        scanner.close();
    }
}


