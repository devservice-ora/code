#include <string>
#include "linkedlist.h"

using namespace std;

Node::Node(string element) {
    data = element;
    previous = nullptr;
    next = nullptr;
}

LinkedList::LinkedList() {
    first = nullptr;
    last = nullptr;
    last_node = nullptr;
    last_index = -1;
}

void LinkedList::insert(string element) {
    Node* new_node = new Node(element);
    if (last == nullptr) // LinkedList is empty
    {
        first = new_node;
        last = new_node;
    } else {
        new_node->previous = last;
        last->next = new_node;
        last = new_node;
    }
}

void LinkedList::insert(Iterator iter, string element) {
    if (iter.position == nullptr) {
        insert(element);
        return;
    }

    Node* after = iter.position;
    Node* before = after->previous;
    Node* new_node = new Node(element);
    new_node->previous = before;
    new_node->next = after;
    after->previous = new_node;
    if (before == nullptr) { // Insert at beginning
        first = new_node;
    } else {
        // Invalidate last access
        last_node = nullptr;
        last_index = -1;
        before->next = new_node;
    }
}

string LinkedList::get(int k) {
    Node* n;
    if (last_node != nullptr && k >= last_index) {
        n =  last_node;
        int t = k;
        k = k - last_index;
        last_index = t;
    } else {
        n = first;
        last_index = 0;
    }
    k--;
    while (k >= 0 && n != nullptr) {
        n = n->next;
        k--;
    }
    last_node = n;
    return n->data;
}

Iterator LinkedList::erase(Iterator iter) {
    Node* remove = iter.position;
    Node* before = remove->previous;
    Node* after = remove->next;
    if (remove == first) {
        first = after;
    } else {
        before->next = after;
    }

    if (remove == last) {
        last = before;
    } else {
        after->previous = before;
    }
    delete remove;
    Iterator r;
    r.position = after;
    r.container = this;
    // Invalid last access
    last_node = nullptr;
    last_index = -1;
    return r;
}

Iterator LinkedList::begin() {
    Iterator iter;
    iter.position = first;
    iter.container = this;
    return iter;
}

Iterator LinkedList::end() {
    Iterator iter;
    iter.position = nullptr;
    iter.container = this;
    return iter;
}

Iterator::Iterator() {
    position = nullptr;
    container = nullptr;
}

string Iterator::get() const {
    return position->data;
}

void Iterator::next() {
    position = position->next;
}

void Iterator::previous() {
    if (position == nullptr) {
        position = container->last;
    } else {
        position = position->previous;
    }
}

bool Iterator::equals(Iterator other) const {
    return position == other.position;
}