package lib.toma.animations;

public final class ByteFlags {

    private byte flags;

    public ByteFlags() {}

    public void set(int pos) {
        flags |= 1 << pos;
    }

    public void clear(int pos) {
        flags &= ~(1 << pos);
    }

    public boolean get(int pos) {
        return (flags & pos) == pos;
    }

    public boolean isEmpty() {
        return flags == 0;
    }
}
