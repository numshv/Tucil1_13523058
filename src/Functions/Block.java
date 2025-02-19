package Functions;

public class Block {

    // Attributes
    private boolean is_placed;
    private char blockID;
    private int row, column;
    private int[] bitmaskBlock;     // block shape in bitmask representation
    private char[][] visualBlock;   // block shape in alphabet representation
    private int size;

    // Constructor
    public Block(char blockID, int row, int column, char[][] visualBlock) {
        this.blockID = blockID;
        this.row = row;
        this.column = column;
        this.visualBlock = visualBlock;
        this.size = 0;
        this.is_placed = false;
        this.bitmaskBlock = new int[row];  // Each row is stored as an integer bitmask

        for (int r = 0; r < row; r++) {
            int bitmask = 0;
            for (int c = 0; c < column; c++) {
                if (visualBlock[r][c] != ' ') {  
                    this.size++;
                    bitmask |= (1 << (column - c - 1));  // Set bit from right to left
                }
            }
            this.bitmaskBlock[r] = bitmask;  
        }
    }

    public void printBitmask() {
        System.out.println("Bitmask representation of block " + blockID + ":");
        for (int r : bitmaskBlock) {
            System.out.println(String.format("%" + column + "s", Integer.toBinaryString(r)).replace(' ', '0'));
        }
    }

    public void printBlock() {
        System.out.println("Visual representation of block " + blockID + ":");
        for (int r = 0; r < row; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < column; c++) {
                if ((bitmaskBlock[r] & (1 << (column - c - 1))) != 0) {
                    sb.append(blockID); 
                } else {
                    sb.append(" ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    public void rotate90() {
        int[] rotated = new int[column]; 
    
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                if ((bitmaskBlock[r] & (1 << (column - c - 1))) != 0) { 
                    rotated[c] |= (1 << r); 
                }
            }
        }
    
        this.bitmaskBlock = rotated;
        int temp = this.row;
        this.row = this.column;
        this.column = temp;
    }

    // Setters and Getters

    public void setIsPlaced(boolean is_placed) {
        this.is_placed = is_placed;
    }

    public boolean getIsPlaced() {
        return this.is_placed;
    }

    public char getBlockID() {
        return this.blockID;
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public int[] getBitmaskBlock() {
        return this.bitmaskBlock;
    }

    public char[][] getVisualBlock() {
        return this.visualBlock;
    }
    
    public int getSize() {
        return this.size;
    }
}
