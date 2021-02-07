package utils;

import java.util.*;

// A class representing a sortable set.
public class SortableSet<E> implements Iterable<E> {
    private ArrayList<E> items;

    // EFFECTS: creates a new sortable set
    public SortableSet() {
        this.items = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds an item and returns true if it is not already present;
    //          returns false otherwise
    public boolean add(E item) {
//        return !this.items.contains(item) && this.items.add(item);
        if (!this.items.contains(item)) {
            this.items.add(item);
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: returns true and removes item from the set if it is present;
    //          returns false otherwise
    public boolean remove(E item) {
        return this.items.remove(item);
    }

    // REQUIRES: 0 <= index < this.size()
    // EFFECTS: returns the item at index `index`, assuming indexing starts at 0.
    public E get(int index) {
        return this.items.get(index);
    }

    // EFFECTS: returns the size of the set
    public int size() {
        return this.items.size();
    }

    // MODIFIES: this
    // EFFECTS: sorts the set in place according to the specific comparator
    public void sort(Comparator<? super E> c) {
        this.items.sort(c);
    }

    // EFFECTS: returns item corresponding to the provided index,
    //          assuming that indexing starts at 1
    public Optional<E> safeGetIndexOne(int index) {
        if (index < this.items.size() + 1 && index > 0) {
            return Optional.of(this.items.get(index - 1));
        } else {
            return Optional.empty();
        }
    }

    // EFFECTS: returns true if the set is empty; false otherwise
    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    // EFFECTS: returns true if the passed object is equal to the set; false otherwise
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SortableSet<?> that = (SortableSet<?>) o;
        return Objects.equals(items, that.items);
    }

    // EFFECTS: returns the hash code of the set
    @Override
    public int hashCode() {
        return Objects.hash(items);
    }

    // EFFECTS: returns an iterator over the elements of the set
    @Override
    public Iterator<E> iterator() {
        return this.items.iterator();
    }

    /**
     * GETTERS AND SETTERS
     */
    public ArrayList<E> getItems() {
        return items;
    }

    public void setItems(ArrayList<E> items) {
        this.items = items;
    }
}
