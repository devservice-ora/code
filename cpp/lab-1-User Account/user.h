/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
 *******************************************************/
#pragma once

#include <string>
#include "dateTime.h"

using namespace std;

/*******************************************************

 * Class Name: User

 * Purpose: Stores userid, username, password,
 * login date, and logout date which is associated with
 * saved user data entries in users_data.csv

 *******************************************************/
class User {
public:
    User();

    User(int uid, string u, string p);

    void setUserId(int uid);

    void setUsername(string u);

    void setPassword(string p);

    void setLoginDateTime(string loginData);

    void setLoginDateTime(int mo, int d, int yr, int hr, int min, int sec);

    void setLogoutDateTime(string logoutData);

    void setLogoutDateTime(int mo, int d, int yr, int hr, int min, int sec);

    int getUserId() const;

    string getPassword() const;

    string getUsername() const;

    string getLoginDateTime() const;

    string getLoginDateCalendar() const;

    string getLogoutDateTime() const;

    string getLogoutDateCalendar() const;

    string getFormattedLoginDate() const;

    string getFormattedLogoutDate() const;

    bool operator==(const User& user) const;

    bool operator!=(const User &user) const;

    bool operator>(const User &user) const;

    bool operator<(const User &user) const;

    bool operator>=(const User &user) const;

    bool operator<=(const User &user) const;

private:
    int userId;
    string username;
    string password;
    DateTime loginDateTime;
    DateTime logoutDateTime;
};
