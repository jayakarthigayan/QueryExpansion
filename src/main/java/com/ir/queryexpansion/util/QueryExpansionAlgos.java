package com.ir.queryexpansion.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import com.ir.queryexpansion.model.Document;
import com.ir.queryexpansion.model.PostingEntry;
import com.ir.queryexpansion.model.Term;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class QueryExpansionAlgos {

	private final TreeMap<Integer,Vector<Double>> DOC_VECTOR;
	private final TreeSet<Document> DOCS;
	private final TreeMap<String,Term> TERMS; 
	private final HashSet<String> STOP_WORDS;
	private final TreeMap<String,Vector<Double>> TERM_VECTOR;
	private StanfordCoreNLP pipeline;		
	
	public QueryExpansionAlgos() {
		super();
		initializeStandfordNLP();
		this.DOC_VECTOR = new TreeMap<Integer,Vector<Double>>();
		this.DOCS = new TreeSet<Document>();
		this.TERMS = new TreeMap<String,Term>();
		this.STOP_WORDS = new HashSet<String>();
		this.TERM_VECTOR = new TreeMap<String,Vector<Double>>();
	}

	public void initializeStandfordNLP()
	{
		Properties props;
	    props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
	    this.pipeline = new StanfordCoreNLP(props);
	}

	public List<String> lemmatize(String documentText)
	{	  
	  List<String> lemmas=new ArrayList<String>();
	  Annotation document=new Annotation(documentText);
	  pipeline.annotate(document);
	  List<CoreMap> sentences=document.get(SentencesAnnotation.class);
	  for (  CoreMap sentence : sentences) {
	    for (    CoreLabel token : sentence.get(TokensAnnotation.class)) {
	      lemmas.add(token.get(LemmaAnnotation.class));
	    }
	  }
	  return lemmas;
	}
	
	public String getStem(final String token)
	{
		Stemmer s = new Stemmer();
		s.add(token.toString().toLowerCase().toCharArray(), token.toString().length());
		s.stem();
		return s.toString();
	}
	
	public void loadStopWords()
	{
		BufferedReader fileReader;
		try
		{
			fileReader = new BufferedReader(new InputStreamReader(
					this.getClass().getClassLoader().getResourceAsStream("stopwords_en.txt")));
//			FileInputStream fis = new FileInputStream(new File("stopwords_en.txt"));
//			fileReader = new BufferedReader(new InputStreamReader(fis));
			String line;
			while((line = fileReader.readLine()) != null)
			{				
				if(!STOP_WORDS.contains(line.replace("'", "")))
				{
					STOP_WORDS.add(line.replace("'", ""));
				}
			}
		}
		catch(FileNotFoundException fnfex)
		{
			System.out.println(fnfex.getLocalizedMessage());
		}
		catch(IOException ioex)
		{
			System.out.println(ioex.getLocalizedMessage());
		}
	}
	
	private boolean checkValidCharacter(char letter)
	{							
		return ('A'<= letter && 'Z' >= letter)
				|| ('a' <= letter && 'z' >= letter);
	}
	
	private boolean validToken(String token)
	{
		String pattern = "*^*";		
		return !token.equals(pattern);
	}
	
	private int getTokens(final String line, 
			final List<String> tokens,
			final List<String> stemTokens,
			boolean populateStem)
	{		
		int count = 0;
		StringBuffer tokensString = new StringBuffer();
		StringBuffer token = new StringBuffer();
		for(int i=0;i<line.length();i++)
		{					
			char c = line.charAt(i);
			if((c == ' ' || c == '\n' || c=='-') 
					&& token.length() > 0)
			{							
				if(!STOP_WORDS.contains(token.toString())
						&& !token.toString().matches("^([0-9]+)$") 
						&& (token.toString().length() > 1))
				{					
					if(populateStem)
					{					
						stemTokens.add(getStem(token.toString()));					
					}
					tokensString.append(token.toString()+" ");
					count++;
				}						
				token = new StringBuffer();				
			}
			else if(checkValidCharacter(c))
			{
				token.append(c);
			}
		}
		if(token.length() > 0 
				&& !STOP_WORDS.contains(token.toString())
				&& !token.toString().matches("^([0-9]+)$")
				&& (token.toString().length() > 1))
		{
			if(populateStem)
			{				
				stemTokens.add(getStem(token.toString()));
			}
			tokensString.append(token.toString()+" ");			
			count++;
		}
		tokens.addAll(lemmatize(tokensString.toString()));
		return count;
	}		
	
	private void addTokensToVector(final List<String> tokens,
			final List<String> stemTokens,
			final Document doc)
	{
		if(tokens != null && 
				!tokens.isEmpty() && 
				stemTokens != null && 
				!stemTokens.isEmpty())
		{
			final TreeMap<String,Term> postingsList = new TreeMap<String,Term>();
			int index =0;
			for(final String term:stemTokens)
			{
				final String voc = tokens.get(index);
				if(postingsList.containsKey(term))
				{
					final Term termObj = postingsList.get(term); 															
					int docTermFreq = termObj.getPostingEntries().first().getDocTermFrequency();
					docTermFreq++;
					termObj.getPostingEntries().first().setDocTermFrequency(docTermFreq);
					termObj.getVocabularyTerms().add(voc);
				}
				else
				{
					final PostingEntry postingEntry = new PostingEntry();
					postingEntry.setDoc(doc);
					postingEntry.setDocTermFrequency(1);
					final TreeSet<String> newVocSet = new TreeSet<String>();
					newVocSet.add(voc);
					final Term newTerm = new Term();
					newTerm.setTerm(term);
					newTerm.getPostingEntries().add(postingEntry);
					newTerm.getVocabularyTerms().addAll(newVocSet);
					postingsList.put(term, newTerm);
				}								
				index++;
			}
			for(final String postingterm: postingsList.keySet())
			{
				if(TERMS.containsKey(postingterm))
				{
					TERMS.get(postingterm).getPostingEntries().addAll(
							postingsList.get(postingterm).getPostingEntries()
							);
					TERMS.get(postingterm).getVocabularyTerms().addAll(
							postingsList.get(postingterm).getVocabularyTerms()
							);
				}
				else
				{
					TERMS.put(postingterm, postingsList.get(postingterm));
				}
			}
		}
	}
	
	public void parseDocumentText(final List<String> matchedDocs)
	{
		if(matchedDocs != null && 
				!matchedDocs.isEmpty())
		{
			int docCount = 0;
			for(final String docs:matchedDocs)
			{				
				final List<String> tokens = new ArrayList<String>();
				final List<String> stemTokens = new ArrayList<String>();
				int docWordCount = 0;
				final String[] fieldValues = docs.split(";");
				if(fieldValues != null && fieldValues.length > 0)
				{
					for(final String value:fieldValues)
					{
						if(validToken(value))
						{
							docWordCount += getTokens(value.toLowerCase(), tokens, stemTokens, true);
						}
					}
				}
				final Document doc = new Document();
				doc.setDocId(docCount);
				doc.setDocSize(docWordCount);
				addTokensToVector(tokens, stemTokens, doc);
				DOCS.add(doc);
				docCount++;
			}
		}		
	}
	
	private void populateWeight(int docId,
			double weight)
	{
		if(DOC_VECTOR.containsKey(docId))
		{
			DOC_VECTOR.get(docId).add(weight);
		}
		else
		{
			final Vector<Double> weightVector = new Vector<Double>();
			weightVector.add(weight);
			DOC_VECTOR.put(docId, weightVector);
		}
	}
	
	private void populateTermVector(final String term,
					double value)
	{
		if(TERM_VECTOR.containsKey(term))
		{
			TERM_VECTOR.get(term).add(value);
		}
		else
		{
			final Vector<Double> newVector = new Vector<Double>();
			newVector.add(value);
			TERM_VECTOR.put(term, newVector);
		}
	}
	
	private void computeDocumentVector()
	{		
		for(final String term:TERMS.keySet())
		{				
			final Term termObj = TERMS.get(term);
			final Iterator<PostingEntry> postingList = termObj.getPostingEntries().iterator();
			for(final Document doc:DOCS)
			{
				final PostingEntry dummyPostingEntry = new PostingEntry();
				dummyPostingEntry.setDoc(doc);
				if(termObj.getPostingEntries().contains(dummyPostingEntry))
				{
					final PostingEntry entry = postingList.next();
					Double weight = (1+Math.log(entry.getDocTermFrequency())) * 
										Math.log((double)DOCS.size()/(double)termObj.getPostingEntries().size());
					populateWeight(doc.getDocId(), weight);
					populateTermVector(term,entry.getDocTermFrequency());
				}
				else
				{
					populateWeight(doc.getDocId(), 0.0);
					populateTermVector(term,0.0);
				}
			}
		}		
	}
	
	private void computeQueryVector(final TreeMap<String,Term> queryTerms,
			final Vector<Double> queryW1Vector)
	{
		for(String term: TERMS.keySet())
		{
			if(queryTerms.containsKey(term))
			{			
				final PostingEntry postingEntry = queryTerms.get(term).getPostingEntries().first();
				Double weight = (1+Math.log(postingEntry.getDocTermFrequency())) * 
					Math.log((double)DOCS.size()/(double)TERMS.get(term)
							.getPostingEntries().size());
				queryW1Vector.add(weight);				
			}
			else
			{
				queryW1Vector.add(0.0);
			}
		}
	}
	
	
	private void addQueryTokens(List<String> tokens,
			Document doc,
			final TreeMap<String,Term> queryTerms)
	{
		for(String token:tokens)
		{
			if(queryTerms.containsKey(token))
			{
				int docFreq = queryTerms.get(token).getPostingEntries().first().getDocTermFrequency();
				docFreq++;
				queryTerms.get(token).getPostingEntries().first().setDocTermFrequency(docFreq);
			}
			else
			{
				Term term = new Term();
				term.setTerm(token);
				PostingEntry posting = new PostingEntry();
				posting.setDoc(doc);
				posting.setDocTermFrequency(1);
				term.getPostingEntries().add(posting);
				queryTerms.put(token, term);
			}
		}
	}
	
	public TreeMap<String,Term> parseQueryTokens(String line)
	{	
		final TreeMap<String,Term> queryTerms = new TreeMap<String,Term>();
		final List<String> tokens = new ArrayList<String>();		
		final List<String> stemTokens = new ArrayList<String>();
		int docWordCount = 0;	
		final Document lemmaDoc = new Document();
		lemmaDoc.setDocId(-1);
		if(line != null)
		{
			if(validToken(line))
			{
				docWordCount += getTokens(line,tokens,stemTokens,true);						
			}			
		}		
		lemmaDoc.setDocSize(docWordCount);
		addQueryTokens(stemTokens, lemmaDoc, queryTerms);		
		return queryTerms;
	}
	
	private String expandUsingRocchio(final Vector<Double> queryVector,
			final String queryText)
	{
		final StringBuffer expandedQuery = new StringBuffer(queryText);
		int docCount = DOC_VECTOR.size();
		int factorK = (int)(docCount - (0.3 * docCount));
		int nonRelevantDocs = factorK + 1;
		double alpha = 1;
		double beta = 0.75;
		double gamma = 0.15;
		while(factorK >= 0)
		{		
			final Vector<Double> docVector = DOC_VECTOR.get(factorK);
			int count = 0;
			for(final Double value:queryVector)
			{
				queryVector.set(count, (value * alpha)+(docVector.get(count) * beta));
				count++;
			}
			factorK--;
		}
		
		while(nonRelevantDocs < DOC_VECTOR.size())
		{
			final Vector<Double> docVector = DOC_VECTOR.get(nonRelevantDocs);
			int count = 0;
			for(final Double value:queryVector)
			{
				queryVector.set(count, value - (docVector.get(count) * gamma));
				count++;
			} 
			nonRelevantDocs++;
		}
		int termValue = 0;
		for(final String term:TERMS.keySet())
		{
			double value = queryVector.get(termValue);
			if(value > 0.0)
			{
				for(final String voc:TERMS.get(term).getVocabularyTerms())
				{
					if(!expandedQuery.toString().contains(voc))
					{
						expandedQuery.append(" "+voc);
					}
				}
			}
			termValue++;
		}
		return expandedQuery.toString();
	}
	
	private Double multiplyVectors(final Vector<Double> vector1,
			final Vector<Double> vector2)
	{
		Double result = 0.0;
		if(vector1 != null && vector2 != null && (vector1.size() == vector2.size()))
		{
			int count = 0;
			for(final Double value:vector1)
			{
				result += (value * vector2.get(count));
				count++;
			}
		}
		return result;
	}
	
	private Double computeNormCorr(final String term1,
			final String term2)
	{
		Double normCorr = 0.0;
		if(TERM_VECTOR.containsKey(term1) && TERM_VECTOR.containsKey(term2))
		{
			final Vector<Double> vector1 = TERM_VECTOR.get(term1);
			final Vector<Double> vector2 = TERM_VECTOR.get(term2);
			normCorr = multiplyVectors(vector1,vector2)/
						(multiplyVectors(vector1,vector1)+
								multiplyVectors(vector2,vector2)+
									multiplyVectors(vector1,vector2));
		}
		return normCorr;
	}
	
	private Double calculateCosineSim(final String term1,
			final String term2,
			TreeMap<String,Vector<Double>> allTermAssocScores)
	{
		Double cosineScore = 0.0;
		if(allTermAssocScores.containsKey(term1) && allTermAssocScores.containsKey(term2))
		{
			final Vector<Double> vector1 = allTermAssocScores.get(term1);
			final Vector<Double> vector2 = allTermAssocScores.get(term2);
			cosineScore = multiplyVectors(vector1, vector2)/
							(Math.sqrt(multiplyVectors(vector1, vector1)) *
									Math.sqrt(multiplyVectors(vector2, vector2)));
		}
		return cosineScore;
	}
	
	private String expandUsingAssociation(final TreeMap<String, Term> queryTerms,
			final String queryText)
	{
		final StringBuffer expandedQuery = new StringBuffer(queryText);
		
		for(final String term:queryTerms.keySet())
		{
			final TreeMap<String,Double> relatedTermScores = new TreeMap<String,Double>();
			
			if(TERM_VECTOR.containsKey(term))
			{
				for(final String vectorTerm:TERM_VECTOR.keySet())
				{
					if(!vectorTerm.equals(term))
					{
						final Double normCorr = computeNormCorr(term,vectorTerm);  
						relatedTermScores.put(vectorTerm, normCorr);
					}
				}
			}
			if(!relatedTermScores.isEmpty())
			{
				int count =0;				
				while(count < 2)
				{
					count++;
					double maxScore = 0.0;
					String maxRelatedTerm = "";
					for(final String value:relatedTermScores.keySet())
					{
						if(relatedTermScores.get(value) > maxScore)
						{
							maxScore = relatedTermScores.get(value);
							maxRelatedTerm = value;
						}
						
					}
					if(maxScore > 0.0 &&
							!"".equals(maxRelatedTerm))
					{
						
						relatedTermScores.remove(maxRelatedTerm);
						for(final String voc:TERMS.get(maxRelatedTerm).getVocabularyTerms())
						{
							if(!expandedQuery.toString().contains(voc))
							{
								expandedQuery.append(" "+voc);
							}
						}
					}
				}
			}
		}
		return expandedQuery.toString();
	}
	
	private String expandUsingScalar(final TreeMap<String, Term> queryTerms,
			final String queryText)
	{
		final StringBuffer expandedQuery = new StringBuffer(queryText);
		
		final TreeMap<String,Vector<Double>> allTermAssocScores 
									= new TreeMap<String,Vector<Double>>();
		
		for(final String term1:TERM_VECTOR.keySet())
		{
			final Vector<Double> associationScores = new Vector<Double>();
			for(final String term2:TERMS.keySet())
			{
				final Double normCorr = computeNormCorr(term1,term2);
				associationScores.add(normCorr);
			}
			allTermAssocScores.put(term1, associationScores);
		}
		
		for(final String term:queryTerms.keySet())
		{
			final TreeMap<String,Double> relatedTermScores = new TreeMap<String,Double>();
			
			if(allTermAssocScores.containsKey(term))
			{
				for(final String vectorTerm:TERMS.keySet())
				{
					if(!vectorTerm.equals(term))
					{
						final Double cosineScores = calculateCosineSim(term,
														vectorTerm,
														allTermAssocScores);  
						relatedTermScores.put(vectorTerm, cosineScores);
					}
				}
			}
			if(!relatedTermScores.isEmpty())
			{
				int count =0;				
				while(count < 2)
				{
					count++;
					double maxScore = 0.0;
					String maxRelatedTerm = "";
					for(final String value:relatedTermScores.keySet())
					{
						if(relatedTermScores.get(value) > maxScore)
						{
							maxScore = relatedTermScores.get(value);
							maxRelatedTerm = value;
						}
						
					}
					if(maxScore > 0.0 &&
							!"".equals(maxRelatedTerm))
					{
						
						relatedTermScores.remove(maxRelatedTerm);
						for(final String voc:TERMS.get(maxRelatedTerm).getVocabularyTerms())
						{
							if(!expandedQuery.toString().contains(voc))
							{
								expandedQuery.append(" "+voc);
							}
						}
					}
				}
			}
		}
		return expandedQuery.toString();
	}
	
	public HashMap<String,String> getAllExpansions(final String queryText,
											final List<String> matchedDocs)
	{
		HashMap<String, String> expandedQueries = new HashMap<String,String>();
		loadStopWords();
		parseDocumentText(matchedDocs);
		computeDocumentVector();
		final Vector<Double> queryVector = new Vector<Double>();
		final TreeMap<String, Term> queryTerms = parseQueryTokens(queryText);
		computeQueryVector(queryTerms,queryVector);
		expandedQueries.put("roccioQuery", expandUsingRocchio(queryVector, queryText));
		expandedQueries.put("associationQuery", expandUsingAssociation(queryTerms, queryText));
		expandedQueries.put("scalarQuery", expandUsingScalar(queryTerms, queryText));
		return expandedQueries;
	}
}