package annotator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;


public class Annotator {

    HashSet<String> copyrightSet;

    String copyrightChar = "\u00AE";

    public Annotator(){
        // reads file from resources
        copyrightSet = readKeyWords();
    }

    public Annotator(String keywordFilePath){
        copyrightSet = readKeyWords(keywordFilePath);
    }

    public HashSet<String> readKeyWords(){
        String filepath = "keywords.txt";
        System.out.println(filepath);
        return readKeyWords(filepath);
    }

    public HashSet<String> readKeyWords(String filename){
        HashSet<String> keywords = new HashSet<String>();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while((line = reader.readLine()) != null){
                keywords.add(line.toLowerCase());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return keywords;
    }

    public HashSet<String> buildKeyWords(String keywordString){
        String[] keywords = keywordString.toLowerCase().split("\n");
        return new HashSet<String>(Arrays.asList(keywords));
    }

    // assuming copyrightWords doesn't contain any spaces the following code
    // adds a copyright symbol
    public String annotate(String content){
        return annotate(content, copyrightSet);
    }

    public String annotate(String content, HashSet<String> keywords){
        // assumes the file is in ascii encoding without unicodes
        // unfair assumption to make, but decent one given the circumstances
        String[] words = content.split(" ");
        StringBuilder annotated = new StringBuilder();

        for(String word: words){
            annotated.append(word);
            if(keywords.contains(word.toLowerCase())){
                annotated.append(copyrightChar);
            }
            annotated.append(" ");
        }
        return annotated.substring(0, annotated.length()-1);
    }

}
