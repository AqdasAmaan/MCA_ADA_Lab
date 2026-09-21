import random
import timeit
import matplotlib.pyplot as plt

def bubbleSort(arr):
    n = len(arr)

    for i in range(n-1):
        swapped = False

        for j in range(n - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
                swapped = True

        if not swapped:
            break

    return arr

def bubbleSort2(arr):
    n = len(arr)

    for i in range(n-1):
        swapped = False

        for j in range(n - i - 1):
            if arr[j] > arr[j + 1]:
                temp = arr[j]
                arr[j] = arr[j + 1]
                arr[j + 1] = temp
                
                swapped = True

        if not swapped:
            break

    return arr

def selectionSort(arr):
    n = len(arr)
    for i in range(n):
        min_idx = i
        for j in range(i+1, n):
            if arr[j] < arr[min_idx]:
                min_idx = j
        arr[i], arr[min_idx] = arr[min_idx], arr[i]
    return arr

def generate_graph(bubble_times, bubble2_times, selection_times):

    sizes = [i * 50 for i in range(1, 11)]

    plt.plot(sizes, bubble_times, label='Bubble Sort', marker='o')
    plt.plot(sizes, bubble2_times, label='Bubble Sort 2', marker='o')
    plt.plot(sizes, selection_times, label='Selection Sort', marker='o')

    plt.xlabel('Array Size')
    plt.ylabel('Time (milliseconds)')
    plt.title('Bubble Sort vs Selection Sort Performance')
    plt.legend()
    plt.grid()
    plt.show()

def generateBarGraph(bubble_times, bubble2_times, selection_times):
    sizes = [i * 50 for i in range(1, 11)]
    x = range(len(sizes))

    plt.bar(x, bubble_times, width=0.4, label='Bubble Sort', align='center')
    plt.bar([p + 0.4 for p in x], bubble2_times, width=0.4, label='Bubble Sort 2', align='center')
    plt.bar([p + 0.8 for p in x], selection_times, width=0.4, label='Selection Sort', align='center')

    plt.xlabel('Array Size')
    plt.ylabel('Time (milliseconds)')
    plt.title('Bubble Sort vs Selection Sort Performance')
    plt.xticks([p + 0.2 for p in x], sizes)
    plt.legend()
    plt.grid()
    plt.show()

def main():
    bubble_times = []
    bubble2_times = []
    selection_times = []
    bubble = 0
    selection = 0
    for i in range(1, 11):
        n = i*50 
        arr = [random.randint(1, 1000) for _ in range(n)]

        bubble_sort_time = timeit.timeit(lambda: bubbleSort(arr.copy()), number=1) * 1000
        bubble_sort_time2 = timeit.timeit(lambda: bubbleSort2(arr.copy()), number=1) * 1000
        selection_sort_time = timeit.timeit(lambda: selectionSort(arr.copy()), number=1) * 1000

        bubble_times.append(bubble_sort_time)
        bubble2_times.append(bubble_sort_time2)
        selection_times.append(selection_sort_time)

        if selection_sort_time < bubble_sort_time:
            selection += 1
        elif bubble_sort_time < selection_sort_time:
            bubble += 1
        
        # print(f"Array size: {n}, Bubble Sort Time: {bubble_sort_time*1000000:.4f}ms, Selection Sort Time: {selection_sort_time*1000000:.4f}ms")

    generate_graph(bubble_times, bubble2_times, selection_times)
    generateBarGraph(bubble_times, bubble2_times, selection_times)
    print(f"\nBubble Sort was faster {bubble} times.")
    print(f"Selection Sort was faster {selection} times.")

if __name__ == "__main__":
    main()
