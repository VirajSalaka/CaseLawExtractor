package org.elixir;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.simple.*;
import org.apache.lucene.index.Term;
import org.elixir.utils.Utils;

import java.io.IOException;
import java.util.*;

public class ArgumentTreeGenerator {
	private static ArrayList<TerminalResult> terminalResultSet = new ArrayList<>();

	private static final ArrayList<String> SUBJECT_LIST = new ArrayList<>(Arrays.asList("Petitioner", "Government","Defendant"));

	private static ArrayList<String> currentSubjects = new ArrayList<>();

	private static ArrayList<ArrayList<ArrayList<String>>> extractedArguments = new ArrayList<>();

	private static ArrayList<Integer> lastSubjects = new ArrayList<>();

	private static boolean hasSubject = false;

	private static String lastSentence = null;

	private static String finalRawSentence;

	public static ArrayList<ArrayList<ArrayList<String>>> getExtractedArguments(){
		return extractedArguments;
	}


	public static void main(String[] args) {

		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
		Properties props = new Properties();
		props.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog, openie, ner");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

		// read some text in the text variable
		String text = "The Court in Martinez made clear, however, that it exercised its equitable discretion in view of the unique importance of protecting a defendant's trial rights, particularly the right to effective assistance of trial counsel.";
		ArrayList<String> rawSentences = new ArrayList<>();
		Document doc = new Document(text);
		List<Sentence> sentences1 = doc.sentences();
		for (Sentence sentence : sentences1) {
			rawSentences.add(sentence.toString());
		}

		for (String rawSentence : rawSentences) {

			String ss = rawSentence.replaceAll("(that,|that|'s)", "");
			//String ss = rawSentence.replaceAll("(zzz)", "");
            finalRawSentence = rawSentences.get(rawSentences.size()-1);

			System.out.println("splitted sentence : " + ss);
			// create an empty Annotation just with the given text
			Annotation document = new Annotation(ss);

			// run all Annotators on this text
			pipeline.annotate(document);

			// these are all the sentences in this document
			// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
			List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
			for (CoreMap sentence : sentences) {

			    ArrayList<Integer> sentenceAdded = new ArrayList<>();


				String rawSentence1 = sentence.get(CoreAnnotations.TextAnnotation.class);
				// traversing the words in the current sentence
				// a CoreLabel is a CoreMap with additional token-specific methods
				for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
					// this is the text of the token
					String word = token.get(CoreAnnotations.TextAnnotation.class);
					// this is the POS tag of the token
					String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
					// this is the NER label of the token
					String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
					//System.out.println(word + " " + pos + " " + ne);
				}

				Collection<RelationTriple> triples =
						sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);
				// Print the triples

				for (RelationTriple triple : triples) {
//                    System.out.println(triple.confidence + "\t" +
//                            triple.subjectLemmaGloss() + "\t" +
//                            triple.relationLemmaGloss() + "\t" +
//                            triple.objectLemmaGloss());
					System.out.println(triple.confidence + "\t" +
							triple.subjectGloss() + "\t" +
							triple.relationGloss() + "\t" +
							triple.objectGloss());
					String sentenceSubject = triple.subjectLemmaGloss();
					//System.out.println("subject : " + sentenceSubject);

				}

				System.out.println("------------------------------------------------");
				System.out.println("");

			}

		}

		try {
			Utils.writeToJson(extractedArguments);
		}
		catch (IOException e) {
			System.out.println("Error writing extracted arguments to JSON file");
			e.printStackTrace();
		}
	}   // main

}

class TerminalResult{
	public ArrayList<String> subjects;
	public ArrayList<ArrayList<ArrayList<String>>> argumentList;
	public ArrayList lastSubjects;
}
