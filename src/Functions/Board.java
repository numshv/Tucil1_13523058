package Functions;

public class Board {

    // Attributes
    private boolean isComplete;
    private int[] bitmaskBoard;     // Bitmask board for fast fit checking
    private char[][] visualBoard;   // Character board for final printing
    private int size, length, width;
    private int filledSpace, emptySpace;

    // Constructor
    public Board(int width, int length) {
        this.length = length;
        this.width = width;
        this.size = length * width;
        this.isComplete = false;
        this.filledSpace = 0;
        this.emptySpace = this.size;
        this.bitmaskBoard = new int[length];  // Initialize with all zeros (empty board)
        this.visualBoard = new char[length][width];  // Initialize with spaces

        // Fill visualBoard with spaces
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < width; col++) {
                this.visualBoard[row][col] = ' ';
            }
        }
    }

    // Print visual board
    public void printBoard() {
        System.out.println("Current Board:");
        for (char[] row : visualBoard) {
            System.out.println(new String(row));
        }
    }

    // Print bitmask board (for debugging)
    public void printBitmaskBoard() {
        System.out.println("Bitmask Representation:");
        for (int row : bitmaskBoard) {
            System.out.println(String.format("%" + width + "s", Integer.toBinaryString(row)).replace(' ', '0'));
        }
    }

    public void addBlock(Block block, int row, int col) {
        int[] blockMask = block.getBitmaskBlock(); // Get bitmaskBlock from Block object
        char blockID = block.getBlockID();        // Get block identifier (A, B, etc.)
        int blockHeight = block.getLength();      // Block height
        int blockWidth = block.getWidth();        // Block width

        // 1. Shift each row of bitmaskBlock to align with board (add col offset)
        for (int i = 0; i < blockHeight; i++) {
            int shiftedRow = blockMask[i] << (width - blockWidth - col); // Shift left to align
            bitmaskBoard[row + i] |= shiftedRow; // 2. Add to board using OR operation
        }

        // 3. Update visualBoard
        for (int i = 0; i < blockHeight; i++) {
            for (int j = 0; j < blockWidth; j++) {
                if ((blockMask[i] & (1 << (blockWidth - j - 1))) != 0) {
                    visualBoard[row + i][col + j] = blockID; // Set block character
                }
            }
        }

        // 4. Update filled and empty spaces
        this.filledSpace += block.getSize(); 
        this.emptySpace -= block.getSize();
    }


    // setters getters

    public boolean getIsComplete() {
        return isComplete;
    }

    public void setIsComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    public int[] getBitmaskBoard() {
        return bitmaskBoard;
    }

    public void setBitmaskBoard(int[] bitmaskBoard) {
        this.bitmaskBoard = bitmaskBoard;
    }

    public char[][] getVisualBoard() {
        return visualBoard;
    }

    public void setVisualBoard(char[][] visualBoard) {
        this.visualBoard = visualBoard;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getFilledSpace() {
        return filledSpace;
    }

    public void setFilledSpace(int filledSpace) {
        this.filledSpace = filledSpace;
    }

    public int getEmptySpace() {
        return emptySpace;
    }

    public void setEmptySpace(int emptySpace) {
        this.emptySpace = emptySpace;
    }


}

