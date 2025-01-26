import time
import matplotlib.pyplot as plt
from random import randint


def arr_sum(arr):
    sum_element = 0
    for i in arr:
        sum_element += i
    return sum_element


def result(arr):
    for i in range(len(arr)):
        arr[i] = arr_sum(arr)
    return arr


def result_2(arr):
    last_element = arr[0]
    arr[0] = sum(arr)
    for i in range(1, len(arr)):
        arr[i], last_element = arr[i - 1] * 2 - last_element, arr[i]
    return arr


start = time.perf_counter()
print(result([1, 2, 3, 4]))
finish = time.perf_counter()

start_1 = time.perf_counter()
print(result_2([1, 2, 3, 4]))
finish_1 = time.perf_counter()

print(finish_1 - start_1)

print(finish-start)

test_time_1 = []
test_time_2 = []

for i in range(1, 100):
    arr = [randint(0,100) for i in range(10*i)]
    start = time.perf_counter()
    result(arr)
    finish = time.perf_counter()
    test_time_1.append(finish - start)
    start = time.perf_counter()
    result_2(arr)
    finish = time.perf_counter()
    test_time_2.append(finish - start)

x = [10*i for i in range(1, 100)]
#plt.plot(x, test_time_1)
plt.plot(x, test_time_2)
plt.show()




