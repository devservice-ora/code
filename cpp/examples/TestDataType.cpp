#include <iostream>
#include <string>
using namespace std;

void testDataType()
{
	cout << "C++ data type with the auto keyword and number of bytes:" << endl;
	cout << "========================================================" << endl;
	auto testChar = 'D';
	cout << "sizeof(char) = " << sizeof(testChar) << endl;

	auto testBool = true;
	cout << "sizeof(bool) = " << sizeof(testBool) << endl;

	auto testInt = 0;
	cout << "sizeof(int) = " << sizeof(testInt) << endl;

	auto testLong = 459L;
	cout << "sizeof(long) = " << sizeof(testLong) << endl;
	
	auto testLongLong = 459LL;
	cout << "sizeof(long long) = " << sizeof(testLongLong) << endl;

	auto testDouble = 12.0;
	cout << "sizeof(double) = " << sizeof(testDouble) << endl;

	long double  longDouble = 12;
	auto testLongDouble = longDouble;
	cout << "sizeof(long double) = " << sizeof(testLongDouble) << endl;

	auto testString = "Hello";
	cout << "sizeof(string) = " << sizeof(testString) << "; string length = " << ((string)testString).length() << endl;

	//cout << "Type casting from int to char of printable ASCII charaset set (see page 1212):" << endl;
	//cout << "==============================================================================" << endl;
	//for (int i = 65; i <= 126; i++) {
	//	cout << "value: " << i << "; ascii character: " << static_cast<char>(i) << endl;
	//}

	cout << "Type casting from long long to int:" << endl;
	cout << "===================================" << endl;
	// 20 digits
	long long longLong = 9999999999999999999LL;
	// type casting C++ style
	int x = int(longLong);
	// value of int is underflow.
	cout << "From long long = " << longLong << " to int = " << x << endl;
	// value of long long is underlow - type casting from long long to short.
	unsigned short s = (long long)(unsigned short)x;
	cout << "From int = " << x << " to unsigned short = " << s << endl;
	// unsigned short (or any of data types) - overflow
	short x2 = s + 1;
	// notice value for x2 is zero.
	cout << "unsigned short = " << s << "; short = " << x2 << endl;

	cout << "Overflow:" << endl;
	cout << "=========" << endl;
	short s3 = 32767;
	short s4 = s3 + 1; // s3 = s3 + 1;
	cout << "short = " << s3 << ";  short + 1 = " << s4 << endl;
	cout << "Underflow:" << endl;
	cout << "=========" << endl;
	short s5 = s4 - 1;
	cout << "short = " << s4 << ";  short - 1 = " << s5 << endl;
}

