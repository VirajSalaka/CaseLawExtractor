import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        ArrayList<String> sentenceList = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader("/home/viraj/demotext.txt");
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] words = sCurrentLine.split(" ");
                for(int i=1;i<words.length-1;i++){
                    String regex = "[0-9]+";
                    if(words[i].equals("v.")){
                        sentenceList.add((sCurrentLine));
                        break;
                    }else if(words[i-1].matches("[0-9]+") && words[i].equals("U.") && words[i+1].equals("S.")){
                        sentenceList.add((sCurrentLine));
                        break;
                    }
                }
            }

            for (String item:sentenceList){
                System.out.println(item);
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }
    }

    public static String sentenceSelecter(String text){
        return "not set";
    }

}
