package TLCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*
* @author Tuan Dinh, Xue Qin
*/
public class Scanner {
	public static HashMap<String, String> MULTIPLE_OPERATOR = new HashMap<String, String>();
	public static HashMap<String, String> SINGLE_OPERATOR = new HashMap<String, String>();
	public static HashMap<String, String> KEYWORDS = new HashMap<String, String>();
	public static HashMap<String, String> PROCEDURES = new HashMap<String, String>();
	public Scanner() {
		
	}
	
	public static void loadDictionary(){
		
		SINGLE_OPERATOR.put("(", "LP");
		SINGLE_OPERATOR.put(")", "RP");
		SINGLE_OPERATOR.put(":=", "ASGN");
		SINGLE_OPERATOR.put(";", "SC");
		
		MULTIPLE_OPERATOR.put("*", "MULTIPLICATIVE");
		MULTIPLE_OPERATOR.put("div", "MULTIPLICATIVE");
		MULTIPLE_OPERATOR.put("mod", "MULTIPLICATIVE");
		MULTIPLE_OPERATOR.put("+", "ADDITIVE");
		MULTIPLE_OPERATOR.put("-", "ADDITIVE");
		String[] list = {"=","!=","<",">","<=",">="};
		for (String s: list){
			MULTIPLE_OPERATOR.put(s, "COMPARE");
		}
		String[] key = {"if","then", "else", "begin", "end", "while", "do", "program", "var", "as", "int", "bool"};
		for (String s: key){
			KEYWORDS.put(s, s.toUpperCase());
		}
		
		PROCEDURES.put("writeInt", "WRITEINT");
		PROCEDURES.put("readInt", "READINT");
		PROCEDURES.put("writeint", "WRITEINT");
		PROCEDURES.put("readint", "READINT");
	}
	
	public static boolean isSingleOperator(String key){
		return SINGLE_OPERATOR.containsKey(key);
	}
	
	public static boolean isMultipleOperator(String key){
		return MULTIPLE_OPERATOR.containsKey(key);
	}

	
	public static boolean isKeyword(String key){
		return KEYWORDS.containsKey(key);
	}
	
	public static boolean isProcedure(String key){
		return PROCEDURES.containsKey(key);
	}
	
	public static boolean isComment(String key){
		return key.startsWith("%");
	}
	
	// The regular expression allows all natural numbers, 
	// but we are using 32-bit integers. 
	// Only 0 through 2147483647 are legal integer.
	public static boolean isNumber(String key){
		int num;
	    String regex = "\\d+";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(key);
	    if (matcher.matches()) {
	    	num = Integer.parseInt(key);
	    	if (num < 2147483647 && num >= 0 )
	    		return true;
	    	else {
	    		System.err.println("Input number illegal, outside range from 0 to 2147483647");
	    		System.exit(1);
	    		return false;
	    	}
	    } else {
	    	return false;
	    }
	}
	
	public static boolean isBoollit(String key){
		if (key.equalsIgnoreCase("false") || key.equalsIgnoreCase("true"))
			return true;
		else
			return false;
	}
	
	public static boolean isIdent(String key){
	    String regex = "[a-zA-Z][a-zA-Z0-9]*";
	    Pattern pattern = Pattern.compile(regex);
	    Matcher matcher = pattern.matcher(key);
	    return matcher.matches();
	}
	
	/* Don't need that
	public static boolean isSeparator(String key){
		if (isOperator(key) && key.equalsIgnoreCase(";"))
			return true;
		else
			return false;
	}
	*/
	
	public static void scan (String inputFilePath){
		loadDictionary();

		int line_num = 0;
		int token_num =0;
		
		
		try(BufferedReader br = new BufferedReader(new FileReader(inputFilePath+".tl"))) {
			
			
			String tokenList = "";
			
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
				token_num = 0;
		    	line_num++;
				
				line = line.trim();
				if (line.equals(""))
					continue; //skip the empty line
				
		    	String[] list = line.split("\\s+");
		    	
		    	for (String token: list){
					token_num++;
		    		if (isComment(token))
						break;
		    		else if (isSingleOperator(token))
		    			tokenList+= (SINGLE_OPERATOR.get(token)) +"\n";  //print out SC, LP,RP
		    		else if (isBoollit(token))
		    			tokenList +=("boollit"+ "(" + token +")")+"\n";		    		
		    		else if (isNumber(token))
		    			tokenList +=("num"+ "(" + token +")")+"\n";
		    		else if (isKeyword(token))
		    			tokenList +=(KEYWORDS.get(token))+"\n";
		    		else if (isMultipleOperator(token))
		    			tokenList +=(MULTIPLE_OPERATOR.get(token) + "("+token+")")+"\n";
		    		else if (isProcedure(token))
		    			tokenList +=(PROCEDURES.get(token))+"\n";
		    		else if (isIdent(token))
		    			tokenList +=("ident"+ "(" + token +")")+"\n";
		    		else {
		    			System.err.println("Illegal token found: " + token + ", line " +line_num +", token " +token_num);
			    		System.exit(1);
		    		}
		    			
		    			
		    	}
				
				
		    }
			
			//remove the last new line character
			if (tokenList.length() > 0 && tokenList.charAt(tokenList.length()-1)=='\n') {
			      tokenList = tokenList.substring(0, tokenList.length()-1);
			    }
			
			//put it to the file
			FileWriter writer = null;
			    try {
			        writer = new FileWriter(inputFilePath+".tok");
			        writer.write(tokenList);
			        writer.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			
		    // line is not visible here.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
			
		
		
	}

}
