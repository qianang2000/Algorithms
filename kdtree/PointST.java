import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Stack;

public class PointST<Value> {
    RedBlackBST<Point2D, Value> st;

    // construct an empty symbol table of points
    public PointST() {
        st = new RedBlackBST<>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return st.isEmpty();
    }

    // number of points
    public int size() {
        return st.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new IllegalArgumentException();
        }
        st.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return st.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return st.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Stack<Point2D> stack = new Stack<>();
        for (Point2D p : st.keys()) {
            stack.push(p);
        }
        return stack;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Stack<Point2D> stack = new Stack<>();
        for (Point2D p : st.keys()) {
            if (rect.contains(p)) {
                stack.push(p);
            }
        }
        return stack;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        Point2D nearest = st.max();
        double distance = Double.POSITIVE_INFINITY;
        for (Point2D p2 : st.keys()) {
            double d = p2.distanceSquaredTo(p);
            if (d < distance) {
                distance = d;
                nearest = p2;
            }
        }
        return nearest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> pst = new PointST<>();
        Point2D p1 = new Point2D(1.8, 2.2);
        Point2D p2 = new Point2D(3.1, 4.0);
        Point2D p3 = new Point2D(5.4, 2.1);
        Point2D p4 = new Point2D(6.3, 4.4);
        Point2D p5 = new Point2D(7.2, 3.5);
        Point2D p6 = new Point2D(2.0, 3.3);
        Point2D p7 = new Point2D(5.3, 2.0);
        pst.put(p1, 3);
        pst.put(p2, 9);
        pst.put(p3, 4);
        pst.put(p4, 5);
        pst.put(p5, 6);
        pst.put(p6, 7);
        System.out.println(pst.isEmpty()); // false
        System.out.println(pst.size()); // 6
        System.out.println(pst.get(p1)); // 3
        System.out.println(pst.get(p2)); // 9
        System.out.println(pst.contains(p6)); // true
        System.out.println(pst.contains(p7)); // false
        RectHV rect = new RectHV(2.0, 2.0, 4.0, 4.0);
        System.out.println("All the points in the Point2D symbol table:");
        for (Point2D p : pst.points()) {
            System.out.println(p.toString());
        }
        System.out.println("All the points inside the rectangle:");
        for (Point2D p : pst.range(rect)) {
            System.out.println(p.toString());
        }
        System.out.println("The nearest point to (5.3, 2.0):");
        System.out.println(pst.nearest(p7).toString());
    }
}

