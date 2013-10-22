/**
 * 
 */
package enviroment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import constants.Constants;

/**
 * @author vogias
 * 
 */
public class Arguments {

	Properties props;

	public Arguments() throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new FileInputStream("configure.properties"));
	}

	/**
	 * @return the props
	 */
	public Properties getProps() {
		return props;
	}

	/**
	 * @param props
	 *            the props to set
	 */
	public void setProps(Properties props) {
		this.props = props;
	}

	// public String getInputFolder() {
	// return props.getProperty(Constants.input);
	// }

	// public String getOutputFolder() {
	// return props.getProperty(Constants.output);
	// }

	public String getaddGlobalLOIdentifier() {

		return props.getProperty(Constants.addGlobalLOIdentifier).toLowerCase();

	}

	public String getaddGlobalMetadataIdentifier() {
		return props.getProperty(Constants.addGlobalMetadataIdentifier)
				.toLowerCase();

	}

	// public String getMetadataIDXPath() {
	// return props.getProperty(Constants.metadataID);
	// }
	//
	// public String getLOIDXPath() {
	// return props.getProperty(Constants.loID);
	// }

}
