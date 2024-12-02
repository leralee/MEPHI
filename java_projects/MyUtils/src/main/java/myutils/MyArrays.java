package myutils;

import java.util.Comparator;

/**
 * @author valeriali
 * @project MEPHI
 */
public class MyArrays {

    // ----------------------- Общий метод для бинарного поиска -----------------------

    private static <T> int binarySearchGeneric(T[] array, int fromIndex, int toIndex, T key, Comparator<? super T> comparator) {
        int low = fromIndex;
        int high = toIndex - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            T midVal = array[mid];
            int cmp = comparator.compare(midVal, key);

            if (cmp < 0) {
                low = mid + 1; // Ищем в правой половине
            } else if (cmp > 0) {
                high = mid - 1; // Ищем в левой половине
            } else {
                return mid; // Элемент найден
            }
        }

        return -1; // Элемент не найден
    }

    // ----------------------- Примитивные типы: методы для бинарного поиска -----------------------

    public static int binarySearch(int[] array, int key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(int[] array, int fromIndex, int toIndex, int key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Integer::compare);
    }

    public static int binarySearch(long[] array, long key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(long[] array, int fromIndex, int toIndex, long key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Long::compare);
    }

    public static int binarySearch(double[] array, double key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(double[] array, int fromIndex, int toIndex, double key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Double::compare);
    }

    public static int binarySearch(float[] array, float key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(float[] array, int fromIndex, int toIndex, float key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Float::compare);
    }

    public static int binarySearch(char[] array, char key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(char[] array, int fromIndex, int toIndex, char key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Character::compare);
    }

    public static int binarySearch(byte[] array, byte key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(byte[] array, int fromIndex, int toIndex, byte key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Byte::compare);
    }

    public static int binarySearch(short[] array, short key) {
        return binarySearch(array, 0, array.length, key);
    }

    public static int binarySearch(short[] array, int fromIndex, int toIndex, short key) {
        return binarySearchGeneric(wrap(array), fromIndex, toIndex, key, Short::compare);
    }

    public static <T> int binarySearch(T[] array, T key, Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("Comparator cannot be null");
        }
        return binarySearchGeneric(array, 0, array.length, key, comparator);
    }

    public static <T> int binarySearch(T[] array, int fromIndex, int toIndex, T key, Comparator<? super T> comparator) {
        if (comparator == null) {
            throw new NullPointerException("Comparator cannot be null");
        }
        return binarySearchGeneric(array, fromIndex, toIndex, key, comparator);
    }

    // ----------------------- Вспомогательные методы -----------------------

    private static Integer[] wrap(int[] array) {
        Integer[] wrapped = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Long[] wrap(long[] array) {
        Long[] wrapped = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Double[] wrap(double[] array) {
        Double[] wrapped = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Float[] wrap(float[] array) {
        Float[] wrapped = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Character[] wrap(char[] array) {
        Character[] wrapped = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Byte[] wrap(byte[] array) {
        Byte[] wrapped = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }

    private static Short[] wrap(short[] array) {
        Short[] wrapped = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            wrapped[i] = array[i];
        }
        return wrapped;
    }
}