#include <iostream>
#include <string>
#include "Test.h"

using namespace std;

void testArray()
{
	char test1d[] = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
	char test2d[2][10] = {
			{ '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' },
			{ '0', '9', '8', '7', '6', '5', '4', '3', '2', '1' } };

	// Using regular loop
	cout << "1 Dimentional array - Using regular for loop" << endl;
	cout << "============================================" << endl;
	for (int i = 0; i < 10; i++) {
		cout << "test1d[" << i << "]=" << test1d[i] << endl;
	}

	// Using Range-based loop
	cout << "1 Dimentional array - Using Range-based for loop" << endl;
	cout << "============================================" << endl;
	for (char c : test1d) {
		cout << "char" << "=" << c << "; ascii code=" << (int)c << endl;
	}

	// 2D demo
	cout << "2 Dimentional array test" << endl;
	cout << "========================" << endl;
	for (int x = 0; x < 2; x++) {
		for (int y = 0; y < 10; y++) {
			cout << "(" << x << "," << y << ")=" << test2d[x][y] << " ";
			if (y == 9) {
				cout << endl;
			}
		}
	}
}