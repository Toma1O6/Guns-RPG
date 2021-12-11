package dev.toma.gunsrpg.util.math;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public final class TreeDimensions<T> implements IDimensions {

    private final T root;
    private final Node<T> rootNode;
    private final int height, width;

    public TreeDimensions(T root, Function<T, T[]> childrenGetter) {
        this.root = root;
        this.rootNode = new Node<>(root, childrenGetter);
        this.height = rootNode.getDepth();
        this.width = rootNode.getWidth();
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    private static class Node<T> {

        private final int depth;
        private final Node<T>[] children;

        public Node(T root, Function<T, T[]> childrenGetter) {
            this(root, childrenGetter, 1);
        }

        @SuppressWarnings("unchecked")
        public Node(T root, Function<T, T[]> childrenGetter, int depth) {
            this.depth = depth;

            T[] children = childrenGetter.apply(root);
            if (children != null && children.length > 0) {
                this.children = Arrays.stream(childrenGetter.apply(root)).map(t -> new Node<>(t, childrenGetter, depth + 1)).toArray(Node[]::new);
            } else {
                this.children = new Node[0];
            }
        }

        public int getWidth() {
            int[] data = new int[getDepth()];
            incrementWidthAt(data, this);
            return Arrays.stream(data).max().orElse(0);
        }

        public int getDepth() {
            int max = depth;
            for (Node<T> node : children) {
                int nodeDepth = node.getDepth();
                if (nodeDepth > max) {
                    max = nodeDepth;
                }
            }
            return max;
        }

        private void incrementWidthAt(int[] data, Node<T> node) {
            data[node.depth - 1]++;
            for (Node<T> child : node.children) {
                incrementWidthAt(data, child);
            }
        }
    }
}
