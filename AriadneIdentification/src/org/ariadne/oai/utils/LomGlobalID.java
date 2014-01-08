/**
 * 
 */
package org.ariadne.oai.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.Constants;
import enviroment.Enviroment;

/**
 * @author vogias
 * 
 */
public class LomGlobalID {

	private static final Logger slf4jLogger = LoggerFactory
			.getLogger(LomGlobalID.class);

	/**
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static void main(String[] args) throws JDOMException, IOException,
			ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			// System.err.println("Usage : ");
			// System.err
			// .println("java -jar LomGlobalID.jar <input folder path> <output folder path>");
			// System.exit(-1);

			System.err
					.println("Usage : java -jar LomGlobalID.jar <input folder path> <output folder path>");
			System.exit(-1);
		}

		StringBuffer logstring = new StringBuffer();
		Enviroment enviroment = new Enviroment(args[0], args[1]);
		Collection<File> xmls = enviroment.getXMLs();
		Iterator<File> iterator = xmls.iterator();

		SAXBuilder builder = new SAXBuilder();

		Properties props = enviroment.getArguments().getProps();

		String idClass = props.getProperty(Constants.idCreatorClass);
		ClassLoader myClassLoader = ClassLoader.getSystemClassLoader();
		Class myClass = myClassLoader.loadClass(idClass);

		// HarvesterUtils utils = new HarvesterUtils();

		// System.out.println("========================================");
		// System.out.println("Number of records to Identify:" + xmls.size());
		// System.out.println("========================================");

		String catalog = props.getProperty(Constants.catalog);
		System.out.println("Number of records:" + xmls.size());

		boolean flag = false;

		int cnt = 0;
		while (iterator.hasNext()) {
			Object whatInstance = myClass.newInstance();
			Identification id = (Identification) whatInstance;

			// System.out.println(iterator.next().getName());
			File xmlFile = iterator.next();
			String parentFolder = xmlFile.getParentFile().getName();
			File parentDest = new File(enviroment.getOutput(), parentFolder);

			if (!parentDest.exists())
				parentDest.mkdir();

			if (flag == false) {

				Date date = new Date();
				System.out.println("Identifing repository:" + parentFolder);
				logstring.append(" " + parentFolder);

				System.out.println("Identification date:" + date.toString());

				if (enviroment.addGlobalLOIdentifier()) {
					// System.out
					// .println("Creating global LO identifiers for repository:"
					// + parentFolder);

					System.out.println("Creating global LO identifiers");
					logstring.append(" " + "TRUE");
				} else
					logstring.append(" " + "FALSE");

				if (enviroment.addGlobalMetadataIdentifier()) {
					// System.out
					// .println("Creating global LOM identifiers for repository:"
					// + parentFolder);
					System.out.println("Creating global LOM identifiers");
					logstring.append(" " + "TRUE");
				} else
					logstring.append(" " + "FALSE");

				flag = true;

			}

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			Record record = new Record();

			record.setMetadata(rootNode);

			String xmlString = JDomUtils.parseXml2string(record.getMetadata()
					.getDocument(), null);

			if (enviroment.addGlobalLOIdentifier()) {
				record = id
						.addGlobalLOIdentifier(record, parentFolder, catalog);

				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			}

			File nFile;
			String oaiID = xmlFile.getName();
			oaiID = oaiID.replace(".xml", "");

			if (enviroment.addGlobalMetadataIdentifier()) {
				record = id.addGlobalMetadataIdentifier(record, parentFolder,
						catalog, oaiID);
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);
				String globalLOMIdentifier = id.getGlobalLOMIdentifier();
				// System.out.println("MID:" + globalLOMIdentifier);
				globalLOMIdentifier = globalLOMIdentifier.replace("/", ".");
				globalLOMIdentifier = globalLOMIdentifier.replace(":", ".");
				nFile = new File(parentDest, globalLOMIdentifier + ".xml");

			} else
				nFile = new File(parentDest, xmlFile.getName());

			if (!id.getGlobalLOIdentifier().equals("")) {
				OaiUtils.writeStringToFileInEncodingUTF8(xmlString,
						nFile.getPath());
				cnt++;
			}

		}
		logstring.append(" " + cnt);

		slf4jLogger.info(logstring.toString());

		// System.out.println("========================================");
		// System.out.println("Done");
	}
}
