import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class characterRecognizer extends NetworkTrainer {
	
	private Map<Integer[], String> letterMap;
	Scanner sc;
	
	private static final double outputThreshold = 0.5; 

	public characterRecognizer(String trainingDataFilename, String letterFilename, 
			String letterDataFilename) throws IOException {
		super(trainingDataFilename);
		createLetterMap(letterFilename);
		sc = new Scanner(System.in);
	}
	
	public void createLetterMap(String letterFilename) {
		letterMap = new HashMap<Integer[], String>();
		
		try {
			List<String> letterData = Files.readAllLines(Paths.get(letterFilename), Charset.defaultCharset());
			
			int sizeOfEncoding = Integer.parseInt(letterData.get(0));
		    
			String[] lineData;
			Integer[] encoding;
			for(int i = 1; i < letterData.size(); i++) {
				lineData = letterData.get(i).split(" ");
				encoding = new Integer[sizeOfEncoding];
				
				for(i = 0; i < sizeOfEncoding; i++) {
					encoding[i] = Integer.parseInt(lineData[i]);
				}
				letterMap.put(encoding, lineData[sizeOfEncoding]);
				
			}
		    
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void runInput(String letterDataFilename) {
		
		try {
			ArrayList<Integer> result = new ArrayList<Integer>();
			List<String> inputData = Files.readAllLines(Paths.get(letterDataFilename), Charset.defaultCharset());
			
			
			String[] inputLine;
			ArrayList<Double> inputValues = new ArrayList<Double>();
			ArrayList<Double> outputValues = new ArrayList<Double>();
			
			for(int i = 0; i < inputData.size(); i++) {
				inputLine = inputData.get(i).split(" ");
				for(String s : inputLine) {
					inputValues.add(Double.parseDouble(s));
				}
				network.enterData(inputValues);
				outputValues = network.getOutputValues();
				System.out.println(getCharacter(outputValues));
				
				inputValues.clear();
			}
		    
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getCharacter(ArrayList<Double> outputEncoding) {
		Integer[] out = new Integer[outputEncoding.size()];
		for(int i = 0; i < outputEncoding.size(); i++) {
			Double d = outputEncoding.get(i);
			if(d > outputThreshold) {
				out[i] = 1;
			} else if (d < -1*outputThreshold) {
				out[i] = 0;
			} else {
				out[i] = -1; //inconclusive/ambiguous output
			}
		}
		
		if(letterMap.containsKey(out)) {
			return letterMap.get(out);
		} else {
			//Either the letter encoding is not in the map or an 
			//output value was inconclusive. 
			return "0"; 
		}
	}

	public static void main(String[] args) throws IOException {
		characterRecognizer rec = new characterRecognizer(args[0], args[1], args[2]);
		
		
	}
}