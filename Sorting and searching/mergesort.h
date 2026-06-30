#pragma once

#include "common.h"

template <typename T>
void mergeSort(T a[], int from, int to);
template <typename T>
void merge(T a[], int from, int mid, int to);


/**
   Merges two adjacent ranges in an array.
   @param a the array with the elements to merge
   @param from the start of the first range
   @param mid the end of the first range
   @param to the end of the second range
*/
template <typename T>
void merge(T a[], int from, int mid, int to) {
    int n = to - from + 1; // Size of the range to be merged 
    // Merge both halves into a temporary array b 
    // We allocate the array dynamically since its size is only
    // known at run time--see Section 7.4
    T* b = new T[n];

    int i1 = from;
    // Next element to consider in the first half 
    int i2 = mid + 1;
    // Next element to consider in the second half 
    int j = 0; // Next open position in b 

    // As long as neither i1 nor i2 is past the end, move the smaller
    // element into b

    while (i1 <= mid && i2 <= to)  {
        if (a[i1] < a[i2]) {
            b[j] = a[i1];
            i1++;
        } else {
            b[j] = a[i2];
            i2++;
        }
        j++;
    }

    // Note that only one of the two while loops below is executed

    // Copy any remaining entries of the first half
    while (i1 <= mid) {
        b[j] = a[i1];
        i1++;
        j++;
    }
    // Copy any remaining entries of the second half
    while (i2 <= to) {
        b[j] = a[i2];
        i2++;
        j++;
    }

    // Copy back from the temporary array
    for (j = 0; j < n; j++) {
        a[from + j] = b[j];
    }

    // The temporary array is no longer needed.
    delete[] b;
}

/**
   Sorts the elements in a range of an array.
   @param a the array with the elements to sort
   @param from start of the range to sort
   @param to end of the range to sort
*/
template <typename T>
void mergeSort(T a[], int from, int to) {
    if (from == to) { return; }
    int mid = (from + to) / 2;
    // Sort the first and the second half
    mergeSort(a, from, mid);
    mergeSort(a, mid + 1, to);
    merge(a, from, mid, to);
}
