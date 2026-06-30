#include <iostream>
#include <algorithm> // C++ library sort

#include "bubblesort.h"
#include "shellsort.h"
#include "quicksort.h"
#include "mergesort.h"
#include "selectionsort.h"

const int ITERATIONS = 100000;
const int MAX_SIZE = 10;

using namespace std;


int main() {    
    int values[MAX_SIZE];        
    int size = 0;

    cout << "Demo with " << size << " elements:" << endl;
    cout << "Enter array size: ";
    cin >> size;

    int before = time(0);
    srand(before);    
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        quickSort(values, size);
    }
    int after = time(0);
    cout << "Elapsed time with quick sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;    

    srand(0); // Make sure we get the same arrays again
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        selectionSort(values, size);
    }
    after = time(0);
    cout << "Elapsed time with selection sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;

    srand(0); // Make sure we get the same arrays again
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        shellSort(values, size);
    }
    after = time(0);
    cout << "Elapsed time with shell sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;

    srand(before); // Make sure we get the same arrays again
    before = time(0);
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        sort(values, values + size);
    }
    after = time(0);
    cout << "Elapsed time with the library sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;

    srand(0); // Make sure we get the same arrays again
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        insertionSort(values, size, 0, 1);
    }
    after = time(0);
    cout << "Elapsed time with insertion sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;

    srand(0); // Make sure we get the same arrays again
    for (int i = 1; i <= ITERATIONS; i++) {
        random_fill(values, size);
        bubbleSort(values, size);
    }
    after = time(0);
    cout << "Elapsed time with bubble sort: "
        << (after - before) * 1.0 / ITERATIONS << " seconds" << endl;

    const int ITERATIONS2 = ITERATIONS;
    before = time(0);
    for (int i = 1; i <= ITERATIONS2; i++) {
        random_fill(values, size);
        mergeSort(values, 0, size);
    }
    after = time(0);
    cout << "Elapsed time with merge sort: " << after - before
        << (after - before) * 1.0 / ITERATIONS2 << " seconds" << endl;

    return 0;
}