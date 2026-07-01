/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
 *******************************************************/
  
#include "user.h"
#include <string>
#include <sstream>
#include <iomanip>
#include <ctime>

User::User() {

}

User::User(int uid, string u, string p) {
    userId = uid;
    username = u;
    password = p;
}

void User::setUserId(int uid) {
    userId = uid;
}

void User::setUsername(string u) {
    username = u;
}

void User::setPassword(string p) {
    password = p;
}

int User::getUserId() const{
    return userId;
}

string User::getPassword() const {
    return password;
}

string User::getUsername() const {
    return username;
}

/*******************************************************

 * Function Name: setLoginDateTime

 * Purpose: Takes the combined calendar date and time
 * string from the users_data.csv file and parses for
 * all data used in loginDateTime

 * Parameter: string loginData

 * Return: No return value, void function.

 *******************************************************/
void User::setLoginDateTime(string loginData) {
    string hr, min, sec, mo, d, yr;
    stringstream ss(loginData);
    getline(ss, mo, '/');
    getline(ss, d, '/');
    getline(ss, yr, ' ');
    getline(ss, hr, ':');
    getline(ss, min, ':');
    getline(ss, sec);
    loginDateTime.setHour(stoi(hr));
    loginDateTime.setMinute(stoi(min));
    loginDateTime.setSecond(stoi(sec));
    loginDateTime.setMonth(stoi(mo));
    loginDateTime.setDay(stoi(d));
    loginDateTime.setYear(stoi(yr));
}

/*******************************************************

 * Function Name: setLoginDateTime

 * Purpose: updates loginDateTime values using
 * parameters passed in.

 * Parameter: int mo, int d, int yr, int hr, int min, int sec

 * Return: No return value, void function.

 *******************************************************/
void User::setLoginDateTime(int mo, int d, int yr, int hr, int min, int sec) {
    loginDateTime.setHour(hr);
    loginDateTime.setMinute(min);
    loginDateTime.setSecond(sec);
    loginDateTime.setMonth(mo);
    loginDateTime.setDay(d);
    loginDateTime.setYear(yr);
}

/*******************************************************

 * Function Name: setLogoutDateTime

 * Purpose: Takes the combined calendar date and time
 * string from the users_data.csv file and parses for
 * all data used in logoutDateTime

 * Parameter: string logoutData

 * Return: No return value, void function.

 *******************************************************/
void User::setLogoutDateTime(string logoutData) {
    string hr, min, sec, mo, d, yr;
    stringstream ss(logoutData);
    getline(ss, mo, '/');
    getline(ss, d, '/');
    getline(ss, yr, ' ');
    getline(ss, hr, ':');
    getline(ss, min, ':');
    getline(ss, sec);
    logoutDateTime.setHour(stoi(hr));
    logoutDateTime.setMinute(stoi(min));
    logoutDateTime.setSecond(stoi(sec));
    logoutDateTime.setMonth(stoi(mo));
    logoutDateTime.setDay(stoi(d));
    logoutDateTime.setYear(stoi(yr));
}

/*******************************************************

 * Function Name: setLogoutDateTime

 * Purpose: updates logoutDateTime values using
 * parameters passed in.

 * Parameter: int mo, int d, int yr, int hr, int min, int sec

 * Return: No return value, void function.

 *******************************************************/
void User::setLogoutDateTime(int mo, int d, int yr, int hr, int min, int sec) {
    logoutDateTime.setHour(hr);
    logoutDateTime.setMinute(min);
    logoutDateTime.setSecond(sec);
    logoutDateTime.setMonth(mo);
    logoutDateTime.setDay(d);
    logoutDateTime.setYear(yr);
}

