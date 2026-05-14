// ENGINE
// 1. with x&y range, get multiple points and store them in the data structure(what data structure?)
// 1. generate 4 random numbers
// 2. combine them as a floating number
// 2. get 4 floating numbers(x1, x2, y1, y2), executing searching algorithm

// DATA STRUCTURE
// 1. 1-D range tree
// 1. sort and store the x value of points in an array
// 2. build a tree based on the array(each node stores the value of the rightmost
// leaf and the number of node it contains)

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

class Point {
    int x;
    int y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class AssocTree {
    private class Node {
        Point value;
        int node_num;
        Node left, right;

        Node(Point value, int node_num) {
            this.value = value;
            this.node_num = node_num;
            this.left = null;
            this.right = null;
        }
    }

    Node root = null;
    int result_num = 0;

    public AssocTree(List<Point> list) {
        root = list_to_BST(list);
    }

    private Node list_to_BST(List<Point> list) {
        if (list.size() == 1) {
            return new Node(list.get(0), 1);
        } else {
            int mid = list.size() / 2 - 1;
            Node node = new Node(list.get(mid), list.size());
            node.left = list_to_BST(list.subList(0, mid + 1));
            node.right = list_to_BST(list.subList(mid + 1, list.size()));
            return node;
        }
    }

    public void print_tree(Node node) {
        print_tree_recursive(node, 0);
    }

    private void print_tree_recursive(Node node, int level) {
        if (node == null) return;

        print_tree_recursive(node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println("[" + node.value.x + ", " + node.value.y + "]");
        print_tree_recursive(node.left, level + 1);
    }

    private void print_BST(Node node) {
        if (node == null) return;
        result_num += node.node_num;
//        System.out.println("the number of nodes in the subtree " + node.value.x + " is " + node.node_num);
    }

    private Node find_split_node(Node node, int y_1, int y_2) {
        while ((node.left != null && node.right != null) && (y_2 <= node.value.y || y_1 > node.value.y)) {
            if (y_2 <= node.value.y) {
                node = node.left;
            }
            else {
                node = node.right;
            }
        }
        return node;
    }

    public void range_query(int y_1, int y_2) {
        result_num = 0;
        Node node_split = find_split_node(root, y_1, y_2);
        if (node_split.left == null && node_split.right == null) {
            if (node_split.value.y >= y_1 && node_split.value.y <= y_2) {
                System.out.println("[" + node_split.value.x + ", " + node_split.value.y + "]");
                result_num++;
            }
        }
        else {
            // Path to y_1
            Node node = node_split.left;
            while (node.left != null && node.right != null) {
                if (y_1 <= node.value.y) {
                    print_BST(node.right);
                    node = node.left;
                }
                else {
                    node = node.right;
                }
            }
            // Check whether the leaf must be reported
            if (node.value.y >= y_1 && node.value.y <= y_2) {
                System.out.println("[" + node.value.x + ", " + node.value.y + "]");
                result_num++;
            }

            // Path to y_2
            node = node_split.right;
            while (node.left != null && node.right != null) {
                if (y_2 >= node.value.y) {
                    print_BST(node.left);
                    node = node.right;
                }
                else {
                    node = node.left;
                }
            }
            // Check whether the lead must be reported
            if (node.value.y >= y_1 && node.value.y <= y_2) {
                System.out.println("[" + node.value.x + ", " + node.value.y + "]");
                result_num++;
            }
        }
    }
}

class BST {
    private class Node {
        int value;
        int node_num;
        Node left, right;
        AssocTree tree;

        Node(int value, int node_num) {
            this.value = value;
            this.node_num = node_num;
            this.left = null;
            this.right = null;
        }
    }

    Node root = null;
    int result_num = 0;

    public BST(List<Point> list) {
        root = list_to_BST(list);
    }

    public void print_tree() {
        print_tree_recursive(root, 0);
    }

    private void print_tree_recursive(Node node, int level) {
        if (node == null) return;

        print_tree_recursive(node.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("    ");
        }
        System.out.println(node.value);
        print_tree_recursive(node.left, level + 1);
    }

    private void print_BST(Node node, int y_1, int y_2) {
        if (node == null) return;
        node.tree.range_query(y_1, y_2);
        result_num += node.tree.result_num;
    }

    private Node list_to_BST(List<Point> list) {
        if (list.size() == 1) {
            Node node = new Node(list.get(0).x, 1);
            node.tree = new AssocTree(list);
            return node;
        }
        else {
            int mid = list.size() / 2 - 1;
            Node node = new Node(list.get(mid).x, list.size());
            List<Point> list_sorted_y = new ArrayList<>(list);
            list_sorted_y.sort(Comparator.comparingInt(p -> p.y));
            node.tree = new AssocTree(list_sorted_y);

            node.left = list_to_BST(list.subList(0, mid + 1));
            node.right = list_to_BST(list.subList(mid + 1, list.size()));
            return node;
        }
    }

    private Node find_split_node(Node node, int x_1, int x_2) {
        while ((node.left != null && node.right != null) && (x_2 <= node.value || x_1 > node.value)) {
            if (x_2 <= node.value) {
                node = node.left;
            }
            else {
                node = node.right;
            }
        }
        return node;
    }

    // no print, just traverse
    public void range_query(int x_1, int x_2, int y_1, int y_2) {
        result_num = 0;
        Node node_split = find_split_node(root, x_1, x_2);
        if (node_split.left == null && node_split.right == null) {
            if (node_split.value >= x_1 && node_split.value <= x_2) {
                node_split.tree.range_query(y_1, y_2);
                result_num += node_split.tree.result_num;
            }
        }
        else {
            // Path to x_1
            Node node = node_split.left;
            while (node.left != null && node.right != null) {
                if (x_1 <= node.value) {
                    print_BST(node.right, y_1, y_2);
                    node = node.left;
                }
                else {
                    node = node.right;
                }
            }
            // Check whether the lead must be reported
            if (node.value >= x_1 && node.value <= x_2) {
                node.tree.range_query(y_1, y_2);
                result_num += node.tree.result_num;
            }

            // Path to x_2
            node = node_split.right;
            while (node.left != null && node.right != null) {
                if (x_2 >= node.value) {
                    print_BST(node.left, y_1, y_2);
                    node = node.right;
                }
                else {
                    node = node.left;
                }
            }
            // Check whether the lead must be reported
            if (node.value >= x_1 && node.value <= x_2) {
                node.tree.range_query(y_1, y_2);
                result_num += node.tree.result_num;
            }
        }
    }
}

class PointsGenerator {

    public ArrayList<Point> getPoints(int num) {
        Random rand = new Random();
        ArrayList<Point> list = new ArrayList<>();
        while (list.size() < num) {
            int num_1 = rand.nextInt(10);
            int num_2 = rand.nextInt(10);
            int num_3 = rand.nextInt(10);
            int num_4 = rand.nextInt(10);
            int x = num_1 * 1000 + num_2 * 100 + num_3 * 10 + num_4;

            num_1 = rand.nextInt(10);
            num_2 = rand.nextInt(10);
            num_3 = rand.nextInt(10);
            num_4 = rand.nextInt(10);
            int y = num_1 * 1000 + num_2 * 100 + num_3 * 10 + num_4;

            boolean repeated = false;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(j).x == x || list.get(j).y == y) {
                    repeated = true;
                    break;
                }
            }
            if (!repeated) list.add(new Point(x,y));
        }
        return list;
    }

