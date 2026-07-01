#include <iostream>
#include "SortingOrder.h"

using namespace std;

void testBubbleSort()
{
	const int SIZE = 8;
	int testarray[SIZE] = { 105, 102, 107, 103, 106, 100, 104, 101 };

	// Display the array contents.
	showArray(testarray, SIZE);

	// Sort array1 using Bubble Sort
	bubbleSort(testarray, SIZE);
}

// ********************************************************
// The bubbleSort function performs an ascending order    *
// bubble sort on the array. The size parameter is the    *
// number of elements in the array. The function has been *
// modified to print the contents of the array after each *
// pass.                                                  *
// ********************************************************
void bubbleSort(int values[], int size)
{
	bool swapped;
	int temp;
	int pass = 1;

	cout << "Now performing the bubble sort\n";
	cout << "------------------------------\n";

	do {
		swapped = false;
		for (int count = 0; count < (size - 1); count++) {
			if (values[count] > values[count + 1]) {
				swap(values[count], values[count + 1]);
				swapped = true;
			}

			// Display the array contents after this pass.
			cout << "After pass " << pass << ": ";
			showArray(values, size);
			pass++;
		}
	} while (swapped);
}