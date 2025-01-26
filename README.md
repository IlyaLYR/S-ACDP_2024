**Задание:** Написать программу, решающую указанную в варианте задачу. Провести теоретический и экспериментальный анализ сложности полученного алгоритма. Для практического анализа провести серию экспериментов на различных размерах входных данных (получаемых случайным образом), от меньших к большим. Например, для массива размером 10000, 20000, 30000, 40000, 50000 элементов. Важно, чтобы между значениями сохранялся фиксированный интервал. Для каждого размера входных данных зафиксировать время выполнения алгоритма (для точности  - в процессорных тиках). Рекомендуется проведение нескольких экспериментов для каждого размера входных данных с последующим усреднением. Результаты экспериментов представить в виде графика зависимости времени выполнения алгоритма от размера входных данных.

**Доп. вопрос:** Подумать, существует ли алгоритм решения задачи с меньшей оценкой временной сложности, чем полученный.

***Варианты:***
1) Удалить из массива все отрицательные значения, а оставшиеся уплотнить (сдвинуть) с сохранением исходного порядка к началу массива.
2) Каждый элемент a[i] массива заменить на сумму элементов исходного массива вплоть до него самого включительно, т.е. от 0 до i-го.
3) Назовем x-отрезком группу подряд идущих элементов массива, каждый из которых равен x. Для заданного числа x заменить элементы каждого x-отрезка на полусумму элементов, прилегающих к этому отрезку справа и слева. Если x-отрезок расположен в начале или конце массива, считать второй крайний элемент равным нулю.
4) Сгруппировать положительные элементы массива в его начале, а отрицательные — в конце с сохранением их порядка.
5) Назовем массив из N целых чисел счастливым, если существует такое 0 < k < N, что сумма элементов с индексами от 0 до k−1 совпадает с суммой элементов с индексами от k до N −1. Определить является ли данный массив счастливым.
6) Назовем массив из целых чисел плотным, если множество значений элементов массива полностью заполняет некоторый отрезок [a, b] (рассматриваются целые значения). Определить является ли данный массив плотным.
7) Даны два неубывающих массива. Построить третий неубывающий массив, который является объединением первых двух (элементы могут повторяться).
8) Выполнить следующее преобразование. Элементы с четными индексами сгруппировать в начале массива с сохранением их исходного порядка относительно друг друга, а элементы с нечетными индексами сгруппировать в конце массива также с сохранением их исходного порядка.
9) Выполнить следующее преобразование массива длины N. Элементы с индексами i ≤ [(N + 1)/2] переместить на позиции с четными индексами с сохранением их исходного порядка относительно друг друга, а оставшиеся элементы (i > [(N + 1)/2]) разместить на позициях с нечетными индексами также с сохранением их исходного порядка. Т.е. начальная и конечная половины массива “перемешиваются” чередованием элементов.
10) Сократить подряд идущие одинаковые элементы целочисленного массива до одного элемента. То есть, если в массиве встречается несколько одинаковых элементов, стоящих рядом, то оставить только один из них, а остальные удалить из массива. Оставшиеся элементы сдвинуть к началу массива.
11) Удалить из целочисленного массива одинаковые значения, т.е. если какое-то значение встречается несколько раз (в разных местах массива), то оставить только первый такой элемент, а остальные удалить из массива. Оставшиеся элементы сдвинуть к началу массива.
12) Определить какое число встречается в массиве целых чисел наибольшее количество раз.
