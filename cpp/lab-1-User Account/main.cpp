/*******************************************************
 * Program Name: Lab 1 - Manage User Accounts
 * Author: Solution
 * Date: 02/25/2026
 * Description: Create an object-oriented programming (OOP) program that manages User Accounts.
 * Your program should run continuously until the user selects exit.
 *******************************************************/

#include "loginMenu.h"
using namespace std;

int main() {
    int input;
    LoginMenu loginMenu;

    do {
        input = loginMenu.get_input();
        switch(input) {
            case 1:
                loginMenu.doLogin(); // log into new account
                break;
            case 2:
                loginMenu.create(); // create new account and log into it automatically
                break;
            case 3:
                loginMenu.remove(); // remove currently logged in user from vector
                break;
            case 4:
                loginMenu.reset(); // reset password (change password)
                break;
        }
    }
    while (input != 5);

    loginMenu.doLogout();
    loginMenu.writeUserData();

    return 0;
}