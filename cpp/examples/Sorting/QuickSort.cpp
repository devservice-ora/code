// This program demonstrates the QuickSort Algorithm.
#include <iostream>
#include "SortingOrder.h"

using namespace std;

// Function prototypes
void quickSort(int[], int, int);
int partition(int[], int, int);
void swapValue(int &, int &);

const int SIZE = 8;
int testarray[8] = {};
static int swapped = 1;
static int pass = 1;

void testQuickSort()
{
	int array[SIZE] = { 105, 102, 107, 103, 106, 100, 104, 101 };
	memcpy(testarray, array, SIZE * sizeof(int));

	// Display the array contents.
	showArray(testarray, SIZE);

	cout << "Now performing the quick sort\n";
	cout << "---------------------------------\n";
	swapped = 1;
	// Sort the array.
	quickSort(testarray, 0, SIZE - 1);

	// Display the array contents.
	showArray(testarray, SIZE);
}

//************************************************
// quickSort uses the quicksort algorithm to     *
// sort set, from set[start] through set[end].   *
//************************************************
void quickSort(int set[], int start, int end)
{
	int pivotPoint;

	if (start < end) {
		// Get the pivot point.
		pivotPoint = partition(set, start, end);
		// Sort the first sub list.
		quickSort(set, start, pivotPoint - 1);
		// Sort the second sub list.
		quickSort(set, pivotPoint + 1, end);
	}
}

//**********************************************************
// partition selects the value in the middle of the        *
// array set as the pivot. The list is rearranged so       *
// all the values less than the pivot are on its left      *
// and all the values greater than pivot are on its right. *
//**********************************************************
int partition(int set[], int start, int end)
{
	int pivotValue, pivotIndex, mid;

	swapped = 1; // test # of swapped

	mid = (start + end) / 2;
	swapValue(set[start], set[mid]);
	pivotIndex = start;
	pivotValue = set[start];
	for (int scan = start + 1; scan <= end; scan++) {
		if (set[scan] < pivotValue) {
			pivotIndex++;
			swapValue(set[pivotIndex], set[scan]);
		}
		// test
		cout << "After pass " << pass << ": ";
		showArray(testarray, SIZE);
		pass++;	
		// end-test
	}
	swapValue(set[start], set[pivotIndex]);	
	return pivotIndex;
}

//**********************************************
// swap simply exchanges the contents of       *
// value1 and value2.                          *
//**********************************************
void swapValue(int &value1, int &value2)
{
	swap(value1, value2);

	// test
	cout << "Swapped " << swapped << endl;
	swapped++;
	// end-test

}