/*******************************************************

 * Function Name: getLoginDateTime

 * Purpose: returns formatted time to be used in
 * saving login data to new csv

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getLoginDateTime() const {
    stringstream s;
    s << setfill('0') << setw(2) << loginDateTime.getHour();
    s << ":";
    s << setfill('0') << setw(2) << loginDateTime.getMinute();
    s << ":";
    s << setfill('0') << setw(2) << loginDateTime.getSecond();
    return s.str();
}

/*******************************************************

 * Function Name: getLoginDateCalendar

 * Purpose: returns formatted calendar date to be used in
 * saving data to new csv

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getLoginDateCalendar() const {
    return to_string(loginDateTime.getMonth()) + "/" + to_string(loginDateTime.getDay()) + "/" + to_string(loginDateTime.getYear());

}

/*******************************************************

 * Function Name: getLogoutDateTime

 * Purpose: returns formatted time to be used in
 * saving logout data to new csv

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getLogoutDateTime() const {
    stringstream s;
    s << setfill('0') << setw(2) << logoutDateTime.getHour();
    s << ":";
    s << setfill('0') << setw(2) << logoutDateTime.getMinute();
    s << ":";
    s << setfill('0') << setw(2) << logoutDateTime.getSecond();
    return s.str();
}

/*******************************************************

 * Function Name: getLogoutDateCalendar

 * Purpose: returns formatted calendar date to be used in
 * saving data to new csv

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getLogoutDateCalendar() const {
    return to_string(logoutDateTime.getMonth()) + "/" + to_string(logoutDateTime.getDay()) + "/" + to_string(logoutDateTime.getYear());
}

/*******************************************************

 * Function Name: getFormattedLoginDate

 * Purpose: returns formatted login date for use in
 * console

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getFormattedLoginDate() const {

    tm * date = new tm;
    date->tm_year = loginDateTime.getYear() - 1900;
    date->tm_mon = loginDateTime.getMonth() - 1;
    date->tm_mday = loginDateTime.getDay();
    date->tm_hour = loginDateTime.getHour();
    date->tm_min = loginDateTime.getMinute();
    date->tm_sec = loginDateTime.getSecond();

    char dateB[50];
    string formattedDate = "";
    strftime(dateB, 50, "%a %m/%d/%Y", date);

    for (int i = 0; i < 50; i++) {
        if (dateB[i] == '\0') {
            return formattedDate;
        }
        formattedDate = formattedDate + dateB[i];
    }

    delete date;
    return formattedDate;
}

/*******************************************************

 * Function Name: getFormattedLogoutDate

 * Purpose: returns formatted logout date for use in
 * console

 * Parameter: none

 * Return: string

 *******************************************************/
string User::getFormattedLogoutDate() const {

    tm * date = new tm;
    date->tm_year = logoutDateTime.getYear() - 1900;
    date->tm_mon = logoutDateTime.getMonth() - 1;
    date->tm_mday = logoutDateTime.getDay();
    date->tm_hour = logoutDateTime.getHour();
    date->tm_min = logoutDateTime.getMinute();
    date->tm_sec = logoutDateTime.getSecond();

    char dateB[50];
    string formattedDate = "";
    strftime(dateB, 50, "%a %m/%d/%Y", date);

    for (int i = 0; i < 50; i++) {
        if (dateB[i] == '\0') {
            return formattedDate;
        }
        formattedDate = formattedDate + dateB[i];
    }

    delete date;
    return formattedDate;
}


/*******************************************************

 * Function Name: operator==

 * Purpose: checks for equality between two user's
 * username and password.

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator==(const User & user) const {
    return ( this->getUsername() == user.getUsername() &&  this->getPassword() == user.getPassword() );
}

/*******************************************************

 * Function Name: operator!=

 * Purpose: checks for inequality between two user's
 * userid

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator!=(const User &user) const {
    return ( this->getUserId() != user.getUserId() );
}

/*******************************************************

 * Function Name: operator>

 * Purpose: checks to see if this-> user has a greater
 * userid than passed in user

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator>(const User &user) const {
    return ( this->getUserId() > user.getUserId() );
}

/*******************************************************

 * Function Name: operator<

 * Purpose: checks to see if this-> user has a lesser
 * userid than passed in user

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator<(const User &user) const {
    return ( this->getUserId() < user.getUserId() );
}

/*******************************************************

 * Function Name: operator>=

 * Purpose: checks to see if this-> user has a greater
 * than or equal to userid than passed in user

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator>=(const User &user) const {
    return ( this->getUserId() >= user.getUserId() );
}

/*******************************************************

 * Function Name: operator<=

 * Purpose: checks to see if this-> user has a lesser
 * than or equal to userid than passed in user

 * Parameter: User & user

 * Return: bool

 *******************************************************/
bool User::operator<=(const User &user) const {
    return ( this->getUserId() <= user.getUserId() );
}