#include <iostream>
#include <iomanip>
#include "SortingOrder.h"

using namespace std;

void testSorting() 
{
	int choice;	
	do {
		cout << "\n\n******** TEST SORTING ********\n\n";
		cout << setw(5) << "1." << " Bubble Sort\n";
		cout << setw(5) << "2." << " Selection Sort\n";
		cout << setw(5) << "3." << " Quick Sort\n";
		cout << setw(5) << "-1." << " Exit\n\n";
		cout << "Your choice, please: (1-3)  ";
		cin >> choice;

		// Validate the menu selection.
		while (choice < -1 || choice > 3) {
			cout << "Enter a number from 1 through 3 or -1 to exit: ";
			cin >> choice;
		}

		switch (choice) {
		case 1:
			testBubbleSort();
			break;
		case 2:
			testSelectionSort();
			break;
		case 3:
			testQuickSort();
			break;
		default:
			break;
		}

	} while (choice != -1);
}

//**********************************************
// swap simply exchanges the contents of       *
// value1 and value2.                          *
//**********************************************
void swap(int &value1, int &value2)
{
	int temp = value1;
	value1 = value2;
	value2 = temp;
}

// ********************************************************
// The showArray function displays the contents of the    *
// array. The size parameter is the number of elements.   *
// ********************************************************
void showArray(int values[], int size)
{
	for (int count = 0; count < size; count++) {
		cout << values[count] << " ";
	}
	cout << endl;
}