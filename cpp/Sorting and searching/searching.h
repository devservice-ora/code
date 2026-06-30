#pragma once

template <typename T>
int binarySearch(const T list[], int length, const T& item)
{
    int first = 0;
    int last = length - 1;
    int mid;

    bool found = false;

    while (first <= last && !found)
    {
        mid = (first + last) / 2;

        if (list[mid] == item)
            found = true;
        else if (list[mid] > item)
            last = mid - 1;
        else
            first = mid + 1;
    }

    if (found)
        return mid;
    else
        return -1;
} //end binarySearch

template <typename T>
int seqSearch(const T list[], int length, const T& item) {
    for (int i = 0; i < length; i++) {
        if (list[i] == item) {
            return i;
        }
    }

    return -1;
}