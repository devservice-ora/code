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