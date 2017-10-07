package ya.eventdetector.treeversion;

import ya.eventdetector.EventCounter;

import java.util.Calendar;
import java.util.Random;

import static ya.eventdetector.util.Utils.*;


// I decide to use decart balanced tree(treap) to make all operations for O(logn)6 but altho we can use AVL tree
public class EventCounterImplTree implements EventCounter {

    private final Random random;
    private final long TREE_CLEAR_PERIOD = 10000000;
    private Node root;

    public EventCounterImplTree() {
        random = new Random();
        root = null;
    }

    //add the event to tree
    @Override
    public void addEvent() {
        synchronized (random) {
            final long timeInMillis = getCurrentTimeInMillis();
            deleteOldValues(timeInMillis);
            final Node newNode = new Node(timeInMillis, random.nextInt());
            root = insert(root, newNode);
        }
    }

    @Override
    public long getCountOfEventsPerMinute() {
        return getNumberOfEvents(root, getCurrentTimeInMillis() - MINUTE);
    }

    @Override
    public long getCountOfEventsPerHour() {
        return getNumberOfEvents(root, getCurrentTimeInMillis() - HOUR);
    }

    @Override
    public long getCountOfEventsPerDay() {
        return getNumberOfEvents(root, getCurrentTimeInMillis() - DAY);
    }

    private long getCurrentTimeInMillis() {
        final Calendar data = Calendar.getInstance();
        return data.getTimeInMillis();
    }

    //deletes the old value from tree
    private void deleteOldValues(final long timeInMillis) {
        if (root == null || root.height % TREE_CLEAR_PERIOD != 0) {
            return;
        }
        final Node[] splited = split(root, timeInMillis - DAY);
        root = splited[RIGHT];
    }

    private long getNumberOfEvents(final Node currentRoot, final long timeInMillis) {
        synchronized (random) {
            if (currentRoot == null) {
                return 0;
            }
            final long number = (root == null ? 0 : root.height) - findCount(root, timeInMillis);
            return number;
        }
    }

    // inserts the node to the tree
    private Node insert(final Node currentRoot, final Node node) {
        // res = root of new tree
        if (currentRoot == null) {
            recalculateHeight(node);
            return node;
        }
        if (currentRoot.randomKey < node.randomKey) {
            final Node[] splitted = split(currentRoot, node.timeInMillis);
            node.left = splitted[LEFT];
            node.right = splitted[RIGHT];
            recalculateHeight(node);
            return node;
        }
        if (node.timeInMillis < currentRoot.timeInMillis) {
            currentRoot.left = insert(currentRoot.left, node);
            recalculateHeight(currentRoot);
            return currentRoot;
        } else {
            currentRoot.right = insert(currentRoot.right, node);
            recalculateHeight(currentRoot);
            return currentRoot;
        }
    }

    // splits the tree by value
    private Node[] split(final Node currentRoot, final long value) {
        // res[0]: < timeInMillis
        // res[1]: >= timeInMillis
        final Node[] splittedTrees = new Node[2];
        if (currentRoot == null) {
            return splittedTrees;
        }
        if (currentRoot.timeInMillis < value) {
            final Node[] splitted = split(currentRoot.right, value);
            currentRoot.right = splitted[LEFT];
            splittedTrees[LEFT] = currentRoot;
            splittedTrees[RIGHT] = splitted[RIGHT];
        } else {
            final Node[] t = split(currentRoot.left, value);
            currentRoot.left = t[RIGHT];
            splittedTrees[LEFT] = t[LEFT];
            splittedTrees[RIGHT] = currentRoot;
        }
        recalculateHeight(splittedTrees[LEFT]);
        recalculateHeight(splittedTrees[RIGHT]);
        return splittedTrees;
    }

    private long findCount(final Node currentRoot, final long timeInMills) {
        if (currentRoot == null) {
            return 0;
        }
        long count = 0;
        if (currentRoot.timeInMillis > timeInMills) {
            count += findCount(currentRoot.left, timeInMills);
        } else {
            Node leftNode = currentRoot.left;
            count += findCount(currentRoot.right, timeInMills) + (leftNode == null ? 0 : leftNode.height) + 1;
        }
        return count;
    }

    private void recalculateHeight(final Node root) {
        if (root == null) return;
        root.height = sizeOf(root.left) + sizeOf(root.right) + 1;
    }

    private long sizeOf(final Node root) {
        return root == null ? 0L : root.height;
    }

    //methods for testing

    public void printTree() {
        print(root);
    }

    private void print(final Node root) {
        if (root == null) {
            return;
        }
        print(root.left);
        System.out.println(root.timeInMillis + " " + root.randomKey + " " + root.height);
        print(root.right);
    }

    public void addSpecialEvent(final String timeLimit) {
        synchronized (random) {
            long timeInMillis;
            switch (timeLimit) {
                case "HOUR":
                    timeInMillis = getCurrentTimeInMillis() - HOUR;
                    break;
                case "DAY":
                    timeInMillis = getCurrentTimeInMillis() - DAY;
                    break;
                default:
                    timeInMillis = 0L;
            }
            deleteOldValues(timeInMillis);
            Node newNode = new Node(timeInMillis, random.nextInt());
            root = insert(root, newNode);
        }
    }

}
