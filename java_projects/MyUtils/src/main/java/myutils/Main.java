package myutils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author valeriali
 * @project MEPHI
 */
public class Main {
    public static void main(String[] args) {
        // ----------to_index не включительно---------------
        // int[]
        int[] intArray = {1, 3, 5, 7, 9};
        System.out.println(MyArrays.binarySearch(intArray, 5)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(intArray, 2)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(intArray, 0, 3, 7)); // Ожидается: -1

        // long[]
        long[] longArray = {100L, 200L, 300L, 400L, 500L};
        System.out.println(MyArrays.binarySearch(longArray, 300L)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(longArray, 600L)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(longArray, 1, 4, 400L)); // Ожидается: 3

        // double[]
        double[] doubleArray = {1.1, 2.2, 3.3, 4.4, 5.5};
        System.out.println(MyArrays.binarySearch(doubleArray, 3.3)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(doubleArray, 6.6)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(doubleArray, 1, 4, 4.4)); // Ожидается: 3

        // byte[]
        byte[] byteArray = {10, 20, 30, 40, 50};
        System.out.println(MyArrays.binarySearch(byteArray, (byte) 30)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(byteArray, (byte) 25)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(byteArray, 0, 3, (byte) 20)); // Ожидается: 1

        // char[]
        char[] charArray = {'a', 'b', 'c', 'd', 'e'};
        System.out.println(MyArrays.binarySearch(charArray, 'c')); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(charArray, 'f')); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(charArray, 0, 4, 'd')); // Ожидается: 3

        // float[]
        float[] floatArray = {1.1f, 2.2f, 3.3f, 4.4f, 5.5f};
        System.out.println(MyArrays.binarySearch(floatArray, 3.3f)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(floatArray, 6.6f)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(floatArray, 0, 4, 2.2f)); // Ожидается: 1

        // short[]
        short[] shortArray = {100, 200, 300, 400, 500};
        System.out.println(MyArrays.binarySearch(shortArray, (short) 300)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(shortArray, (short) 250)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(shortArray, 0, 3, (short) 400)); // Ожидается: 3

        // Object[] с Comparator
        String[] stringArray = {"apple", "banana", "cherry", "mango"};
        Comparator<String> comparator = String::compareTo;
        System.out.println(MyArrays.binarySearch(stringArray, "cherry", comparator)); // Ожидается: 2
        System.out.println(MyArrays.binarySearch(stringArray, "grape", comparator)); // Ожидается: -1
        System.out.println(MyArrays.binarySearch(stringArray, 1, 3, "banana", comparator)); // Ожидается: 1

//        // ----------------------- Тестируем MyCollections -----------------------

        List<String> stringList = Arrays.asList("apple", "banana", "cherry", "mango");
        System.out.println(MyCollections.binarySearch(stringList, "banana")); // Ожидается: 1
        System.out.println(MyCollections.binarySearch(stringList, "grape")); // Ожидается: -1

        // List с Comparator
        System.out.println(MyCollections.binarySearch(stringList, "cherry", comparator)); // Ожидается: 2
        System.out.println(MyCollections.binarySearch(stringList, "apple", comparator)); // Ожидается: 0
        System.out.println(MyCollections.binarySearch(stringList, "fig", comparator)); // Ожидается: -1

        // Тесты для List с объектами и кастомным компаратором
        List<Person> people = Arrays.asList(
                new Person("Alice", 30),
                new Person("Bob", 25),
                new Person("Charlie", 35)
        );

        Comparator<Person> personByName = (p1, p2) -> p1.getName().compareTo(p2.getName());
        System.out.println(MyCollections.binarySearch(people, new Person("Bob", 0), personByName)); // Ожидается: 1
        System.out.println(MyCollections.binarySearch(people, new Person("David", 0), personByName)); // Ожидается: -1
    }
}

// Дополнительный класс для тестов
class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
