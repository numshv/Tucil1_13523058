package Functions;

public class Block {

    // Attributes
    private boolean is_placed;
    private char blockID;
    private int length, width;
    private int[] bitmaskBlock;     // Bitmask block for fast fit checking
    private char[][] visualBlock;   // ??Character block for final printing
    private int size;

    // Constructor
    public Block(char blockID, int length, int width, char[][] visualBlock) {
        this.blockID = blockID;
        this.length = length;
        this.width = width;
        this.visualBlock = visualBlock;
        this.size = 0;
        this.is_placed = false;
        this.bitmaskBlock = new int[length];  // Each row is stored as an integer bitmask

        for (int row = 0; row < length; row++) {
            int bitmask = 0;
            for (int col = 0; col < width; col++) {
                if (visualBlock[row][col] != ' ') {  // Non-space character → Set bit to 1
                    this.size++;
                    bitmask |= (1 << (width - col - 1));  // Set bit from right to left
                }
            }
            this.bitmaskBlock[row] = bitmask;  // Store in bitmaskBlock
        }
    }

    // Print the bitmask representation (for debugging)
    public void printBitmask() {
        System.out.println("Bitmask representation of block " + blockID + ":");
        for (int row : bitmaskBlock) {
            System.out.println(String.format("%" + width + "s", Integer.toBinaryString(row)).replace(' ', '0'));
        }
    }

    public void printBlock() {
        System.out.println("Visual representation of block " + blockID + ":");
        for (int row = 0; row < length; row++) {
            StringBuilder sb = new StringBuilder();
            for (int col = 0; col < width; col++) {
                if ((bitmaskBlock[row] & (1 << (width - col - 1))) != 0) {
                    sb.append(blockID);  // Use block ID instead of '1'
                } else {
                    sb.append(" ");  // Empty space
                }
            }
            System.out.println(sb.toString());
        }
    }

    public void rotate90() {
        int[] rotated = new int[width];  // New bitmask after rotation (flipped width & height)
    
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < width; col++) {
                if ((bitmaskBlock[row] & (1 << (width - col - 1))) != 0) { // Check if bit is set
                    rotated[col] |= (1 << row);  // Move it to the new rotated position
                }
            }
        }
    
        // Swap width and length after rotation
        this.bitmaskBlock = rotated;
        int temp = this.length;
        this.length = this.width;
        this.width = temp;
    }
    

    // Setters Getters

    public void setIsPlaced(boolean is_placed) {
        this.is_placed = is_placed;
    }

    public boolean getIsPlaced() {
        return this.is_placed;
    }

    public char getBlockID() {
        return this.blockID;
    }

    public int getLength() {
        return this.length;
    }

    public int getWidth() {
        return this.width;
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
