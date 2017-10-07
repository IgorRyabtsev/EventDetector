package ya.eventdetector.treeversion;

public class Node {

    long timeInMillis;
    long randomKey;
    long height;
    Node left, right;

    public Node(long timeInMillis, long randomKey) {
        this.timeInMillis = timeInMillis;
        this.height = 0;
        this.randomKey = randomKey;
    }
}
