# Program Overview
# This script is an object-oriented Python console application that manages an interactive user menu. It translates the original C++ structure into Python, allowing a developer to # dynamically build a menu list, print it to the screen, and safely collect validated numeric inputs from a user without risking program crashes due to bad input.
# Core Components
# Main Driver (main.py):
# Instantiates the Menu object and adds three specific options: "1) option 1", "2) option 2", and "3) Exit".
# Enters a while True loop (Python's equivalent to the C++ do-while loop) that repeatedly calls the menu's input method.
# Prints the user's selection and gracefully breaks the loop to terminate the program if the user selects option 3.
# The Menu Class (Menu in menu.py):
# Data Storage (__init__): Instead of a fixed-type std::vector, Python uses a dynamic list (self._options) to hold the menu strings. It also stores an optional self._title string.
# Adding Options (add_option): Uses Python's .append() method to dynamically grow the list of menu choices.
# Displaying (_display): A helper method (marked with a leading underscore to signify it is intended to be private) that loops through and prints each stored string.
# Input Handling & Validation (get_input):
# The Shortcut: Just like the C++ code, if a user types "x" or "X", Python catches it using .lower() and automatically maps it to the final menu option number.
# Crash Prevention: Python uses a try...except ValueError block. If a user types accidental text (like "abc"), Python safely catches the error and loops back to ask again, mirroring the original data-validation behavior. It ensures the returned value is always a valid integer between 1 and the total number of options.

class Menu:
    def __init__(self, title=""):
        """Initializes the Menu with an optional title."""
        self._options = []
        self._title = title

    def add_option(self, option: str):
        """Adds a new option to the options list."""
        self._options.append(option)

    def _display(self):
        """Displays all saved options in the console (Private method)."""
        print()
        for option in self._options:
            print(option)

    def get_input(self) -> int:
        """Gets user input in the console and returns it as a valid integer."""
        while True:
            self._display()
            user_input = input().strip()

            # Handle the 'X' / 'x' exit shortcut feature from the C++ code
            if user_input.lower() == 'x':
                user_input = str(len(self._options))

            try:
                val = int(user_input)
                # Input validation: must be within the range of available options
                if 1 <= val <= len(self._options):
                    return val
            except ValueError:
                # If input isn't a number, loop again just like the C++ do-while behavior
                pass

from menu import Menu

def main():
    _menu = Menu()
    _menu.add_option("1) option 1")
    _menu.add_option("2) option 2")
    _menu.add_option("3) Exit")

    while True:
        user_input = _menu.get_input()
        print(f"Selected: {user_input}")
        
        if user_input == 3:
            break

if __name__ == "__main__":
    main()