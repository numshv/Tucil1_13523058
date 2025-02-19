import java.util.Scanner;
import Functions.Board;
import Functions.Block;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Block blockA = new Block('A', 2, 2, new char[][]{{'A', ' '}, {' ', 'A'}});
        System.out.println(blockA.getSize());
        blockA.printBitmask();
        blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();
        // blockA.rotate90();
        // blockA.printBlock();


        Board board = new Board(5, 4);
        board.printBitmaskBoard();
        board.printBoard();

        board.addBlock(blockA, 0, 0);
        board.printBitmaskBoard();
        board.printBoard();
    }
}
