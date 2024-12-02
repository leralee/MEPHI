package myutils;

import java.util.Comparator;
import java.util.List;

/**
 * @author valeriali
 * @project MEPHI
 */
public class MyCollections {

    // ----------------------- Методы для работы с Comparable -----------------------

    public static <T extends Comparable<? super T>> int binarySearch(List<T> list, T key) {
        if (list == null || key == null) {
            throw new NullPointerException("List or key must not be null.");
        }
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            T midVal = list.get(mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    // ----------------------- Методы для работы с Comparator -----------------------

    public static <T> int binarySearch(List<T> list, T key, Comparator<? super T> c) {
        if (list == null || key == null || c == null) {
            throw new NullPointerException("List, key, or comparator must not be null.");
        }
        int low = 0;
        int high = list.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            T midVal = list.get(mid);
            int cmp = c.compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }

        return -1;
    }
}
