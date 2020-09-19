/**
 * @author heyuan
 */
public class QuadraticProbingHashTable<E> {
    private static final int DEFAULT_TABLE_SIZE = 11;
    private HashEntry<E>[] array;
    private int currentSize;

    public QuadraticProbingHashTable(){
        this(DEFAULT_TABLE_SIZE);
    }

    public QuadraticProbingHashTable(int size){
        allocateArray(size);
        makeEmpty();
    }

    public void makeEmpty(){
        currentSize = 0;
        for(int i = 0; i<array.length; i++){
            array[i] = null;
        }
    }

    public boolean contains(E x){
        int currentPos = findPos(x);
        return isActive(currentPos);
    }

    public void insert(E x){
        int currentPos = findPos(x);
        if(isActive(currentPos)){
            return;
        }

        array[currentPos] = new HashEntry<>(x, true);

        if(currentSize > array.length){
            rehash();
        }
    }

    public void remove(E x){
        int currentPos = findPos(x);
        if(isActive(currentPos) ){
            array[currentPos].isActive = false;
        }
    }

    private static class HashEntry<E>{
        public E data;
        public boolean isActive;

        public HashEntry(E e, boolean i){
            data = e;
            isActive = i;
        }

        public HashEntry(E e){
            this(e, true);
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
        int offset = 1;
        int currentPos = myHash(x);

        while(array[currentPos] != null && !array[currentPos].data.equals(x)){
            currentPos += offset;
            offset += 2;
            if(currentPos >= array.length){
                currentPos -= array.length;
            }
        }

        return currentPos;
    }

    private boolean isActive(int currentPos){
        return array[currentPos] != null && array[currentPos].isActive;
    }

    private void rehash(){
        HashEntry<E>[] oldArray = array;
        allocateArray(nextPrime(2 * oldArray.length));
        currentSize = 0;

        for(int i = 0; i < oldArray.length; i++){
            if(oldArray[i] != null && oldArray[i].isActive){
                insert(oldArray[i].data);
            }
        }
    }

    public static void main(String[] args){

    }

}

