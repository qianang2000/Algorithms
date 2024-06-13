import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeST<Value> {

    private Node root; // root of BST

    private class Node {
        private Point2D key;  // the point
        private Value val;  // the value associated with the node
        private RectHV rect;  // the corresponding rectangle bounding box
        private int size;  // the size of the subtree rooted at the node
        private Node lb;  // the left/bottom subtree
        private Node rt;  // the right/top subtree

        // constructor of the node
        public Node(Point2D key, Value val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }


    // Check if the symbol table is empty.
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the size of KD tree
    public int size() {
        return size(root);
    }

    // helper method of size
    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    // associate the value val with node p
    public void put(Point2D key, Value val) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        if (val == null) return;
        if (root == null) {
            root = new Node(key, val, 1);
            root.rect = new RectHV(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY,
                                   Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        root = put(root, key, val, true);
    }

    // helper method of the put
    private Node put(Node x, Point2D key, Value val, boolean orientation) {
        // orientation true: vertical, false: horizontal
        if (x == null) {
            return new Node(key, val, 1);
        }
        // compare the node key with the key to be inserted based on orientation
        double cmp;
        if (orientation) {
            cmp = key.x() - x.key.x();
        }
        else {
            cmp = key.y() - x.key.y();
        }
        double xmin = x.rect.xmin();
        double xmax = x.rect.xmax();
        double ymin = x.rect.ymin();
        double ymax = x.rect.ymax();
        // put the key based on the value of cmp
        if (cmp < 0) {
            x.lb = put(x.lb, key, val, !orientation);
            // update the bounding rect
            if (orientation) {
                double value = Math.min(xmax, x.key.x());
                x.lb.rect = new RectHV(xmin, ymin, value, ymax);
            }
            else {
                double value = Math.min(ymax, x.key.y());
                x.lb.rect = new RectHV(xmin, ymin, xmax, value);
            }
        }
        else if (cmp > 0) {
            x.rt = put(x.rt, key, val, !orientation);
            // update the bounding rect
            if (orientation) {
                double value = Math.max(xmin, x.key.x());
                x.rt.rect = new RectHV(value, ymin, xmax, ymax);
            }
            else {
                double value = Math.max(ymin, x.key.y());
                x.rt.rect = new RectHV(xmin, value, xmax, ymax);
            }
        }
        else {
            x.val = val;
        }
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }


    // value associated with point p
    public Value get(Point2D key) {
        return get(root, key, true);
    }

    // helper method of get
    private Value get(Node x, Point2D key, boolean orientation) {
        if (key == null) throw new IllegalArgumentException("calls get() with a null key");
        if (x == null) return null;
        // compare the node key with the key to be inserted based on orientation
        double cmp;
        if (orientation) {
            cmp = key.x() - x.key.x();
        }
        else {
            cmp = key.y() - x.key.y();
        }
        // get the key based on the value of cmp
        if (cmp < 0) {
            return get(x.lb, key, !orientation);
        }
        else if (cmp > 0) {
            return get(x.rt, key, !orientation);
        }
        else {
            return x.val;
        }
    }

    // check if the Kd tree contains a specific key
    public boolean contains(Point2D key) {
        if (key == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(key) != null;
    }

    // returns all keys in the symbol table in the given range, and we use level order here
    public Iterable<Point2D> points() {
        Queue<Point2D> keys = new Queue<>(); // queue to store the result
        Queue<Node> queue = new Queue<>();  // queue to store the intermediate state
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.key);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return keys;
    }

    // check the bounding rectangle of each node
    private void testBounding(Node x) {
        if (x == null) return;
        testBounding(x.lb);
        System.out.println(x.key.toString() + " " + x.rect.toString());
        testBounding(x.rt);
    }

    // print the bounding rectangle of each node in order
    private void printRect() {
        testBounding(root);
    }

    // Iterate the points inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<>();
        range(root, rect, queue);
        return queue;
    }

    // helper function of range method
    private Iterable<Point2D> range(Node x, RectHV rect, Queue<Point2D> queue) {
        if (rect == null) throw new IllegalArgumentException("argument to range() is null");
        if (x == null) return null;
        if (rect.contains(x.key)) queue.enqueue(x.key);
        if (x.rect.intersects(rect)) {
            range(x.lb, rect, queue);
            range(x.rt, rect, queue);
        }
        // rectangle on the left or below the bounding box, search left subtree
        else if (rect.xmax() < x.rect.xmin() || rect.ymax() < x.rect.ymin()) {
            range(x.lb, rect, queue);
        }
        else {
            range(x.rt, rect, queue);
        }
        return queue;
    }


    // Find the nearest neighbor of a given point
    public Point2D nearest(Point2D key) {
        return findNearest(root, key).key;
    }

    // helper function of nearest
    private Node findNearest(Node x, Point2D key) {
        if (key == null) throw new IllegalArgumentException("argument to nearest() is null");
        if (x == null) return null;
        Node champ = x;
        double dist = champ.key.distanceSquaredTo(key);
        // if the closest point discovered so far is closer than the distance between the query
        // point and the rectangle corresponding to a node, there is no need to explore that node
        // (or its subtrees).
        if (dist < champ.rect.distanceSquaredTo(key)) {
            return null;
        }
        //  when there are two subtrees to explore, always choose first the subtree that is on the
        //  same side of the splitting line as the query point
        Node left = findNearest(x.lb, key);
        Node right = findNearest(x.rt, key);
        if (left != null && left.key.distanceSquaredTo(key) < dist) {
            champ = left;
            dist = champ.key.distanceSquaredTo(key);
        }
        if (right != null && right.key.distanceSquaredTo(key) < dist) {
            champ = right;
            dist = champ.key.distanceSquaredTo(key);
        }
        return champ;
    }


    public static void main(String[] args) {
        KdTreeST<Integer> st = new KdTreeST<>();
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.9, 0.6);
        Point2D p4 = new Point2D(0.2, 0.3);
        Point2D p5 = new Point2D(0.4, 0.7);
        st.put(p1, 14);
        st.put(p2, 22);
        st.put(p3, 18);
        st.put(p4, 33);
        st.put(p5, 29);
        Point2D pt = new Point2D(0.4, 0.8);
        System.out.println(st.nearest(pt).toString());

    }
}
