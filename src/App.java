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
            System.out.print("Enter the test file name (inside test folder): ");
            String filename = scanner.nextLine();
            file = new File("test/" + filename); // Check file inside test folder

            if (!file.exists()) {
                System.out.println("File not found. Please try again.");
                continue; // Restart loop
            }

            // Attempt to read and validate the file content
            try (Scanner fileScanner = new Scanner(file)) {
                if (fileScanner.hasNextInt()) row = fileScanner.nextInt();
                else {
                    System.out.println("Invalid file format: First line must contain exactly 3 integers.");
                    continue;
                }

                if (fileScanner.hasNextInt()) col = fileScanner.nextInt();
                else {
                    System.out.println("Invalid file format: First line must contain exactly 3 integers.");
                    continue;
                }

                if (fileScanner.hasNextInt()) P = fileScanner.nextInt();
                else {
                    System.out.println("Invalid file format: First line must contain exactly 3 integers.");
                    continue;
                }

                fileScanner.nextLine(); // Move to the next line after reading numbers

                // **Read board type**
                if (fileScanner.hasNextLine()) {
                    boardType = fileScanner.nextLine().trim().toLowerCase();
                } else {
                    System.out.println("Invalid file format: Missing board type.");
                    continue;
                }

                // **Validate board type**
                if (!boardType.equals("default") && !boardType.equals("custom")) {
                    System.out.println("Only provides DEFAULT or CUSTOM board type.");
                    continue;
                }

                // Successfully read valid file → Break out of loop
                break;
            } catch (FileNotFoundException e) {
                System.out.println("Unexpected error: File not found.");
            }
        }

        // Output the extracted values
        System.out.println("Row = " + row + ", Col = " + col + ", P = " + P);
        System.out.println("Board Type: " + boardType);

        Board board;
        char[][] customBoard = new char[row][col]; // For CUSTOM board

        // Process the input file again to parse the board & blocks
        try (Scanner fileScanner = new Scanner(file)) {
            // Skip first two lines (N M P and board type)
            fileScanner.nextLine();
            fileScanner.nextLine();

            // **Handle CUSTOM board input**
            if (boardType.equals("custom")) {
                for (int i = 0; i < row; i++) {
                    if (!fileScanner.hasNextLine()) {
                        System.out.println("Invalid file format: Incomplete CUSTOM board data.");
                        System.exit(1);
                    }
                    String line = fileScanner.nextLine();
                    line = line.toUpperCase(); // Convert to uppercase
                    char[] rowChars = line.toCharArray();

                    for (int j = 0; j < col; j++) {
                        // Ensure we do not exceed the provided row length
                        customBoard[i][j] = (j < rowChars.length) ? rowChars[j] : ' ';
                    }
                }
            }

            // **Initialize the Board**
            if (boardType.equals("default")) {
                board = new Board(col, row);
            } else {
                board = new Board(col, row, customBoard);
            }
            board.printBoard();
            board.printBitmaskBoard();
            System.out.println("board size: " + board.getSize());
            Block[] blocks = new Block[P];

            // **Process the block input**
            Map<Character, List<String>> blockMap = new LinkedHashMap<>();
            Character currentBlockID = null;

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.isEmpty()) continue; // Ignore empty lines

                // Convert lowercase to uppercase
                line = line.toUpperCase();

                // Detect new block
                char blockID = line.replace(" ", "").charAt(0); // Ignore spaces when detecting block ID

                // If encountering a new block ID, store previous block and start a new one
                if (currentBlockID == null || currentBlockID != blockID) {
                    currentBlockID = blockID;
                    blockMap.putIfAbsent(blockID, new ArrayList<>());
                }

                // Add row data to the corresponding block
                blockMap.get(blockID).add(line);
            }

            // **Validation: Ensure number of blocks matches P**
            if (blockMap.size() != P) {
                System.out.println("P doesn't match the actual amount of existing blocks.");
                System.exit(1);
            }

            // **Convert block data into Block objects**
            int blockIndex = 0;
            for (Map.Entry<Character, List<String>> entry : blockMap.entrySet()) {
                char blockID = entry.getKey();
                List<String> shapeLines = entry.getValue();

                int blockRows = shapeLines.size();
                int blockCols = 0;

                // Determine max width of the block based on character count (including spaces)
                for (String s : shapeLines) {
                    blockCols = Math.max(blockCols, s.length());
                }

                // Convert to char array
                char[][] visualBlock = new char[blockRows][blockCols];

                for (int i = 0; i < blockRows; i++) {
                    char[] rowChars = shapeLines.get(i).toCharArray();
                    for (int j = 0; j < blockCols; j++) {
                        // Allow space (' ') as an empty spot in the block
                        visualBlock[i][j] = (j < rowChars.length) ? rowChars[j] : ' ';
                    }
                }

                // Create Block object and store it
                blocks[blockIndex++] = new Block(blockID, blockRows, blockCols, visualBlock);
            }

            // **Print all blocks for verification**
            for (Block block : blocks) {
                if (block != null) {
                    block.printBlock();
                    System.out.println(block.getSize());
                }
            }

            // **Check board size validation**
            if (!Utils.initialCheck(board, blocks)) {
                System.exit(1);
            }

            // **Solve the puzzle**
            Utils.solve(board, blocks);

        } catch (FileNotFoundException e) {
            System.out.println("Unexpected error: File not found.");
        }

        scanner.close(); // Close the scanner
    }
}





        // Now, row, col, P are accessible
        // Board board = new Board(col, row);
        // Block[] blocks = new Block[P];

        // Test block addition
        // Block block1 = new Block('L', 2, 2, new char[][]{{'A', 'A'}, {'A', 'A'}});
        // Block block2 = new Block('D', 3, 2, new char[][]{{'A', ' '}, {'A', 'A'}, {'A', ' '}});
        // board.addBlock(block1, 3, 4);
        // board.addBlock(block2, 0, 0);
        // board.printBoard();

        // System.out.println("Hello, World!");

        // Block blockA = new Block('A', 2, 3, new char[][]{{'A', 'A', 'A'}, {' ', 'A', ' '}});
        // System.out.println(blockA.getSize());
        // blockA.printBitmask();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();


        // Board boardTes = new Board(5, 4);
        // boardTes.printBitmaskBoard();
        // boardTes.printBoard();

        // boardTes.addBlock(blockA, 0, 0);
        // boardTes.printBitmaskBoard();
        // boardTes.printBoard();



