package utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SortableSetTest {
    private SortableSet<Integer> set;

    @BeforeEach
    public void setUp() {
        SortableSet<Integer> set = new SortableSet<>();
        ArrayList<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(2);
        set.setItems(items);
        this.set = set;
    }

    @Test
    public void testAdd() {
        assertTrue(this.set.add(3));
        ArrayList<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(2);
        items.add(3);
        assertEquals(items, this.set.getItems());
        assertFalse(this.set.add(3));
        assertEquals(items, this.set.getItems());
    }

    @Test
    public void testRemove() {
        assertTrue(this.set.remove(1));
        assertFalse(this.set.remove(3));
        ArrayList<Integer> items = new ArrayList<>();
        items.add(2);
        assertEquals(items, this.set.getItems());
    }

    @Test
    public void testSize() {
        assertEquals(2, this.set.size());
        this.set.add(10);
        assertEquals(3, this.set.size());
    }

    @Test
    public void testGet() {
        assertEquals(1, this.set.get(0));
        assertEquals(2, this.set.get(1));
    }

    @Test
    public void testSort() {
        this.set.sort(Comparator.reverseOrder());
        assertEquals(this.set.get(0), 2);
        assertEquals(this.set.get(1), 1);
    }

    @Test
    public void testSafeGetIndexOne() {
        Optional<Integer> optItem = this.set.safeGetIndexOne(1);
        assertTrue(optItem.isPresent());
        assertEquals(1, optItem.get());
        assertFalse(this.set.safeGetIndexOne(3).isPresent());
        assertFalse(this.set.safeGetIndexOne(0).isPresent());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(this.set.isEmpty());
        SortableSet<Integer> newSet = new SortableSet<>();
        assertTrue(newSet.isEmpty());
    }

    @Test
    public void testEquals() {
        SortableSet<Integer> newSet = new SortableSet<>();
        newSet.add(1);

        assertNotEquals(newSet, this.set);
        assertNotEquals(newSet, null);
        assertEquals(newSet, newSet);
    }

    @Test
    public void testHashCode() {
        SortableSet<Integer> newSet = new SortableSet<>();
        newSet.add(1);
        newSet.add(2);
        assertEquals(this.set.hashCode(), newSet.hashCode());
    }

    @Test
    public void testIterator() {
        ArrayList<Integer> items = new ArrayList<>();
        items.add(1);
        items.add(2);
        Iterator<Integer> setIterator = this.set.iterator();
        for (Integer item : items) {
            assertEquals(item, setIterator.next());
        }
    }
}
