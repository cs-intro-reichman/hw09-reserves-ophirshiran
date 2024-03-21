import java.util.HashMap;
import java.util.Random;


public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;

    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
    private Random randomGenerator;
    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();

  }


    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();

    }

    /** Builds a language model from the text in the given file (the corpus). */
public void train(String fileName) {
   String window = "";
   char c;
   In in = new In(fileName);
   for (int i = 0; i < windowLength; i++)
   { 

     // Gets the next character
      char temp  = in.readChar();
      window += temp;
  }
 
  while (!in.isEmpty())
  {
    c = in.readChar();
    List probs = CharDataMap.get(window);
      if (probs == null) 
      // Creates a new empty list, and adds (window,list) to the map
        {
            probs = new List();
            CharDataMap.put(window, probs);
        }
        //Calculates the counts of the current character.
        probs.update(c);
        window = (window + c).substring(1);
    }
   // The entire file has been processed, and all the characters have been counted.
   // Proceeds to compute and set the p and cp fields of all the CharData objects
   // in each linked list in the map.
  for (List probs : CharDataMap.values())
  {

      calculateProbabilities(probs);
  }
}
    // Computes and sets the probabilities (p and cp fields) of all the
    // characters in the given list. */
    public  static void calculateProbabilities(List probs) {
     ListIterator iterator = new ListIterator(probs.getFirstNode());
     if (!iterator.hasNext()) 
     {

        return;
     }
    int totalCount = 0;
    while (iterator.hasNext()) 
    {
        CharData current = iterator.next();
        totalCount += current.count;
    }
    // Reset iterator 
    iterator = new ListIterator(probs.getFirstNode());
    double prev = 0;
        while (iterator.hasNext()) 
        {

            CharData current = iterator.next();
            current.p = ((double)current.count) / totalCount;
            current.cp = current.p + prev;
            prev = current.cp;
        }
      } 
                    
      
    // Returns a random character from the given probabilities list.
    public char getRandomChar(List probs) 
    {

        calculateProbabilities(probs);
        ListIterator iterator = new ListIterator(probs.getFirstNode());
        double r = this.randomGenerator.nextDouble();//using random generator as asked
        CharData first = probs.getFirstNode().cp;
        char top =  first.chr;
        if (first.cp > r) 
        {
        return top;
        }
        while (iterator.hasNext()) 
        {
            CharData current = iterator.next();
            double val = current.cp;
               if ( val > r)
                 {
                    return current.chr;
                 }
        }
        return '_';
    }               
        
    

    /**
     * Generates a random text, based on the probabilities that were learned during training. 
     * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
     * doesn't appear as a key in Map, we generate no text and return only the initial text. 
     * @param numberOfLetters - the size of text to generate
     * @return the generated text
     */
    public String generate(String initialText, int textLength) 
    {
         // check If the initial text is shorter than the window length
    if (initialText.length() < windowLength) {
        return initialText;
    }
    
    // Initialize the generated text with the initial text
    //StringBuilder gt = new StringBuilder(initialText);
    // Set the initial window to the last windowLength characters of the initial text
    String window = initialText.substring(initialText.length() - windowLength);
    String gt = window;
    int counter = textLength + windowLength;
    // Continue generating text until the length of the generated text reaches the desired text length
    while (gt.length() < counter) {
        // Get the list associated with the current window from the Hashmap
        List  probabilitieslist = CharDataMap.get(window);
        // If the current window is not found in the map, stop the process and return the generated text so far
        if (probabilitieslist == null) {
            break;
        }
        // Generate a random character based on the probabilities in the list
        char nextChar = getRandomChar(probabilitieslist);
        // connects the generated character to the generated text based on the stringbuilder
        gt += nextChar;;
        // Move the window by removing the first character and adding the newly generated character
        window = gt.substring(gt.length() - windowLength);
    }

    // Returns the generated text
    return gt;

}   
    /** Returns a string representing the map of this language model. */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (String key : CharDataMap.keySet()) {
            List keyProbs = CharDataMap.get(key);
            str.append(key + " : " + keyProbs + "\n");
        }

        return str.toString();
    }

         public static void main(String[] args) {

       int windowLength = Integer.parseInt(args[0]);
       String initialText = args[1];
       int generatedTextLength = Integer.parseInt(args[2]);
       Boolean randomGeneration = args[3].equals("random");
       String fileName = args[4];
       // Create the LanguageModel object
       LanguageModel lm;
       if (randomGeneration)
           lm = new LanguageModel(windowLength);
       else
           lm = new LanguageModel(windowLength, 20);
       // Trains the model, creating the map.
       lm.train(fileName);
       // Generates text, and prints it.
       System.out.println(lm.generate(initialText, generatedTextLength)) ;

      }
    }