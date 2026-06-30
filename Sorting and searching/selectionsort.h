#pragma once
#include "common.h"

template <typename T>
int minLocation(T list[], int first, int last);
template <typename T>
void selectionSort(T list[], int length);

template <typename T>
void selectionSort(T list[], int length)
{
    int loc, minIndex;

    for (loc = 0; loc < length; loc++)
    {
        minIndex = minLocation(list, loc, length - 1);
        swap(list, loc, minIndex);
    }
} //end selectionSort

template <typename T>
int minLocation(T list[], int first, int last)
{
    int loc, minIndex;
    minIndex = first;

    for (loc = first + 1; loc <= last; loc++) {
        if (list[minIndex] > list[loc]) {
            minIndex = loc;
        }
    }

    return minIndex;
} //end minLocation