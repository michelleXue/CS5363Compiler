package TLCompiler;

/**
*
* @author Tuan Dinh, Xue Qin
*/

public class TLCompiler {

	public static void main(String[] args) {
		String base = "";
		if (args[0]!= null && args[0].endsWith(".tl") || args[0].endsWith(".TL"))
			base = args[0].substring(0, args[0].length() - 3);
		Scanner.scan(base);
	}

}
