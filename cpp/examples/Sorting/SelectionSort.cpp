#include <iostream>
#include "SortingOrder.h"

using namespace std;

void testSelectionSort()
{
	const int SIZE = 8;
	int array2[SIZE] = { 105, 102, 107, 103, 106, 100, 104, 101 };
	// Sort array2 using Selection Sort
	selectionSort(array2, SIZE);
}

// ********************************************************
// The selectionSort function performs an ascending order *
// selection sort on the array. The size parameter is the *
// number of elements in the array. The function has been *
// modified to print the contents of the array after each *
// pass.                                                  *
// ********************************************************
void selectionSort(int values[], int size)
{
	int startScan, minIndex, minValue;
	int pass = 1;

	cout << "Now performing the selection sort\n";
	cout << "---------------------------------\n";

	for (startScan = 0; startScan < (size - 1); startScan++) {
		minIndex = startScan;
		minValue = values[startScan];

		for (int index = startScan + 1; index < size; index++)
		{
			if (values[index] < minValue)
			{
				minValue = values[index];
				minIndex = index;
			}
		}

		values[minIndex] = values[startScan];
		values[startScan] = minValue;

		// Display the array contents after this pass.
		cout << "After pass " << pass << ": ";
		showArray(values, size);
		pass++;
	}
}