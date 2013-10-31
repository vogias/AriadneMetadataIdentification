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

	public Enviroment(String input, String output) throws FileNotFoundException {

		arguments = new Arguments();
		this.input = input;
		this.output = output;

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

			System.err
					.println("Wrong argument on Global LO Identifier declaration.");
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
			System.err
					.println("Wrong argument on Global LOM Identifier declaration.");
			return false;
		}
	}

	private boolean checkInput() {
		String in = getInput();
		File inputFolder = new File(in);

		if (inputFolder.exists() && inputFolder.isDirectory())
			return true;
		else {

			System.err.println("Wrong input folder location.");
			return false;
		}

	}

	private boolean checkOutput() {
		String outputFolder = getOutput();
		File output = new File(outputFolder);

		if (output.exists() && output.isDirectory())
			return true;
		else {

			System.err.println("Wrong output folder location.");
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
