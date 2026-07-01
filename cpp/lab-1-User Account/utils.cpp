/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
 *******************************************************/
  
#define _CRT_SECURE_NO_WARNINGS // supress localtime warning

#include <fstream>
#include <ctime>
#include "utils.h"

using namespace std;
//Returns the system time
void getCurrentTime(int& mo, int& d, int& yr, int& hr, int& min, int& sec) {
	time_t t = std::time(0);    // get time now
	tm* now = localtime(&t);

	mo = now->tm_mon + 1;
	d = now->tm_mday;
	yr = now->tm_year + 1900;

	hr = now->tm_hour;
	min = now->tm_min;
	sec = now->tm_sec;
}

void openFile(fstream& inFile, string fileName) {
	inFile.open(fileName);
}

/*******************************************************

 * Function Name: writeFile

 * Purpose: writes information from passed in User
 * vector to location passed in.

 * Parameter: const vector<User> &users,
 * string newFileName

 * Return: none

 *******************************************************/
void writeFile(const vector<User> &users, string newFileName) {
    ofstream outFile;
    outFile.open(newFileName);
    string userData;

    for (auto x : users) {
        userData = "";
        userData += to_string(x.getUserId()) + ",";
        userData += x.getUsername() + ",";
        userData += x.getPassword() + ",";
        userData += x.getLoginDateCalendar() + " ";
        userData += x.getLoginDateTime() + ",";
        userData += x.getLogoutDateCalendar() + " ";
        userData += x.getLogoutDateTime() + "\n";
        outFile << userData;
    }

    outFile.close();
}