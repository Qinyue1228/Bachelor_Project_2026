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
    public class Node {
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
//                System.out.println("[" + node_split.value.x + ", " + node_split.value.y + "]");
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
//                System.out.println("[" + node.value.x + ", " + node.value.y + "]");
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
//                System.out.println("[" + node.value.x + ", " + node.value.y + "]");
                result_num++;
            }
        }
    }

    public void delete_point(int target_y) {
        // 1. Search the target point
        Node prev_node = null;
        Node curr_node = root;

        int left_right_flag = 0; // If flag = 0, the target node is on the left subtree, otherwise that is on the right subtree
        while (curr_node.node_num != 1) {
            if (target_y <= curr_node.value.y) {
                prev_node = curr_node;
                if (curr_node.left == null) break;
                curr_node = curr_node.left;
                left_right_flag = 0;
            }
            else {
                prev_node = curr_node;
                if (curr_node.right == null) break;
                curr_node = curr_node.right;
                left_right_flag = 1;
            }
        }
        if (curr_node.value.y == target_y) {
            // 2. If found the target point, delete it
//            System.out.println("We got it in the ASSOC tree!");
            if (curr_node == root) root = null;
            else {
                Node node_before_target = prev_node;
                if (left_right_flag == 0) {
                    curr_node = root;
                    left_right_flag = 0;
                    while (curr_node != node_before_target) {
                        if (target_y <= curr_node.value.y) {
                            curr_node.node_num--;
                            prev_node = curr_node;
                            curr_node = curr_node.left;
                            left_right_flag = 0;
                        } else {
                            curr_node.node_num--;
                            prev_node = curr_node;
                            curr_node = curr_node.right;
                            left_right_flag = 1;
                        }
                    }
                    if (left_right_flag == 0) {
                        prev_node.left = curr_node.right;
                    } else {
                        prev_node.right = curr_node.right;
                    }
                } else {
                    curr_node = root;
                    int rightmost_flag = 1;
                    while (curr_node != node_before_target) {
                        if (target_y <= curr_node.value.y) {
                            if (curr_node.value.y == target_y) {
                                curr_node.value = node_before_target.value;
                                rightmost_flag = 0;
                            }
                            curr_node.node_num--;
                            curr_node = curr_node.left;
                        } else {
                            curr_node.node_num--;
                            curr_node = curr_node.right;
                        }
                    }
//                    System.out.println("===> [" + curr_node.value.x + ", "  + curr_node.value.y + "]" + ", " + curr_node.node_num);
                    curr_node.node_num -= 1;
                    curr_node.right = null;
                }
            }
        }
        else {
//            System.out.println("We don't have the point with the value of y = " + target_y + "in the ASSOC tree");
//            print_tree(root);
        }
    }

    public void insert_point(int x, int y) {
        Point point = new Point(x, y);

        if (root.node_num == 1) {
            if (root.value.y > y) {
                root.node_num = 2;
                root.left = new Node(point, 1);
                root.right = new Node(root.value, 1);
            }
            else if (root.value.y < y) {
                root.node_num = 2;
                root.left = new Node(root.value, 1);
                root.right = new Node(point, 1);
            }
            return;
        }

        // 1. Find the place of the new point
        Node prev_node = null;
        Node curr_node = root;

        int left_right_flag = 0; // If flag = 0, the target node is on the left subtree, otherwise that is on the right subtree
        while (curr_node.node_num != 1) {
            if (y <= curr_node.value.y) {
                prev_node = curr_node;
                curr_node = curr_node.left;
                left_right_flag = 0;
            }
            else {
                prev_node = curr_node;
                curr_node = curr_node.right;
                left_right_flag = 1;
            }
        }

        if (left_right_flag == 0) {
            Node target_node = curr_node;
            curr_node = root;

            while (curr_node != target_node) {
                if (y <= curr_node.value.y) {
                    curr_node.node_num++;
                    curr_node = curr_node.left;
                } else {
                    curr_node.node_num++;
                    curr_node = curr_node.right;
                }
            }
            Point neighbour_point = curr_node.value;
            curr_node.node_num = 2;
            curr_node.value = point;

            if (neighbour_point.y < y) {
                curr_node.left = new Node(neighbour_point, 1);
                curr_node.right = new Node(point, 1);
            }
            else {
                curr_node.left = new Node(point, 1);
                curr_node.right = new Node(neighbour_point, 1);
            }
        }

        else {
            Node target_node = curr_node;
            curr_node = root;
            int rightmost_flag = 1;

            while (curr_node != target_node) {
                if (y <= curr_node.value.y) {
                    if (curr_node.value.y == target_node.value.y) {
                        rightmost_flag = 0;
                    }
                    curr_node.node_num++;
                    curr_node = curr_node.left;
                } else {
                    curr_node.node_num++;
                    curr_node = curr_node.right;
                }
            }

            Point neighbour_point = curr_node.value;
            curr_node.node_num = 2;
            if (rightmost_flag == 1 && y > target_node.value.y) curr_node.value = target_node.value;
            else curr_node.value = point;

            if (neighbour_point.y < y) {
                curr_node.left = new Node(neighbour_point, 1);
                curr_node.right = new Node(point, 1);
            }
            else {
                curr_node.left = new Node(point, 1);
                curr_node.right = new Node(neighbour_point, 1);
            }
        }
//        print_tree(root);
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
    Boolean is_deleted = false;
    Boolean is_inserted = false;

    public BST(List<Point> list) {
        root = list_to_BST(list);
    }

    public int height(Node node) {
        if (node.node_num == 1) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
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
        System.out.println(node.value + ", " + node.node_num);
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

    public void delete_point(int target_x, int target_y) {
        // 1. Search the target point
        Node prev_node = null;
        Node curr_node = root;

        int left_right_flag = 0; // If flag = 0, the target node is on the left subtree, otherwise that is on the right subtree
        while (curr_node.node_num != 1) {
            if (target_x <= curr_node.value) {
                prev_node = curr_node;
                if (curr_node.left == null) break;
                curr_node = curr_node.left;
                left_right_flag = 0;
            }
            else {
                prev_node = curr_node;
                if (curr_node.right == null) break;
                curr_node = curr_node.right;
                left_right_flag = 1;
            }
        }
        if (curr_node.value == target_x) {
            int p_y = curr_node.tree.root.value.y;
            if (p_y == target_y) {
                // 2. If found the target point, change the associate tree on the path to it
//                System.out.println("We got it!");
                is_deleted = true;
                if (curr_node == root) root = null;
                else {
                    Node node_before_target = prev_node;
                    if (left_right_flag == 0) {
                        curr_node = root;
                        left_right_flag = 0;
                        while (curr_node != node_before_target) {
                            if (target_x <= curr_node.value) {
                                curr_node.node_num--;
                                curr_node.tree.delete_point(target_y);
                                prev_node = curr_node;
                                curr_node = curr_node.left;
                                left_right_flag = 0;
                            } else {
                                curr_node.node_num--;
                                curr_node.tree.delete_point(target_y);
                                prev_node = curr_node;
                                curr_node = curr_node.right;
                                left_right_flag = 1;
                            }
                        }
                        if (left_right_flag == 0) {
                            prev_node.left = curr_node.right;
                        } else {
                            prev_node.right = curr_node.right;
                        }
                    } else {
                        curr_node = root;
                        int rightmost_flag = 1;
                        while (curr_node != node_before_target) {
                            if (target_x <= curr_node.value) {
                                if (curr_node.value == target_x) {
                                    curr_node.value = node_before_target.value;
                                    rightmost_flag = 0;
                                }
                                curr_node.node_num--;
                                curr_node.tree.delete_point(target_y);
                                curr_node = curr_node.left;
                            } else {
                                curr_node.node_num--;
                                curr_node.tree.delete_point(target_y);
                                curr_node = curr_node.right;
                            }
                        }
//                        System.out.println("===>" + curr_node.value + ", " + curr_node.node_num);
                        curr_node.node_num -= 1;
                        curr_node.right = null;
                        curr_node.tree.delete_point(target_y);
                    }
                }
            }
//            else System.out.println("We don't have the point with the value of y = " + target_y);
        }
//        else System.out.println("We don't have the point with the value of x = " + target_x);
    }

    public void insert_point(int x, int y) {
        ArrayList<Point> list = new ArrayList<>();

        if (root.node_num == 1) {
            if (root.value > x) {
                is_inserted = true;
                root.node_num = 2;

                root.left = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                root.left.tree = new AssocTree(list);

                root.right = new Node(root.value, 1);
                list = new ArrayList<>();
                list.add(root.tree.root.value);
                root.right.tree = new AssocTree(list);

                root.tree.insert_point(x, y);
            }
            else if (root.value < x) {
                is_inserted = true;
                root.node_num = 2;

                root.left = new Node(root.value, 1);
                list = new ArrayList<>();
                list.add(root.tree.root.value);
                root.left.tree = new AssocTree(list);

                root.right = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                root.right.tree = new AssocTree(list);

                root.tree.insert_point(x, y);
            }
            return;
        }

        // 1. Find the place of the new point
        Node prev_node = null;
        Node curr_node = root;

        int left_right_flag = 0; // If flag = 0, the target node is on the left subtree, otherwise that is on the right subtree
        while (curr_node.node_num != 1) {
            if (x <= curr_node.value) {
                prev_node = curr_node;
                curr_node = curr_node.left;
                left_right_flag = 0;
            }
            else {
                prev_node = curr_node;
                curr_node = curr_node.right;
                left_right_flag = 1;
            }
        }

        if (curr_node.value == x || curr_node.tree.root.value.y == y) return;

        is_inserted = true;
        if (left_right_flag == 0) {
            Node target_node = curr_node;
            curr_node = root;

            while (curr_node != target_node) {
                if (x <= curr_node.value) {
                    // Insert the new point into the ASSOC tree
                    curr_node.tree.insert_point(x, y);
                    curr_node.node_num++;
                    curr_node = curr_node.left;
                } else {
                    // Insert the new point into the ASSOC tree
                    curr_node.tree.insert_point(x, y);
                    curr_node.node_num++;
                    curr_node = curr_node.right;
                }
            }
            Point neighbour_point = curr_node.tree.root.value;
            curr_node.node_num = 2;
            curr_node.value = x;

            if (neighbour_point.x < x) {
                curr_node.left = new Node(neighbour_point.x, 1);
                list = new ArrayList<>();
                list.add(neighbour_point);
                curr_node.left.tree = new AssocTree(list);

                curr_node.right = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                curr_node.right.tree = new AssocTree(list);
            }
            else {
                curr_node.left = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                curr_node.left.tree = new AssocTree(list);

                curr_node.right = new Node(neighbour_point.x, 1);
                list = new ArrayList<>();
                list.add(neighbour_point);
                curr_node.right.tree = new AssocTree(list);
            }
            curr_node.tree.insert_point(x, y);
        }

        else {
            Node target_node = curr_node;
            curr_node = root;
            int rightmost_flag = 1;

            while (curr_node != target_node) {
                if (x <= curr_node.value) {
                    if (curr_node.value == target_node.value) {
                        rightmost_flag = 0;
                    }
                    // Insert the new point into the ASSOC tree
                    curr_node.tree.insert_point(x, y);
                    curr_node.node_num++;
                    curr_node = curr_node.left;
                } else {
                    // Insert the new point into the ASSOC tree
                    curr_node.tree.insert_point(x, y);
                    curr_node.node_num++;
                    curr_node = curr_node.right;
                }
            }

            Point neighbour_point = curr_node.tree.root.value;
            curr_node.node_num = 2;
            if (rightmost_flag == 1 && x > target_node.value) curr_node.value = target_node.value;
            else curr_node.value = x;

            if (neighbour_point.x < x) {
                curr_node.left = new Node(neighbour_point.x, 1);
                list = new ArrayList<>();
                list.add(neighbour_point);
                curr_node.left.tree = new AssocTree(list);

                curr_node.right = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                curr_node.right.tree = new AssocTree(list);
            }
            else {
                curr_node.left = new Node(x, 1);
                list = new ArrayList<>();
                list.add(new Point(x, y));
                curr_node.left.tree = new AssocTree(list);

                curr_node.right = new Node(neighbour_point.x, 1);
                list = new ArrayList<>();
                list.add(neighbour_point);
                curr_node.right.tree = new AssocTree(list);
            }
            curr_node.tree.insert_point(x, y);
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

        int[] dataset_size = {1500, 100, 200, 500, 1000, 2000};
        int[] iteration_num = {150, 10, 20, 50, 100, 150};

        for (int i = 0; i < dataset_size.length; i++) {
            int n = dataset_size[i];
            if (n != 1500) {
                System.out.println("======================> Dataset size: " + n + " <======================");
            }

            // Prepare the dataset
            int size = n;
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

            //Range counting
            for (int j = 0; j < count; j++) {
                int x_1 = PG.getRange_1();
                int x_2 = PG.getRange_2(x_1);
                int y_1 = PG.getRange_1();
                int y_2 = PG.getRange_2(y_1);


                long start = System.nanoTime();
                tree.range_query(x_1, x_2, y_1, y_2);

                long end = System.nanoTime();
                long duration = end - start;
                double ns = duration;
                total_time += ns;
                points_num += tree.result_num;

                if (ns > max_time) max_time = ns;
                if (j == 0) min_time = ns;
                else if (ns < min_time) min_time = ns;
            }

            if (n != 1500) {
                System.out.println("===> Range query:");
                System.out.println("Average search time: " + total_time / count);
                System.out.println("Maximum search time: " + max_time);
                System.out.println("Minimum search time: " + min_time);
                System.out.println("Average number of points: " + points_num / count);
            }

            // Deletion
            total_time = 0;
            max_time = -1;
            min_time = 0;

            for (int j = 0; j < count; j++) {
                tree = new BST(list_sorted_x);
                long duration = 0;

                for (int k = 0; k < iteration_num[i]; k++) {
                    Random rand = new Random();
                    int pos = rand.nextInt(n);

                    int x = list_sorted_x.get(pos).x;
                    int y = list_sorted_x.get(pos).y;

                    long start = System.nanoTime();
                    tree.delete_point(x, y);

                    long end = System.nanoTime();
                    duration += end - start;
                }

                double ns = duration / iteration_num[i];
                total_time += ns;

                if (ns > max_time) max_time = ns;
                if (j == 0) min_time = ns;
                else if (ns < min_time) min_time = ns;
            }

            if (n != 1500) {
                System.out.println("===> Deletion:");
                System.out.println("Average search time: " + total_time / count);
                System.out.println("Maximum search time: " + max_time);
                System.out.println("Minimum search time: " + min_time);
            }

            // Insertion
            total_time = 0;
            max_time = -1;
            min_time = 0;

            for (int j = 0; j < count; j++) {
                tree = new BST(list_sorted_x);
                long duration = 0;

                for (int k = 0; k < iteration_num[i]; k++) {
                    int x = PG.getRange_1();
                    int y = PG.getRange_1();

                    long start = System.nanoTime();
                    tree.insert_point(x, y);

                    long end = System.nanoTime();
                    duration += end - start;
                }

                double ns = duration / iteration_num[i];
                total_time += ns;

                if (ns > max_time) max_time = ns;
                if (j == 0) min_time = ns;
                else if (ns < min_time) min_time = ns;
            }

            if (n != 1500) {
                System.out.println("===> Insertion:");
                System.out.println("Average search time: " + total_time / count);
                System.out.println("Maximum search time: " + max_time);
                System.out.println("Minimum search time: " + min_time);
            }
        }
    }
}
