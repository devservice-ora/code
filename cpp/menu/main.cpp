/*******************************************************
 * Program Name: Demo class Menu
 * Author: Solution
 * Date: 02/25/2026
 * Description: Menu Object
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