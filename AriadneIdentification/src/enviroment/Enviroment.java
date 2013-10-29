/**
 * 
 */
package enviroment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.ariadne.oai.utils.LomGlobalID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author vogias
 * 
 */
public class Enviroment {

	/**
	 * @param args
	 */
	Arguments arguments;
	String input, output;
	private static final Logger slf4jLogger = LoggerFactory
			.getLogger(Enviroment.class);

	public Enviroment(String input, String output) {
		try {
			arguments = new Arguments();
			this.input = input;
			this.output = output;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the arguments
	 */
	public Arguments getArguments() {
		return arguments;
	}

	/**
	 * @param arguments
	 *            the arguments to set
	 */
	public void setArguments(Arguments arguments) {
		this.arguments = arguments;
	}

	/**
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}

	/**
	 * @param output
	 *            the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}

	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @return the args
	 */
	private Arguments getArgs() {
		return arguments;
	}

	public boolean addGlobalLOIdentifier() {

		String addGLOID = getArgs().getaddGlobalLOIdentifier();
		if (addGLOID.equals("true"))
			return true;
		else if (addGLOID.equals("false"))
			return false;
		else {

			slf4jLogger
					.error("Wrong argument on Global LO Identifier declaration.");
			return false;
		}
	}

	public boolean addGlobalMetadataIdentifier() {

		String addGLOID = getArgs().getaddGlobalMetadataIdentifier();
		if (addGLOID.equals("true"))
			return true;
		else if (addGLOID.equals("false"))
			return false;
		else {
			slf4jLogger
					.error("Wrong argument on Global LOM Identifier declaration.");
			return false;
		}
	}

	private boolean checkInput() {
		String inputFolder = getInput();
		File input = new File(inputFolder);

		if (input.exists() && input.isDirectory())
			return true;
		else {

			slf4jLogger.error("Wrong input folder location.");
			return false;
		}

	}

	private boolean checkOutput() {
		String outputFolder = getOutput();
		File output = new File(outputFolder);

		if (output.exists() && output.isDirectory())
			return true;
		else {

			slf4jLogger.error("Wrong output folder location.");
			return false;
		}

	}

	private boolean checkEnviroment() {
		if (checkOutput() & checkInput())
			return true;
		else
			return false;
	}

	public Collection<File> getXMLs() {

		String[] extensions = { "xml" };

		Collection<File> files = null;

		if (checkEnviroment())
			files = FileUtils.listFiles(new File(getInput()), extensions, true);
		else
			System.exit(-1);
		return files;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// if (args.length != 2) {
		// System.err.println("Usage : ");
		// System.err
		// .println("java -jar Enviroment.jar <input folder path> <output folder path>");
		// System.exit(-1);
		// }
		//
		// Enviroment enviroment = new Enviroment(args[0], args[1]);
		// Collection<File> xmls = enviroment.getXMLs();
		// Iterator<File> iterator = xmls.iterator();
		//
		// while (iterator.hasNext()) {
		//
		// System.out.println(iterator.next().getName());
		// }

	}

}
