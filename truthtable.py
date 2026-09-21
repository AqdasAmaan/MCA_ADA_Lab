import timeit
import matplotlib.pyplot as plt
import numpy as np
import threading

def truthtable(n:int):
    if n < 1:
        return []

    num_rows = 2 ** n
    table = []

    for i in range(num_rows):
        row = [(i >> j) & 1 for j in range(n - 1, -1, -1)]
        table.append(row)

    return table

def d2b(n:int):
    if n < 0:
        return []

    binary_representation = []
    while n > 0:
        binary_representation.append(n % 2)
        n //= 2

    binary_representation.reverse()
    return binary_representation

def truth_table_using_d2b(n:int):
    if n < 1:
        return []

    num_rows = 2 ** n
    table = []

    for i in range(num_rows):
        row = d2b(i)
        row = [0] * (n - len(row)) + row
        table.append(row)

    return table

def main():
    f1_times = []
    f2_times = []

    for i in range(1, 11):
        time_f1 = timeit.timeit(lambda: truthtable(i), number=1)
        time_f2 = timeit.timeit(lambda: truth_table_using_d2b(i), number=1)
        f1_times.append(time_f1 * 1000)  
        f2_times.append(time_f2 * 1000)  

    generate_bar_graph(f1_times, f2_times)
    generate_line_graph(f1_times, f2_times)

def generate_bar_graph(f1_times, f2_times):

    x = np.arange(1, 11)
    width = 0.35

    fig, ax = plt.subplots()
    rects1 = ax.bar(x - width/2, f1_times, width, label='truthtable')
    rects2 = ax.bar(x + width/2, f2_times, width, label='truth_table_using_d2b')

    ax.set_xlabel('Number of Variables (n)')
    ax.set_ylabel('Execution Time (milliseconds)')
    ax.set_title('Execution Time Comparison of Truth Table Functions')
    ax.set_xticks(x)
    ax.legend()

    plt.show()

def generate_line_graph(f1_times, f2_times):
    x = np.arange(1, 11)

    plt.plot(x, f1_times, marker='o', label='truthtable')
    plt.plot(x, f2_times, marker='o', label='truth_table_using_d2b')

    plt.xlabel('Number of Variables (n)')
    plt.ylabel('Execution Time (milliseconds)')
    plt.title('Execution Time Comparison of Truth Table Functions')
    plt.xticks(x)
    plt.legend()
    plt.grid()

    plt.show()

if __name__ == "__main__":
    main()
