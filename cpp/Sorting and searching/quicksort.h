#pragma once
#include "common.h"

// QuickSort 
template <typename T>
void quickSort(T list[], int length);
template <typename T>
int partition(T list[], int first, int last);
template <typename T>
void recQuickSort(T list[], int first, int last);

template <typename T>
void quickSort(T list[], int length) {
    recQuickSort(list, 0, length - 1);
} //end quickSort

template <typename T>
int partition(T list[], int first, int last) {
   
    T pivot;

    int index, smallIndex;

    int mid = (first + last) / 2;

    swap(list, first, mid);

    pivot = list[first];
    smallIndex = first;

    for (index = first + 1; index <= last; index++)
        if (list[index] < pivot)
        {
            smallIndex++;
            swap(list, smallIndex, index);
        }

    swap(list, first, smallIndex);

    return smallIndex;
} //end partition

template <typename T>
void recQuickSort(T list[], int first, int last) {
    int pivotLocation;

    if (first < last) {
        pivotLocation = partition(list, first, last);
        recQuickSort(list, first, pivotLocation - 1);
        recQuickSort(list, pivotLocation + 1, last);
    }
} //end recQuickSort