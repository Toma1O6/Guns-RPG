package dev.toma.gunsrpg.util.object;

public class Pair<L, R> {

    private L left;
    private R right;

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public void setRight(R right) {
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    @SuppressWarnings("unchecked")
    public <T> Pair<L, T> castRight(Class<T> tClass) {
        return of(left, (T) right);
    }
}
