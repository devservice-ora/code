/*******************************************************
 * Program Name: Lab 1
 * Author: Solution
 * Date: 02/25/2026
  *******************************************************/
#include "menu.h"
#include <iostream>

// Possibly create constructor that takes a label "can be used for loginmenu to be labeled "Login Menu"
Menu::Menu() {

}

Menu::Menu(string t) {
    title = t;
}

/*******************************************************

 * Function Name: add_option

 * Purpose: adds new option to options vector

 * Parameter: string

 * Return: none

 *******************************************************/
void Menu::add_option(std::string option) {
    options.push_back(option);
}

/*******************************************************

 * Function Name: display

 * Purpose: displays all saved options in console

 * Parameter: none

 * Return: none

 *******************************************************/
void Menu::display() const {
    cout << endl;
    for (int i = 0; i < options.size(); i++) {
        cout << options[i] << endl;
    }
}

/*******************************************************

 * Function Name: get_input

 * Purpose: gets user input in console based off
 * displayed options and returns it as an int

 * Parameter: none

 * Return: int

 *******************************************************/
int Menu::get_input() const {
    string input;
    do {
        display();
        cin >> input;
        if(input == "x" || input == "X") {
            input = to_string(options.size());
        }
    }
    while (stoi(input) < 1 || stoi(input) > options.size());
    return stoi(input);
}