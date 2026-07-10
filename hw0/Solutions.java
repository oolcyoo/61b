/** Solutions to the HW0 Java101 exercises.
 *  @Ree Allyson Park and [INSERT YOUR NAME HERE]
 */
public class Solutions {

    /** Returns whether or not the input x is even.
     */
    public static boolean isEven(int x) {
        // TODO: Your code here. Replace the following return statement.
        if (x%2 == 0) {
            return true;
        }
        return false;
    }

    public static int max(int[] a){
        int top = a[0];
        for (int i = 0; i < a.length; i++){
            if (a[i] > top){
                top = a[i];
            }
        }
        return top;
    }


    public static boolean wordBank(String word, String[] bank){
        for (int i=0; i < bank.length; i++){
            if (word.equals(bank[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean threeSum(int[] a){
        for (int f=0; f < a.length; f++){
            for (int g=0; g < a.length; g++){
                for (int h=0; h < a.length; h++){
                    if (a[f] + a[g] + a[h] == 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
