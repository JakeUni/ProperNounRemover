import java.io.*;

public class CW2Q6 {
    public static void main(String[] args) throws IOException {
        CW2Q6  me = new CW2Q6();
        String[] redact = me.readFile("redact6.txt", true, 100, 100);
        String[] words = me.readFile("warandpeace.txt", false, 10000, 1000);
        Censorship redacter = new Censorship(words, redact);
        me.writeFile(redacter.redact(), "redacted6.txt", false);
        me.writeFile(redacter.getBadWords(), "redact6.txt", true);

        System.out.println(redacter.getBadamount());

    }

    /**
     * this function will read all the words and not words into an array of strings
     *
     * @param file      the file to read from
     * @param justWords a boolean indicating if we only want words or want everythingg
     * @param initial   this is the initial size of the array
     * @param increment this is how much we want the array to increase when we resize it
     * @return
     */
    private String[] readFile(String file, boolean justWords, int initial, int increment) {
        int k = 0;

        String[] words = new String[initial];
        try {
            int c;
            String currWord;

            //make a new string builder and a new buffer to read characters in
            StringBuilder n = new StringBuilder();
            InputStream in = new FileInputStream(file);
            Reader reader = new InputStreamReader(in, "UTF-8");
            Reader buffer = new BufferedReader(reader);
            //if c is not -1 whilst reading in from the buffer it is a character so we want to store it
            while (((c = buffer.read()) != -1)) {
                //if the array is comming close to its maximum size resize it
                if (initial < k + 10) {
                    initial = k + increment;
                    words = dynamicMake(words, initial);
                }
                //if c is a letter add it to the string builder
                if (isLetter(c)) {
                    n.append((char) (c));
                } else {
                    //otherwize if there is a value in the string builder than build the string
                    //store the string and erase the string builder
                    if (n.length() != 0) {
                        currWord = n.toString();
                        words[k++] = currWord;
                        n.setLength(0);
                    }
                    //if we dont only want the words than we will also store the character that was just inputted
                    //which is not a letter
                    if (!justWords) {
                        n.append((char) c);
                        currWord = n.toString();
                        words[k++] = currWord;
                        n.setLength(0);
                    }else{
                        if(c == '\''){

                        }
                    }
                }
            }
        } catch (java.io.FileNotFoundException e) {
            System.out.println("Error file not found");
        } catch (java.io.IOException e) {
            System.out.println("Error Please contact System Admin");
        }
        //this will return the array at the minimum size it should be
        return dynamicMake(words, k);
    }

    /**
     * this function is used to check whether or not a character is a letter
     * through ascii comparrison
     *
     * @param c - the character
     * @return
     */
    public boolean isLetter(int c) {
        //2390 is the ASCII of a random letter in the file which isnt an english letter
        //the rest are english letters
        if ((c == 2390) || (c < 91 && c > 64) || (c < 123 && c > 96)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * this is used to increase the size of a String array
     *
     * @param change - Strinf array to increase size of
     * @param size   new size
     * @return
     */
    private String[] dynamicMake(String[] change, int size) {
        String[] dynamic = new String[size];
        //loop through and copy all values
        for (int i = 0; i < change.length && i < dynamic.length; i++) {
            dynamic[i] = change[i];
        }
        return dynamic;
    }
    /**
     * this is used to increase the size of a int array
     *
     * @param change - int array to increase size of
     * @param size   new size
     * @return
     */
    public int[] dynamicMakeint(int[] change, int size) {
        int[] dynamic = new int[size];
        //loop through and copy all values
        for (int i = 0; i < change.length && i < dynamic.length; i++) {
            dynamic[i] = change[i];
        }
        return dynamic;
    }



    /**
     * This function will write the word array passed to it to a text file
     * It will do this for display purposes and for functional purposes
     *
     * @param words  - array of words
     * @param output - the text file
     * @param adding - this is a boolean marker to represent if it is to be added the same
     *               or if it is to be added with additional syntax
     */
    private void writeFile(String[] words, String output, boolean adding) {
        try {
            //make a new file and write to it
            File file = new File(output);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < words.length; i++) {
                if (adding) {

                    out.append("\"" + words[i] + "\",");
                    out.append((char) 10);

                } else {
                    out.append(words[i]);
                }
            }
            out.close();

        } catch (IOException e) {
            System.err.print("File " + output + " not found, please attempt again");
        }
    }
}
