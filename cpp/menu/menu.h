/*******************************************************
 * Class Name: Menu
 * Purpose: Class Menu definition
 *******************************************************/  

#include <vector>
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