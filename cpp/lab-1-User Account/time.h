/*******************************************************
 * Class Name: Time
 * Purpose: Stores time.
 * Allows for hours, minutes, and second to be accessed and mutated.
 *******************************************************/
#pragma once

class Time {
public:
    //constructor
    Time();
    Time(int h, int m, int s);

    //destructor
    ~Time() {};

	// mutator methods
    void setHour(int);
    void setMinute(int);
    void setSecond(int);

	// accessor methods
    int getHour() const;
    int getMinute() const;
    int getSecond() const;

	// all operators
private:
	int hour;
	int minute;
	int second;
};
