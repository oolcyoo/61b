import java.util.Arrays;

/** HW #7, Two-sum problem.
 * @author
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        int[] sortedA = Arrays.copyOf(A, A.length);
        int[] sortedB = Arrays.copyOf(B, B.length);
        Arrays.sort(sortedA);
        Arrays.sort(sortedB);

        int left = 0;
        int right = sortedB.length - 1;
        while (left < sortedA.length && right >= 0) {
            int sum = sortedA[left] + sortedB[right];
            if (sum == m) {
                return true;
            } else if (sum < m) {
                left += 1;
            } else {
                right -= 1;
            }
        }
        return false;
    }

}
