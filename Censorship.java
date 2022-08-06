import java.util.HashMap;

/**
 * this is my censoship class which handles redacting the proper nouns
 */
public class Censorship {
    //we use two arrays one for words and one fore words we want to redact
    private String[] words;
    private String[] badWords;
    CW2Q6 curr = new CW2Q6();

    //similarly we use two hashmaps , one for possible redact words and one for words we are sure of
    HashMap<String, Integer> redacts = new HashMap<String, Integer>();
    HashMap<String, Integer> redactsPass = new HashMap<String, Integer>();

    //this is a counter which stores the values of words which are ambiguous in origin
    private int[] badder = new int[100000];
    private int  badamount;
    private int  k;

    /**
     * constructor
     * @param words - words in the file
     * @param badWords - words to redact
     */
    public Censorship(String[] words, String[] badWords)
    {
        this.words = (words);
        this.badWords = badWords;
    }

    /**
     * this is the function which will redact all the proper nouns
     * @return
     */
    public String[] redact(){
        //for every word in the word array we want to check it
        for (int i = 0; i < words.length; i++)
        {
            //if the badder array is full give it more space
            if(badder.length == k ){
                badder = curr.dynamicMakeint(badder,badder.length + 30000);
            }
            //if the word fails the check than redact it
            if(!checkWord(words[i])){
                words[i] = star(words[i]);
                badamount++;
                //if it fails this check redact it and add it to the hash map
            } else if (isNoun(i)){
                increment(i);
                words[i] = star(words[i]);

                badamount++;
            }
        }
        //for every word in the total redact word set, we want to check if it passes
        //if it passes we add it to the definitely a pronpoun hashset
        for (String i : redacts.keySet()) {
            if(redacts.get(i) > -8){
                redactsPass.put(i,redacts.get(i));
            }
        }




        int j = 0;
        int check;
        // for every word we classed as ambiguous check if it is one of the new pronouncs found
        while (j < k) // Loops through full stop points and checks the word that follows it
        {
            check = badder[j];
            if(checkRedactWord(words[check])) {
                words[check] = star(words[check]);
                badamount++;
            }
            j++;
        }


        //return the new word array
        return words;
    }

    /**
     * getter for the amount of reducted words
     * @return
     */
    public int getBadamount(){

        return badamount;
    }

    /**
     * this is a getter for all the proper nouns old and new
     * @return
     */
    public String[] getBadWords(){
        int size = badWords.length + redactsPass.size();

        String[] newBad = new String[size];
        int o ;

        for(o = 0;o<badWords.length;o++){
            newBad[o] = badWords[o];
        }

        for (String i : redactsPass.keySet()) {
            newBad[o++] = i;
        }
        return newBad;
    }

    /**
     * this is a function to star out a word
     * @param badword - word to star out
     * @return - the starrred out word
     */
    private String star(String badword) {
        StringBuilder n = new StringBuilder();
        for(int i = 0; i < badword.length(); i++){
            n.append("*");
        }
        return n.toString();
    }

    /**
     * this is a function to check if the word is in the new hashmap
     * @param word - the word
     * @return - if it is or not
     */
    private boolean checkWord(String word) {
        for (int i = 0; i < badWords.length; i++) {
            if (compare(badWords[i], word)) {
                return false;
            }
        }
        return true;
    }

    /**
     * this is a function to check if a word is in the redactPass hashmap
     * @param word
     * @return
     */
    private boolean checkRedactWord(String word) {
            if(redactsPass.containsKey(word)){
                return true;
            }
            return false;
    }

    /**
     * this is a function which will compare two strings and return if they are the same
     * @param a - word a
     * @param b - word b
     * @return - if they are the same
     */
    private static boolean compare(String a, String b) {
        if (a.length() != b.length()){
            return false;
        }
        for(int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    /**
     * this increments the value associatedd with a new pronoun or adds it
     * @param i this is the index to decrement
     */
    private void increment(int i){
        if (redacts.containsKey(words[i])) {
            redacts.put(words[i], redacts.get(words[i]) +15);
        }else{
            redacts.put(words[i],  20);
        }
    }

    /**
     * this similar to above will decrement its value
     * @param i this is the index to decrement
     */
    private void decrement(int i){
        if (redacts.containsKey(words[i])) {
            redacts.put(words[i], redacts.get(words[i]) - 5);
        }else{
            redacts.put(words[i], -10);
        }
    }

    /**
     * this is a function to check if a word is a noun
     * this will perform several checks on the word to decide if it is definitely a proper noun
     * or if it is ambiguous, if it is it is just added to the ambiguity list and to the possible proper noun hash
     * set
     * @param i the index of the noun
     * @return - returns if it is a noun or not
     */
    private boolean isNoun(int i)
    {
        if(badder.length == k ){
            badder = curr.dynamicMakeint(badder,badder.length + 30000);
        }
        if (words[i].length() > 1) {
            if (!(isCaps(i,0))){
                return false;
            }
            String prior = words[i-1];
            if (prior.charAt(0) == '\"' || prior.charAt(0) == '-' || prior.charAt(0) == '!' || prior.charAt(0) == '“') {
                if (words[i-2].charAt(0) == 10) {
                    badder[k++] = i;
                    decrement(i);
                    return false;
                }
                if (words[i-2].length() == 1 && words[i-3].length() == 1) {
                    badder[k++] = i;
                    decrement(i);
                    return false;
                }
            }
            if (prior.charAt(0) == ' ' && words[i-2].length() == 1 && words[i-2].charAt(0) != 'I') {
                int a = (int) words[i-2].charAt(0);
                if(curr.isLetter(a)){
                    increment(i);
                    badder[k++] = i;
                    return false;
                }else{
                    badder[k++] = i;
                    decrement(i);
                    return false;
                }

            }
            if (prior.charAt(0) == 10 && words[i-3].length() == 1 )
            {
                badder[k++] = i;
                decrement(i);
                return false;
            }

            if ((isCaps(i,0))&&!(isCaps(i,1))) {
                return true;
            }
        }
        return false;
    }
    /**
     * this is a simple function to check if a letter is a captial one
     */
    private boolean isCaps(int i,int j){
        if((words[i].charAt(j) > 64 && words[i].charAt(j) < 91)){
            return true;
        }else{
            return false;
        }
    }


}
