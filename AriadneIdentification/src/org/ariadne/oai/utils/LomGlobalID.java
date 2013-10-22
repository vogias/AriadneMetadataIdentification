/**
 * 
 */
package org.ariadne.oai.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.ariadne.util.IOUtilsv2;
import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.ietf.jgss.Oid;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import enviroment.Enviroment;

/**
 * @author vogias
 * 
 */
public class LomGlobalID {

	/**
	 * @param args
	 * @throws IOException
	 * @throws JDOMException
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		if (args.length != 2) {
			System.err.println("Usage : ");
			System.err
					.println("java -jar LomGlobalID.jar <input folder path> <output folder path>");
			System.exit(-1);
		}

		Enviroment enviroment = new Enviroment(args[0], args[1]);
		Collection<File> xmls = enviroment.getXMLs();
		Iterator<File> iterator = xmls.iterator();

		SAXBuilder builder = new SAXBuilder();
		HarvesterUtils utils = new HarvesterUtils();

		System.out.println("========================================");
		System.out.println("Number of records to Identify:" + xmls.size());
		System.out.println("========================================");

		while (iterator.hasNext()) {

			// System.out.println(iterator.next().getName());
			File xmlFile = iterator.next();
			String parentFolder = xmlFile.getParentFile().getName();
			File parentDest = new File(enviroment.getOutput(), parentFolder);

			if (!parentDest.exists()) {

				if (enviroment.addGlobalLOIdentifier()) {
					System.out
							.println("Creating global LO identifiers for repository:"
									+ parentFolder);
				}
				if (enviroment.addGlobalMetadataIdentifier()) {
					System.out
							.println("Creating global LOM identifiers for repository:"
									+ parentFolder);
				}

				parentDest.mkdir();
			}

			File nFile = new File(parentDest, xmlFile.getName());

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			Record record = new Record();
			record.setMetadata(rootNode);

			String xmlString = JDomUtils.parseXml2string(record.getMetadata()
					.getDocument(), null);

			if (enviroment.addGlobalLOIdentifier()) {
				record = utils.addGlobalLOIdentifier(record, parentFolder);
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			}

			if (enviroment.addGlobalMetadataIdentifier()) {
				record = utils
						.addGlobalMetadataIdentifier(record, parentFolder);
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			}
			OaiUtils.writeStringToFileInEncodingUTF8(xmlString, nFile.getPath());

		}
		System.out.println("========================================");
		System.out.println("Done");
	}

}
