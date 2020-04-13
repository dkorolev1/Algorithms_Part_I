import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int n;
    private Item[] items;

    public RandomizedQueue() {
        n = -1;
        items = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == -1;
    }

    // return the number of items on the randomized queue
    public int size() {
        return n + 1;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
        {
            throw new IllegalArgumentException("Argument can't be null");
        }
        if (size() == items.length) {
            resize(items.length * 2);
        }
        n++;
        items[n] = item;
        if (size() > 1) {
            int rIndex = StdRandom.uniform(size());
            if (rIndex != n) {
                Item temp = items[rIndex];
                items[rIndex] = items[n];
                items[n] = temp;
            }
        }
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        if (size() == items.length / 4) {
            resize(items.length / 2);
        }

        Item item = items[n];
        items[n] = null;
        n--;
        return item;
    }

    private void resize(int capacity) {
        Item[] copyItems = (Item[]) new Object[capacity];
        for (int i = 0; i <= n; i++)
        {
            copyItems[i] = items[i];
        }
        items = copyItems;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniform(size())];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item>
    {
        private int i;
        private int[] rIndex;

        public ListIterator() {
            i = 0;
            rIndex = StdRandom.permutation(size());
        }

        public boolean hasNext() {
            return i < rIndex.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item item = items[rIndex[i]];
            i++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.enqueue(5);

        System.out.println("sample1:" + rq.sample());
        System.out.println("sample2:" + rq.sample());
        System.out.println("sample3:" + rq.sample());

        System.out.println("dequeue:" + rq.dequeue());
        rq.enqueue(6);
        System.out.println("dequeue:" + rq.dequeue());
        rq.enqueue(7);
        System.out.println("dequeue:" + rq.dequeue());
        System.out.println("dequeue:" + rq.dequeue());
        System.out.println("dequeue:" + rq.dequeue());
        System.out.println("dequeue:" + rq.dequeue());
        System.out.println("dequeue:" + rq.dequeue());

        Iterator<Integer> iterator1 = rq.iterator();
        Iterator<Integer> iterator2 = rq.iterator();

        while (iterator1.hasNext())
        {
            System.out.println(iterator1.next());
        }
        System.out.println("");
        while (iterator2.hasNext())
        {
            System.out.println(iterator2.next());
        }
    }
}