#include "dateTime.h"

using namespace std;

DateTime::DateTime() : Time() {
    month = 0;
    day = 0;
    year = 0;
}
DateTime::DateTime(int h, int min, int s, int mon, int d, int y) : Time(h, min, s) {
    month = mon;
    day = d;
    year = y;
}

void DateTime::setMonth(int mon) {
    month = mon;
}

void DateTime::setDay(int d) {
    day = d;
}

void DateTime::setYear(int y) {
    year = y;
}

int DateTime::getMonth() const {
    return month;
}

int DateTime::getDay() const {
    return day;
}

int DateTime::getYear() const {
    return year;
}