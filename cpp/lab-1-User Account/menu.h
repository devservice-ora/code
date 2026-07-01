/*******************************************************
 * Class Name: Menu
 * Purpose: Provide functionality for use of a menu
 * in the console.
 *
 *******************************************************/  
#pragma once
#include <string>
using namespace std;

class Menu {
public:
    Menu();
    Menu(string t);
    ~Menu(){};
    void add_option(string option);
    int get_input() const;
private:
    void display() const;
    vector<string> options;
    string title;
};