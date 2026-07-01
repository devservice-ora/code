#include "Test.h"
#include <vector>
#include <iostream>

using namespace std;

void testVector()
{
	vector<char> scores;

	// 10 integers
	for (int i = 0; i < 10; i++)
		scores.push_back((char) i + 101);
	
	cout << "Vector - Using regular for loop" << endl;
	cout << "=============================" << endl;
	for (int i = 0; i < (int) scores.size(); i ++) {
		cout << "char=" << scores[i] << endl;
	}

	cout << "Vector - Using Range for loop" << endl;
	cout << "=============================" << endl;
	for (char c : scores) {
		cout << "c=" << c << endl;
	}
}