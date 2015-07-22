package scriptrunner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.bsf.BSFException;
import org.jruby.Ruby;
import org.jruby.javasupport.JavaUtil;
import org.jruby.runtime.builtin.IRubyObject;

public class ScriptRunner {
	public static void main(String[] args) {
		try {
			useJrubyEngine();
		} catch ( IOException | ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void useJrubyEngine() throws FileNotFoundException, ScriptException {
		// get jruby engine
		ScriptEngine jruby = new ScriptEngineManager().getEngineByName("jruby");
		
		StringWriter sw = new StringWriter();
//	    PrintWriter pw = new PrintWriter(sw);
	    
		jruby.getContext().setWriter(sw);
		// process a ruby file
		jruby.eval(new BufferedReader(new FileReader("src/rbscript/rbscript.rb")));

		// call a method defined in the ruby source
		jruby.put("number", 6);

		Long fact = (Long) jruby.eval("fact($number)");
		
		System.out.println("output: " + sw.getBuffer().toString());
		
		System.out.println("result: " + fact);
		try {
			sw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void useJruby() throws BSFException, IOException {

		String dir = "src/rbscript/";
		Ruby runtime = Ruby.newInstance();
		String content = getFileContents(dir + "rbscript.rb");

		IRubyObject rawRuby = runtime.evalScriptlet(content);
		IRubyObject ruby = rawRuby.callMethod(runtime.getCurrentContext(), "fact",
				JavaUtil.convertJavaToRuby(runtime, 6));
		
		int c;
		InputStream is = runtime.getInputStream();
		while ((c = is.read()) != -1) {
			System.out.write(c);;
        }
//		is.read();
//		StringWriter writer = new StringWriter();
//		IOUtils.copy(is, writer, "UTF-8");
//		String theString = writer.toString();
//		
//		System.out.println(theString);
	}

	private static String getFileContents(String filename) throws IOException {
		FileReader in = new FileReader(filename);
		return org.apache.bsf.util.IOUtils.getStringFromReader(in);
	}
}
