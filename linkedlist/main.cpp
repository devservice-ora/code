#include <string>
#include <iostream>
#include "linkedlist.h"

using namespace std;

int main()
{
    LinkedList names;

    names.insert("Admin");
    names.insert("John");
    names.insert("Guess");
    names.insert("Smith");

    // Add a value in fourth place

    Iterator pos = names.begin();
    pos.next();
    pos.next();
    pos.next();

    names.insert(pos, "Sam");

    // Remove the value in second place

    pos = names.begin();
    pos.next();

    names.erase(pos);

    // Print all values

    for (pos = names.begin(); !pos.equals(names.end()); pos.next())
    {
        cout << pos.get() << endl;
    }

    // Now access using index
    for (int i = 0; i < 4; i++)
    {
        cout << i << ": " << names.get(i) << endl;
    }
    names.erase(names.begin());
    // Now access using index
    cout << "After deleting first element" << endl;
    for (int i = 0; i < 3; i++)
    {
        cout << i << ": " << names.get(i) << endl;
    }
    Iterator iter = names.begin();
    iter.next();
    iter.next();
    names.erase(iter);
    cout << "After deleting last element" << endl;
    for (int i = 0; i < 2; i++)
    {
        cout << i << endl;
        cout << i << ": " << names.get(i) << endl;
    }

    return 0;
}


