package Functions;

public class Board {

    // Constant
    public static final int[] ALPHABET_HEX = {
        0x868188,
        0x5F556A,
        0x646365,
        0xCF8ACB,
        0x45444F,
        0xEDC8C4,
        0x212123,
        0xB2B47E,
        0x352B42,
        0x7B7243,
        0x43436A,
        0x4E584A,
        0x4B80CA,
        0x567B79,
        0x68C2D3,
        0x8AB060,
        0xA2DCC7,
        0xC2D368,
        0xEDE19E,
        0xE5CEB4,
        0xD3A068,
        0xA77B5B,
        0xB45252,
        0x80493A,
        0x6A536E,
        0x4B4158
    };

    // Attributes
    private boolean isComplete;
    private int[] bitmaskBoard;     // board in bitmask representation
    private char[][] visualBoard;   // board in alphabet representation
    private int size, row, col;
    private int filledSpace, emptySpace;

    // Constructor

    // Conso for default board
    // Conso for default board
    public Board(int col, int row) {
        this.row = row;
        this.col = col;
        this.size = row * col;
        this.isComplete = false;
        this.filledSpace = 0;
        this.emptySpace = this.size;
        this.bitmaskBoard = new int[row];  
        this.visualBoard = new char[row][col];


        for (int r = 0; r < row; r++) {
            for (int c = 0; c < col; c++) {
                this.visualBoard[r][c] = ' ';
            }
        }
    }


    // Conso for custom board
    public Board(int col, int row, char[][] visualBoard) {
        this.row = row;
        this.col = col;
        this.size = 0;
        this.isComplete = false;
        this.filledSpace = 0;
        this.bitmaskBoard = new int[row];  
        this.visualBoard = visualBoard; 

        for (int r = 0; r < row; r++) {
            int bitmask = 0;
            for (int c = 0; c < col; c++) {
                if (visualBoard[r][c] != 'X') {  
                    this.filledSpace++;
                    bitmask |= (1 << (col - c - 1));  // Set bit from right to left
                }
                else{
                    this.visualBoard[r][c] = ' ';
                    this.size++;
                }
            }
            this.bitmaskBoard[r] = bitmask;  
        }

        this.emptySpace = this.size;
    }

    public void printBoard() { 
        System.out.println("Current Board:");
    
        for (char[] r : visualBoard) {
            StringBuilder sb = new StringBuilder();
            for (char c : r) {
                if (c == ' ') {
                    sb.append(" "); // Keep spaces uncolored
                } else {
                    int index = c - 'A'; // Get index for ALPHABET_HEX (A=0, B=1, ..., Z=25)
                    if (index >= 0 && index < ALPHABET_HEX.length) {
                        String color = getAnsi256Color(ALPHABET_HEX[index]); // Convert hex to ANSI
                        sb.append(color).append(c).append("\u001B[0m"); // Apply color and reset
                    } else {
                        sb.append(c); // Print normally if not in range
                    }
                }
            }
            System.out.println(sb.toString()); // Print the row
        }
    }
    
    // Convert Hex (0xRRGGBB) to the closest ANSI 256-color code
    private String getAnsi256Color(int hex) {
        int r = (hex >> 16) & 0xFF;
        int g = (hex >> 8) & 0xFF;
        int b = hex & 0xFF;
        int ansiCode = rgbToAnsi256(r, g, b);
        return String.format("\u001B[38;5;%dm", ansiCode);
    }
    
    // Convert RGB to nearest ANSI 256-color code
    private int rgbToAnsi256(int r, int g, int b) {
        // Check for grayscale (r=g=b)
        if (r == g && g == b) {
            if (r < 8) return 16;
            if (r > 238) return 231;
            return (int)(((r - 8) / 10) + 232);
        }
        
        // Calculate the nearest color index for RGB
        int rIndex = Math.min(5, (int)Math.round(r / 51.0));
        int gIndex = Math.min(5, (int)Math.round(g / 51.0));
        int bIndex = Math.min(5, (int)Math.round(b / 51.0));
        
        return 16 + (36 * rIndex) + (6 * gIndex) + bIndex;
    }
    

    public void printBitmaskBoard() {
        System.out.println("Bitmask Representation:");
        for (int r : bitmaskBoard) {
            System.out.println(String.format("%" + col + "s", Integer.toBinaryString(r)).replace(' ', '0'));
        }
    }

    public void addBlock(Block block, int r, int c) {
        int[] blockMask = block.getBitmaskBlock(); 
        char blockID = block.getBlockID();        
        int blockHeight = block.getRow();       
        int blockWidth = block.getColumn();     

        // 1. Shift each row of bitmaskBlock to align with board (add col offset)
        for (int i = 0; i < blockHeight; i++) {
            int shiftedRow = blockMask[i] << (col - blockWidth - c); // Shift left to align
            bitmaskBoard[r + i] |= shiftedRow; // 2. Add to board using OR operation
        }

        // 3. Update visualBoard
        for (int i = 0; i < blockHeight; i++) {
            for (int j = 0; j < blockWidth; j++) {
                if ((blockMask[i] & (1 << (blockWidth - j - 1))) != 0) {
                    visualBoard[r + i][c + j] = blockID; // Set block character
                }
            }
        }

        // 4. Update filled and empty spaces
        this.filledSpace += block.getSize(); 
        this.emptySpace -= block.getSize();
    }

    public void removeBlock(Block block, int r, int c) {
        int[] blockMask = block.getBitmaskBlock();
        int blockHeight = block.getRow();
        int blockWidth = block.getColumn();
        
        // 1. Remove block from bitmaskBoard using XOR to clear the bits
        for (int i = 0; i < blockHeight; i++) {
            int shiftedRow = blockMask[i] << (col - blockWidth - c);
            bitmaskBoard[r + i] ^= shiftedRow; // XOR removes the placed bits
        }
    
        // 2. Clear visualBoard
        for (int i = 0; i < blockHeight; i++) {
            for (int j = 0; j < blockWidth; j++) {
                if ((blockMask[i] & (1 << (blockWidth - j - 1))) != 0) {
                    visualBoard[r + i][c + j] = ' '; // Reset to empty space
                }
            }
        }
    
        // 3. Update filled and empty spaces
        this.filledSpace -= block.getSize();
        this.emptySpace += block.getSize();
    }
    


    // Setters and Getters

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

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        this.col = col;
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
