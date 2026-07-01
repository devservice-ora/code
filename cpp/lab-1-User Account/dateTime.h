/*******************************************************
 * Class Name: DateTime
 * Purpose: DateTime class derived from Time to store
 * Month, Day, and Year.
 *******************************************************/
#pragma once
#include "time.h"
#include <iostream>
using namespace std;

class DateTime : public Time {
public:
    //constructor
    DateTime();
    DateTime(int h, int min, int s, int mon, int d, int y);

    //mutator
    void setMonth(int mon);
    void setDay(int d);
    void setYear(int y);

    //accessor
    int getMonth() const;
    int getDay() const;
    int getYear() const;

private:
    int month;
    int day;
    int year;
};
