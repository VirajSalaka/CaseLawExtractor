import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        ArrayList<String> sentenceList = new ArrayList<>();
        ArrayList<String> caseList = new ArrayList<>();
        BufferedReader br = null;
        FileReader fr = null;
        try {
            //br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader("/home/viraj/demotext2.txt");
            br = new BufferedReader(fr);

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                String[] words = sCurrentLine.split(" ");
                for(int i=1;i<words.length-1;i++){

                    if(words[i].equals("v.")){
                        sentenceList.add((sCurrentLine));
                        caseList.add(new String(words[i-1].replaceAll("[^a-zA-Z]","")));
                        caseList.add(new String(words[i+1].replaceAll("[^a-zA-Z]","")));

                        break;
                    }else if(words[i-1].matches("[0-9]+") && words[i].equals("U.") && words[i+1].equals("S.")){
                        sentenceList.add((sCurrentLine));
                        break;
                    }
                }
                for(int j=0; j<words.length; j++){
                    String newString = words[j].replaceAll("[^a-zA-Z]","");
                    if(caseList.contains(newString)){
                        if(!sentenceList.contains(sCurrentLine)){
                            sentenceList.add(sCurrentLine);
                        }

                    } else if(words[j].toLowerCase().contains("id.")){
                        if(!sentenceList.contains(sCurrentLine)){
                            sentenceList.add(sCurrentLine);
                        }
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

}
