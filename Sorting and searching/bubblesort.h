#pragma once
#include "common.h"

template <typename T>
void bubbleSort(T list[], int length) {    
    for (int iteration = 1; iteration < length; iteration++) {
        for (int index = 0; index < length - iteration; index++) {            
            if (list[index] > list[index + 1]) {
                swap(list, index, index + 1);
            }
        }
    }
} //end bubbleSort