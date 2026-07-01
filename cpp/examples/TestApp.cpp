// Test
#include <iostream>
#include <iomanip>
#include "Test.h"
#include "Sorting/SortingOrder.h"
//#include "Examples/examples.h"

using namespace std;

// Function prototype
void dispatch(int);

int main()
{
	int choice;				// To hold the user's menu choice
	do     // menu
	{
		cout << "\n\n******** TESTS MENU ********\n\n";
		cout << setw(5) << "1." << " Demo\n";
		cout << setw(5) << "2." << " Data Type\n";
		cout << setw(5) << "3." << " Arrays\n";
		cout << setw(5) << "4." << " Vector\n";
		cout << setw(5) << "5." << " Sorting\n";
		cout << setw(5) << "6." << " Struct\n";
		cout << setw(5) << "7." << " Union\n";
		cout << setw(5) << "8." << " Enum\n";
		cout << setw(5) << "9." << " Map\n";
		cout << setw(5) << "10." << " Class\n";
		cout << setw(5) << "11." << " Linked List\n";
		cout << setw(5) << "-1." << " Exit\n\n";
		cout << "Your choice, please: (1-11)  ";
		cin >> choice;

		// Validate the menu selection.
		while (choice < -1 || choice > 12)
		{
			cout << "Enter a number from 1 through 11 please: ";
			cin >> choice;
		}

		// Execute the user's choice.
		dispatch(choice);

	} while (choice != -1);

	return 0;
}

void dispatch(int choice)
{
	switch (choice)
	{
	case 1:
		// demo
		//testExamples();
		break;

	case 2:	
		// Test data type
		testDataType();
		break;

	case 3:
		// Call test Array
		testArray();
		break;

	case 4:	
		// Call test Vector
		testVector();
		break;

	case 5:	
		// Call test sorting
		testSorting();
		break;	
	case 6:
		// Call test Struct
		testStruct();
		break;
	case 7:
		// Call test Union
		testUnion();
		break;
	case 8:
		// Call test Enum
		testEnum();
		break;
	case 9:
		// Call test Map
		testMap();
		break;
	case 10:
		// Call test Class
		testClass();
		break;
	case 11:
		// Call linkedList test Class
		testLinkedList();
		break;
	default: 
		break;
	}
}
