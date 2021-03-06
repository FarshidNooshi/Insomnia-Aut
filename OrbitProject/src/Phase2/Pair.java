package Phase2;

/**
 * this class represents a generic pair class like C++
 *
 * @param <E> is the first element of the pair
 * @param <V> is the second element of the pair
 */
public class Pair<E, V> {
    private E first;
    private V second;

    public Pair(E e, V v) {
        first = e;
        second = v;
    }

    public E getFirst() {
        return first;
    }

    public void setFirst(E e) {
        first = e;
    }

    public V getSecond() {
        return second;
    }

    public void setSecond(V v) {
        second = v;
    }
}
