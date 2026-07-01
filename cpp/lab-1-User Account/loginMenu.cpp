/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
 *******************************************************/
#include "loginMenu.h"
#include "utils.h"
#include <iostream>

LoginMenu::LoginMenu() : Menu("Login Menu") {
    add_option("1) Login");
    add_option("2) Create Account");
    add_option("3) Remove Account");
    add_option("4) Reset Password");
    add_option("X) Exit");
    initUserData();
}

/*******************************************************

 * Function Name: authenticate

 * Purpose: finds a user that matches the provided info,
 * if none is matched then false is returned.
 * If there is a match then mark the user as logged in
 * and update the loginDateTime

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu::authenticate() {
    bool valid = false;
    for (int i = 0; i < (int) users.size(); i++) {
        if (currentUser == users.at(i)) {
            int mo = 0, d = 0, yr = 0, hr = 0, min = 0, sec = 0;
            getCurrentTime(mo, d, yr, hr, min, sec);
            currentUser.setLoginDateTime(mo, d, yr, hr, min, sec);
            currentUser.setUserId(users.at(i).getUserId());
            valid = true;
            break;
        }
    }
    return valid;
}

/*******************************************************

 * Function Name: create

 * Purpose: asks for user input for userid, username,
 * and password. Checks against all already existing
 * users to ensure no repeats. If there is a repeat
 * then the the new user is not logged and instead
 * the user is told about the error.

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu::create() {
    if (logged) {
        doLogout();
    }

    string username, password;
    int uid;
    cout << endl << "Choose an id number: ";
    cin >> uid;
    cout << "Choose a username: ";
    cin >> username;
    cout << "Choose a password: ";
    cin >> password;
    cout << endl;

    if (uid < 1000) {
        cout << "User Id must start at 1000, try something else." << endl;
        return false;
    }

    for (auto x: users) {
        if (username == x.getUsername()) {
            cout << "A user with this username already exists, try something else." << endl;
            return false;
        }
        if (uid == x.getUserId()) {
            cout << "A user with this user id already exists, try something else." << endl;
            return false;
        }
    }

    logged = true;
    currentUser.setUserId(uid);
    currentUser.setPassword(password);
    currentUser.setUsername(username);
    int mo = 0, d = 0, yr = 0, hr = 0, min = 0, sec = 0;
    getCurrentTime(mo, d, yr, hr, min, sec);
    currentUser.setLoginDateTime(mo, d, yr, hr, min, sec);
    return true;
}

/*******************************************************

 * Function Name: remove

 * Purpose: If there is a user currently logged in
 * they are removed from the existing users list.

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu::remove() {
    if (!logged) {
        cout << "You are currently not logged in, try logging in first." << endl;
        return false;
    }

    for (int i = 0; i < users.size(); i++) {
        if(users[i] == currentUser) {
            users.erase(users.begin()+ i);
        }
    }

    cout << currentUser.getUsername() << " has been removed." << endl;
    logged = false;

    return true;
}

/*******************************************************

 * Function Name: reset

 * Purpose: Lets a logged in user change their
 * password with console input.

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu::reset() {
    if (!logged) {
        cout << "You are currently not logged in to an account" << endl;
        return false;
    }

    string password;
    do {
        cout << endl << "What would you like your new password to be: ";
        cin >> password;
        if(password.length() == 0) {
            cout << "You can not enter a password with no length" << endl;
        }
    }
    while (password.length() == 0);

    currentUser.setPassword(password);

    cout << "You password has been changed and you have been logged out, you can now try logging in with your new password." << endl;

    doLogout();

    return true;
}

/*******************************************************

 * Function Name: doLogin

 * Purpose: Takes a users input and attempts to log
 * them into an existing account.

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu::doLogin() {
    if (logged) {
        doLogout();
    }

    int attempt = 0;
    do {
        string username, password;
        cout << endl;
        cout << "Username: ";
        cin >> username;
        cout << "Password: ";
        cin >> password;
        currentUser.setUsername(username);
        currentUser.setPassword(password);
        cout << endl;

        if (authenticate()) {
            logged = true;
            cout << "Successfully Logged In" << endl;
            cout << currentUser.getUsername() << " logged in on " << currentUser.getFormattedLoginDate() << "." << endl;
            return true;
        } else {
            cout << "Failed Authentication Attempt, try again." << endl;
        }
    } while (++attempt < 3);
    return false;
}

/*******************************************************

 * Function Name: doLogout

 * Purpose: If a user is logged in their logoutDateTime
 * is updated and user is set to logged out.

 * Parameter: none

 * Return: bool

 *******************************************************/
bool LoginMenu:: doLogout() {
    if (!logged) {
        return false;
    }

    int mo = 0, d = 0, yr = 0, hr = 0, min = 0, sec = 0;
    getCurrentTime(mo, d, yr, hr, min, sec);
    currentUser.setLogoutDateTime(mo, d, yr, hr, min, sec);
    logged = false;

    bool exists = false;
    for (auto & x: users) {
        if (!(x != currentUser)) {
            exists = true;
            x = currentUser;
        }
    }
    if (!exists) {
        users.push_back(currentUser);
    }

    cout << currentUser.getUsername() << " logged out on " << currentUser.getFormattedLogoutDate() << "." << endl;

    return true;
}

/*******************************************************

 * Function Name: initUserData

 * Purpose: fills User vector with information
 * from all users.

 * Parameter: none

 * Return: none

 *******************************************************/
void LoginMenu::initUserData() {
    fstream inFile;
    openFile(inFile, USERS_DATA);

    string id;
    string username;
    string password;
    string loginDateTime;
    string logoutDateTime;


    string line = "";
    while (getline(inFile, line)) {
        stringstream ss(line);
        getline(ss, id, ',');
        getline(ss, username, ',');
        getline(ss, password, ',');
        getline(ss, loginDateTime, ',');
        getline(ss, logoutDateTime, '\r');

        User login;
        login.setUserId(stoi(id));
        login.setUsername(username);
        login.setPassword(password);
        login.setLoginDateTime(loginDateTime);
        login.setLogoutDateTime(logoutDateTime);

        currentUser = login;
        users.push_back(login);
    }

    inFile.close();

}

/*******************************************************

 * Function Name: writeUserData

 * Purpose: writes all currently saved user information
 * to new csv file.

 * Parameter: none

 * Return: none

 *******************************************************/
void LoginMenu::writeUserData() {
    sort(users.begin(), users.end());
    writeFile(users, USERS_NEW_DATA);
}