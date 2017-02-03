package TLCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
*
* @author Tuan Dinh, Xue Qin
*/
public class Scanner {
	public static HashMap<String, String> OPERATOR = new HashMap<String, String>();
	public static HashMap<String, String> KEYWORDS = new HashMap<String, String>();
	public static HashMap<String, String> PROCEDURES = new HashMap<String, String>();
	public Scanner() {
		
	}
	
	public static void loadDictionary(){
		
		OPERATOR.put("(", "LP");
		OPERATOR.put(")", "RP");
		OPERATOR.put(":=", "ASGN");
		OPERATOR.put(";", "SC");
		OPERATOR.put("*", "MULTIPLICATIVE");
		OPERATOR.put("div", "MULTIPLICATIVE");
		OPERATOR.put("mod", "MULTIPLICATIVE");
		OPERATOR.put("+", "ADDITIVE");
		OPERATOR.put("-", "ADDITIVE");
		String[] list = {"=","!=","<",">","<=",">="};
		for (String s: list){
			OPERATOR.put(s, "COMPARE");
		}
		String[] key = {"if","then", "else", "begin", "end", "while", "do", "program", "var", "as", "int", "bool"};
		for (String s: key){
			KEYWORDS.put(s, s.toUpperCase());
		}
		
		PROCEDURES.put("writeInt", "WRITEINT");
		PROCEDURES.put("readInt", "READINT");
	}
	
	public static boolean isOperator(String key){
		return OPERATOR.containsKey(key);
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
	
	public static boolean isSeparator(String key){
		if (key.isEmpty() || key.equalsIgnoreCase(";"))
			return true;
		else
			return false;
	}
	
	
	public static void scan (String inputFilePath){
		loadDictionary();
		
		try(BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    	
		    	String[] list = line.split("\\s");
		    	
		    	for (String token: list){
		    		if (isComment(token))
						break;
		    		else if (isSeparator(token))
		    			;  //do nothing
		    		else if (isBoollit(token))
		    			System.out.println("boollit"+ "(" + token +")");		    		
		    		else if (isNumber(token))
		    			System.out.println("num"+ "(" + token +")");
		    		else if (isKeyword(token))
		    			System.out.println(KEYWORDS.get(token));
		    		else if (isOperator(token))
		    			System.out.println(OPERATOR.get(token) + "("+token+")");
		    		else if (isProcedure(token))
		    			System.out.println(PROCEDURES.get(token));
		    		else if (isIdent(token))
		    			System.out.println("indent"+ "(" + token +")");
		    		else {
		    			System.err.println("Illegal token found: " + token);
			    		System.exit(1);
		    		}
		    			
		    			
		    	}
		    }
		    // line is not visible here.
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
			
		
		
	}

}
