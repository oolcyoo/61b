import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0 || k == 1 || array == null) {
                return;
            }

            for (int i = 0; i < k; i++) {
                for (int j = i; j > 0; j--) {
                    int cur = array[j];
                    int next = array [j - 1];
                    if (cur < next) {
                        array[j] = next;
                        array[j - 1] = cur;
                    }
                }
            }
        }

        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0 || k == 1 || array == null) {
                return;
            }

            for (int i = 0; i < k; i++) {
                int cur = array[i];
                int n = i;
                for (int j = i+1; j < k; j++) {
                    int next = array[j];
                    if (cur > next) {
                        n = j;
                        cur = array[j];
                    }
                }
                array[n] = array[i];
                array[i] = cur;
            }
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0 || k == 1 || array == null) {
                return;
            }
            sort2(array, 0, k);
        }

        private void sort2(int[] array, int min, int max) {
            if (min == max - 1) {
                return;
            }
            int mid = (min + max) / 2;
            sort2(array, min, mid);
            sort2(array, mid, max);
            merge(array, min, mid, max);
        }

        private void merge(int[] array, int min, int mid, int max) {
            for (int i = mid; i < max; i++) {
                int stored = array[i];
                int j;
                for (j = i-1; j >= min; j--) {
                    int cur = array[j];
                    if (cur <= stored) {
                        break;
                    }
                    array[j+1]=array[j];
                }
                array[j + 1] = stored;
            }
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            if (k == 0 || k == 1 || array == null) {
                return;
            }
            int[] spaceArray = new int[k];
            for (int i = 1; i != 0; i <<=1) {
                int count = 0;
                int count2 = 0;
                for (int j = 0; j < k; j++) {
                    if ((array[j] & i) > 0) {
                        count2 += 1;
                    } else {
                        count += 1;
                    }
                }
                if (count + count2 != k) {
                    break;
                }
                count2 = count;
                count = 0;
                for (int g = 0; g < k; g++) {
                    if ((array[g] & i) > 0) {
                        spaceArray[count2++] = array[g];
                    } else {
                        spaceArray[count++] = array[g];
                    }
                }
                System.arraycopy(spaceArray, 0, array, 0, k);
            }
        }


        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
