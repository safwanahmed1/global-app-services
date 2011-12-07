package uno.com;

import java.util.ArrayList;

public class Node {

    public int moveBox;
    public int point;
    public Board board;
    public Node parent;
    public ArrayList<Node> childrenNode = new ArrayList<Node>();

    public Node() {
        this.board = new Board(Board.intFirstMove);
    }

    public Node(Board board) {
        this.board = board;
    }

    public void AddChildren(Node node) {
        childrenNode.add(node);
        node.Parent(this);
    }

    private void Parent(Node node) {
        this.parent = node;
    }

    public void copy(Node n) {
        this.point=n.point;

        this.board.copy(n.board);

    }

    public void Print() {
        System.out.println();
        System.out.println("Point" + this.point);
        System.out.println("moveBox" + this.moveBox);
        System.out.println();
    }

}