/**
 * 
 */
package enviroment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Constants;

/**
 * @author vogias
 * 
 */
public class Arguments {

	Properties props;
	private static final Logger slf4jLogger = LoggerFactory
			.getLogger(Arguments.class);

	public Arguments() {
		props = new Properties();
		try {
			props.load(new FileInputStream("configure.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			slf4jLogger.error(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			slf4jLogger.error(e.getMessage());
		}
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
