package lib.toma.animations;

import java.util.Comparator;

public class QuickSort {

    public static <T> void sort(T[] array, Comparator<T> comparator) {
        sort(array, 0, array.length - 1, comparator);
    }

    private static <T> void sort(T[] array, int min, int max, Comparator<T> comparator) {
        if (min < max) {
            int index = partition(array, min, max, comparator);
            sort(array, min, index - 1, comparator);
            sort(array, index + 1, max, comparator);
        }
    }

    private static <T> int partition(T[] array, int min, int max, Comparator<T> comparator) {
        T p = array[max];
        int i = (min - 1);
        for (int j = min; j < max; j++) {
            T t = array[j];
            if (comparator.compare(p, t) < 0) {
                ++i;
                T temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        T temp = array[i + 1];
        array[i+1] = array[max];
        array[max] = temp;
        return i + 1;
    }
}
