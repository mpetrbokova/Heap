import java.lang.reflect.Array;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class BinaryMaxHeap <T extends Comparable<T>> implements MyPriorityQueue<T> {    
    private int size; // Maintains the size of the data structure
    private T[] arr; // The array containing all items in the data structure
                     // index 0 must be utilized
    private Map<T, Integer> itemToIndex; // Keeps track of which index of arr holds each item.

    public BinaryMaxHeap(){
        // This line just creates an array of type T. We're doing it this way just
        // because of weird java generics stuff (that I frankly don't totally understand)
        // If you want to create a new array anywhere else (e.g. to resize) then
        // You should mimic this line. The second argument is the size of the new array.
        arr = (T[]) Array.newInstance(Comparable.class, 10);
        size = 0;
        itemToIndex = new HashMap<>();
    }

    // Helper method to help with swapping two items
    // used in restoring the heap property
    private void swap(int a, int b) {
        T temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;

        itemToIndex.remove(arr[a]);
        itemToIndex.remove(arr[b]);
        itemToIndex.put(arr[a], a);
        itemToIndex.put(arr[b], b);
    }

    // move the item at index i "rootward" until
    // the heap property holds
    private void percolateUp(int i){
        int parentIndex = (i - 1) / 2;
        if(arr[i] != null && parentIndex >= 0 && arr[i].compareTo(arr[parentIndex]) > 0) {
                swap(i, parentIndex);
                percolateUp(parentIndex);
        }
    }

    // move the item at index i "leafward" until
    // the heap property holds
    private void percolateDown(int i){
        if (i < size - 1 && arr[i] != null) {
            int leftIndex = 2*i + 1;
            int rightIndex = 2*i + 2;
            int toSwap = i;
        
            if (leftIndex < size && arr[leftIndex].compareTo(arr[toSwap]) > 0) {
                toSwap = leftIndex;
            }
        
            if (rightIndex < size && arr[rightIndex].compareTo(arr[toSwap]) > 0) {
                toSwap = rightIndex;
            }
        
            if (toSwap != i) {
                swap(i, toSwap);
                percolateDown(toSwap);
            }
        
        }
        
    }

    // copy all items into a larger array to make more room.
    private void resize(){
        T[] larger = (T[]) Array.newInstance(Comparable.class, arr.length*2);
        for(int i = 0; i < arr.length; i++) {
            larger[i] = arr[i];

        }
        arr = larger;
    }

    // Adds an item into the heap and updates
    // its position to ensure heap property
    public void insert(T item){
        if(size == arr.length){
            resize();
        }
        arr[size] = item;
        itemToIndex.put(item, size);
        percolateUp(size);
        size++;

    }

    // Removes the max of the heap
    // and restores heap property
    public T extract(){
        if(isEmpty()){
            throw new IllegalStateException();
        }
        T min = arr[0];
        swap(0, size - 1);
        arr[size - 1] = null;
        itemToIndex.remove(min);
        size--;
        percolateDown(0);
        return min;
    }

    // Remove the item at the given index.
    // Makes sure to maintain the heap property
    private T remove(int index){
        T item = arr[index];
        swap(index, size - 1);
        arr[size - 1] = null;
        itemToIndex.remove(item);
        size--;
        updatePriority(index);
        return item;
    }

    // Removes the the given item
    public void remove(T item){
        if(!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException("Given item is not present in the priority queue!");
	    }
        remove(itemToIndex.get(item));
    }

    // Determine whether to percolate up/down
    // the item at the given index, then do it
    // to ensure heap property
    private void updatePriority(int index){
        percolateUp(index);
        percolateDown(index);

    }

    // Gets called when the item is changed and must
    // ensure that the heap property is restored
    // Throws an IllegalArgumentException if the given
    // item is not an element of the priority queue.
    public void updatePriority(T item){
	if(!itemToIndex.containsKey(item)){
            throw new IllegalArgumentException("Given item is not present in the priority queue!");
	}
        updatePriority(itemToIndex.get(item));
    }

    // Returns whether or not the heap is empty
    public boolean isEmpty(){
        return size == 0;
    }

    // Returns the size of the heap
    public int size(){
        return size;
    }

    // Returns the max of the heap
    public T peek(){
        if(isEmpty()){
            throw new IllegalStateException();
        }
        return arr[0];
    }
    
    // Returns a list object of the heap
    public List<T> toList(){
        List<T> copy = new ArrayList<>();
        for(int i = 0; i < size; i++){
            copy.add(i, arr[i]);
        }
        return copy;
    }

    // For debugging
    public String toString(){
        if(size == 0){
            return "[]";
        }
        String str = "[(" + arr[0] + " " + itemToIndex.get(arr[0]) + ")";
        for(int i = 1; i < size; i++ ){
            str += ",(" + arr[i] + " " + itemToIndex.get(arr[i]) + ")";
        }
        return str + "]";
    }
    

}
