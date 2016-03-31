package root;

public class Pair {
    int a;
    int b;

    public Pair(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        Pair p = (Pair) obj;
        return (this.a == p.a && this.b == p.b) || (this.a == p.b && this.b == p.a);
    }

    @Override
    public int hashCode() {
        return this.a + this.b;
    }
}
