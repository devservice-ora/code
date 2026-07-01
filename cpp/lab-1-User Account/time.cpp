/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
  *******************************************************/
#include "time.h"

using namespace std;

//constructor
Time::Time(){
    hour = 0;
    minute = 0;
    second = 0;
}
Time::Time(int h, int m, int s){
    hour = h;
    minute = m;
    second = s;
}

// mutator methods
void Time::setHour(int h){
    hour = h;
}

void Time::setMinute(int m){
    minute = m;
}

void Time::setSecond(int s){
    second = s;
}

// accessor methods
int Time::getHour() const {
    return hour;
}

int Time::getMinute() const {
    return minute;
}

int Time::getSecond() const{
    return second;
}