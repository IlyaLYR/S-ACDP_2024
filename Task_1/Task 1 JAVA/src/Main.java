import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] arr = new int[]{3,2,1,0,-1};
        System.out.println(Arrays.toString(ArrResult(arr)));
        int[] arr2 = new int[]{3,2,1,0,-1};
        System.out.println(Arrays.toString(ArrResult2(arr2)));
    }

    /**
     * Нахождение суммы массива из N элементов
     * @param arr исходный массив из натуральных чисел
     * @return Сумма массива
     */
    public static int ArrSum(int[] arr) {
        int sum = 0;
        for (int i : arr) {
            sum += i;
        }
        return sum;
    }

    /**
     * Task 1
     * @param arr массив из натуральных чисел
     * @return Результат к Task 1
     */
    public static int[] ArrResult(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ArrSum(arr);
        }
        return arr;
    }

    /**
     * Task 1 Вторая реализация Сложность N
     * @param arr массив
     * @return решение задачи
     */
    public static int[] ArrResult2(int[] arr) {
        int [] LastElements = new int[2];
        LastElements[0] = arr[0];
        arr[0] = ArrSum(arr);
        for (int i = 1; i < arr.length; i++) {
            LastElements[1] = arr[i];
            arr[i] = arr[i - 1] * 2 - LastElements[0];
            LastElements[0] = LastElements[1];
        }
        return arr;
    }
}