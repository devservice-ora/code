/*******************************************************
 * Program Name: Demo class Menu
 * Author: Solution
 * Date: 02/25/2026
 * Description: Program Overview
 * This is an object-oriented console application designed to manage and display an interactive user menu. It allows a developer to dynamically build a list of options, display them to * the console, and safely capture valid numeric choices from a user.
 * Core Components
 * Main Application (main.cpp):
 * Acts as the driver for the program.
 * It instantiates a Menu object, adds three specific options ("1) option 1", "2) option 2", and "3) Exit"), and enters a loop.
 * The loop repeatedly requests input from the user and prints their selection until the user chooses option 3 to exit.
 * The Menu Class (menu.h & menu.cpp):
 * Data Storage: It uses an internal std::vector of strings named options to dynamically store the list of choices. It also includes an unused title string variable intended for naming specific menus (like a "Login Menu").
 * add_option(string option): Appends a new menu choice string to the internal vector.
 * display(): A private helper method that cleans up the console spacing and prints every stored menu option line by line.
 * get_input(): The primary logic engine of the class. It displays the menu and grabs user input as a string.
 * Hidden Feature: If the user types "x" or "X", the program automatically treats it as selecting the final menu option (the exit option).
 * Input Validation: It wraps the interaction in a loop that ensures the program will not accept an answer unless it translates to a valid integer matching one of the available menu numbers.
 *******************************************************/

#include <iostream>
#include "menu.h"
using namespace std;

int main() {
    int input;
    Menu _menu;
    _menu.add_option("1) option 1");
    _menu.add_option("2) option 2");    
    _menu.add_option("3) Exit");

    do {
        input = _menu.get_input();
        cout << "Selected: " << input << endl;
    }
    while (input != 3);

    return 0;
}
