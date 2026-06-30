#pragma once
#include <iostream>

template <typename T>
void swap(T list[], int first, int second) {
    T temp;
    temp = list[first];
    list[first] = list[second];
    list[second] = temp;
} //end swap

/**
   Prints all elements in an array.
   @param a the array to print
   @param size the number of elements in a
*/
template <typename T>
void print(T a[], int size) {
    for (int i = 0; i < size; i++) {
        std::cout << a[i] << " ";
    }
    std::cout << std::endl;
}

/**
   Fills an array with random integers between 0 and 99.
   @param a the array to print
   @param size the number of elements in a
*/
void random_fill(int a[], int size) {
    //srand(time(0));
    for (int i = 0; i < size; i++) {
        a[i] = rand() % 100;
    }
}