/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
 *******************************************************/
#pragma once

#include <fstream>
#include <sstream>
#include <string>
#include <vector>
#include "menu.h"
#include "user.h"

using namespace std;

const string USERS_DATA = "/Users/carmart/CLionProjects/CS124/Lab2/users_data.csv"; //location of csv
const string USERS_NEW_DATA = "/Users/carmart/CLionProjects/CS124/Lab2/users_new_data.csv"; //location of new csv

/*******************************************************

 * Class Name: LoginMenu

 * Purpose: Derived class from Menu to provide methods
 * of management for user accounts.

 *******************************************************/
class LoginMenu : public Menu {
public:
    LoginMenu();

    ~LoginMenu() {};

    bool authenticate();
    bool create();
    bool remove();
    bool reset();
    bool doLogin();
    bool doLogout();

    void writeUserData();

private:
    fstream inFile;
    User currentUser;
    vector<User> users;
    bool logged = false;

    void initUserData();
};