    public void printPoints(ArrayList<Point> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("(" + list.get(i).x + ", " + list.get(i).y + ")");
        }
    }

    public int getRange_1() {
        Random rand = new Random();
        int num_1 = rand.nextInt(10);
        int num_2 = rand.nextInt(10);
        int num_3 = rand.nextInt(10);
        int num_4 = rand.nextInt(10);
        return num_1 * 1000 + num_2 * 100 + num_3 * 10 + num_4;
    }

    public int getRange_2(int range_1) {
        Random rand = new Random();
        while (true) {
            int num_1 = rand.nextInt(10);
            int num_2 = rand.nextInt(10);
            int num_3 = rand.nextInt(10);
            int num_4 = rand.nextInt(10);
            int range_2 = num_1 * 1000 + num_2 * 100 + num_3 * 10 + num_4;
            if (range_2 >= range_1) return range_2;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Point> test = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            test.add(new Point(i, i));
        }

        // Prepare the dataset
        int size = 2000;
        PointsGenerator PG = new PointsGenerator();
        ArrayList<Point> list = PG.getPoints(size);

        // Sort the points based on x value
        List<Point> list_sorted_x = new ArrayList<>(list);
        list_sorted_x.sort(Comparator.comparingInt(p -> p.x));

        // Construct the range tree
        BST tree = new BST(list_sorted_x);
        double total_time = 0;
        double max_time = -1;
        double min_time = 0;
        double points_num = 0.0;

        int count = 1000;
        for (int i = 0; i < count; i++) {
            int x_1 = PG.getRange_1();
            int x_2 = PG.getRange_2(x_1);
            int y_1 = PG.getRange_1();
            int y_2 = PG.getRange_2(y_1);
            System.out.println("Range:" + x_1 + ", " + x_2 + ", " + y_1 + ", " + y_2);


            long start = System.nanoTime();
            tree.range_query(x_1, x_2, y_1, y_2);

            long end = System.nanoTime();
            long duration = end - start;
            double ms = duration / 1_000_000.0;
            total_time += ms;
            points_num += tree.result_num;
            System.out.println(ms + " ms");
            System.out.println("Number of points in the range: " + tree.result_num);

            if (ms > max_time) max_time = ms;
            if (i == 0) min_time = ms;
            else if (ms < min_time) min_time = ms;
        }

        System.out.println("Average search time: " + total_time / count);
        System.out.println("Maximum search time: " + max_time);
        System.out.println("Minimum search time: " + min_time);
        System.out.println("Average number of points: " + points_num / count);
    }
}
