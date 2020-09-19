/**
 * @author heyuan
 */
public class LinearProbingHashTable<E> {
    private static final int DEFAULT_TABLE_SIZE = 11;
    private int currentSize;
    private HashEntry<E>[] array;

    public LinearProbingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }

    public LinearProbingHashTable(int size){
        allocateArray(size);
        makeEmpty();
    }

    public void makeEmpty(){
        currentSize = 0;
        for(int i = 0; i < array.length; i++){
            array[i] = null;
        }
    }

    public boolean contains(E x){
        return isActive(findPos(x));
    }

    public void insert(E x){
        int currentPos = findPos(x);
        if(isActive(currentPos)){
            return;
        }

        array[currentPos] = new HashEntry<>(x,true);
        if(currentSize > array.length / 2){
            reHash();
        }
    }

    public void remove(E x){
        int currentPos = findPos(x);
        if(isActive(currentPos)){
            array[currentPos].isActive = false;
        }
    }

    private static class HashEntry<E>{
        public E data;
        public boolean isActive;

        public HashEntry(E data, boolean isActive){
            this.data = data;
            this.isActive = isActive;
        }

        public HashEntry(E data){
            this(data, true);
        }
    }

    private void allocateArray(int arraySize){
        array = new HashEntry[nextPrime(arraySize)];
    }

    private int myHash(E x){
        int hashValue = x.hashCode();
        hashValue %= array.length;
        if(hashValue < 0){
            hashValue += array.length;
        }
        return hashValue;
    }

    private static int nextPrime(int n){
        if(n % 2 == 0) {
            n++;
        }
        for(;!isPrime(n);n += 2){
            ;
        }
        return n;
    }

    private static boolean isPrime(int n){
        if(n == 2 || n == 3) {
            return true;
        }
        if(n ==1 || n % 2 ==0) {
            return false;
        }
        for(int i = 3; i * i <= n;i += 2) {
            if(n % i == 0) {
                return false;
            }
        }
        return true;
    }

    private int findPos(E x){
        int currentPos = myHash(x);

        while(array[currentPos] != null && !array[currentPos].data.equals(x)){
            currentPos++;
            if(currentPos >= array.length){
                currentPos -= array.length;
            }
        }

        return currentPos;
    }

    /**
     *
     * @param currentPos
     * @return
     */
    private boolean isActive(int currentPos){
        return array[currentPos] != null && array[currentPos].isActive;
    }

    private void reHash(){
        HashEntry<E>[] oldArray = array;
        allocateArray(nextPrime(oldArray.length * 2));
        currentSize = 0;

        for(int i = 0; i < oldArray.length; i++){
            if(oldArray[i] != null && oldArray[i].isActive) {
                insert(oldArray[i].data);
            }
            }
    }
}
