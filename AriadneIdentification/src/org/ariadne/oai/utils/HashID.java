/**
 * 
 */
package org.ariadne.oai.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;

import constants.Constants;

/**
 * @author vogias
 * 
 */
public class HashID extends Identification {

	public XPath mmIdOaiCatalog;
	public XPath gIdOaiCatalog;
	String xmlString;

	String gLOID, gLOMID;
	private String ident;

	public HashID() {

		try {

			xmlString = "";
			gLOID = "";
			gLOMID = "";

			mmIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog/text()=\"oai\"");
			mmIdOaiCatalog.addNamespace(Namespace.NO_NAMESPACE);

			gIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:general/lom:identifier/lom:catalog/text()=\"oai\"");
			gIdOaiCatalog.addNamespace(Namespace.NO_NAMESPACE);
		} catch (JDOMException e) {
			// NOOP
			e.printStackTrace();
		}
	}

	@Override
	public Record addGlobalLOIdentifier(Record record, String reposIdentifier,
			String ctlg) throws IllegalStateException, JDOMException {
		// TODO Auto-generated method stub

		String ident = ctlg + "_" + reposIdentifier + "_";

		String loIdent = "";

		// try {
		Element general = JDomUtils.getXpathNode("//lom:lom/lom:general",
				Namespace.NO_NAMESPACE, record.getMetadata());

		// /lom:lom/lom:general
		if (general != null) {
			Element generalIdentifier = general.getChild("identifier",
					Namespace.NO_NAMESPACE);

			if (generalIdentifier != null) {
				if (!(Boolean) gIdOaiCatalog.selectSingleNode(record
						.getMetadata())) {
					loIdent = generalIdentifier.getChildText("entry",
							generalIdentifier.getNamespace());

					if (loIdent != null) {
						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								Namespace.NO_NAMESPACE);
						general.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", Namespace.NO_NAMESPACE);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", Namespace.NO_NAMESPACE);
						ident = ident.replace("/", ".");
						ident = ident.replace(":", ".");
						entry.setText(ident);
						newIdentifier.addContent(entry);

						gLOID = ident;
					} else {

						System.err.println("Missing LOM Identifier");
					}

				}
			} else {
				Element technical = JDomUtils.getXpathNode(
						"//lom:lom/lom:technical", Namespace.NO_NAMESPACE,
						record.getMetadata());

				if (technical != null) {
					Element location = technical.getChild("location",
							Namespace.NO_NAMESPACE);

					if (location != null) {
						loIdent = location.getText();

						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								Namespace.NO_NAMESPACE);
						general.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", Namespace.NO_NAMESPACE);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", Namespace.NO_NAMESPACE);
						ident = ident.replace("/", ".");
						ident = ident.replace(":", ".");
						entry.setText(ident);
						newIdentifier.addContent(entry);

						gLOID = ident;

					}
				}
			}
		} else {

		}

		return record;
	}

	private String createHash(String input) {

		MessageDigest md;

		try {
			Properties props = new Properties();
			props.load(new FileInputStream("configure.properties"));
			String method = props.getProperty(Constants.hMethod);
			md = MessageDigest.getInstance(method);// MD5

			if (!input.equals("")) {
				input = input.trim();
				md.update(input.getBytes());

				byte byteData[] = md.digest();

				// convert the byte to hex format method 1
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < byteData.length; i++) {
					sb.append(Integer
							.toString((byteData[i] & 0xff) + 0x100, 16)
							.substring(1));

				}

				return sb.toString();
			} else
				return "noID";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return "noID";
		} catch (FileNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			return "noID";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "noID";
		}

	}

	@Override
	public Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String ctlg, String oaiID)
			throws IllegalStateException, JDOMException {
		// TODO Auto-generated method stub
		String ident = ctlg + "_" + reposIdentifier + "_";

		Element metametadata = JDomUtils.getXpathNode(
				"//lom:lom/lom:metaMetadata", Namespace.NO_NAMESPACE,
				record.getMetadata());

		if (metametadata != null) {

			if (xmlString.equals(""))
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			ident = ident.concat(createHash(xmlString));

			Element newIdentifier = new Element("identifier", Namespace.NO_NAMESPACE);
			metametadata.addContent(0, newIdentifier);

			Element catalog = new Element("catalog",Namespace.NO_NAMESPACE);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry", Namespace.NO_NAMESPACE);
			ident = ident.replace("/", ".");
			ident = ident.replace(":", ".");
			entry.setText(ident);
			newIdentifier.addContent(entry);
			gLOMID = ident;

		} else {
			if (xmlString.equals(""))
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			ident = ident.concat(createHash(xmlString));

			Element lom = JDomUtils.getXpathNode("//lom:lom",
					Namespace.NO_NAMESPACE, record.getMetadata());
			metametadata = new Element("metaMetadata", Namespace.NO_NAMESPACE);
			lom.addContent(2, metametadata);

			Element newIdentifier = new Element("identifier", Namespace.NO_NAMESPACE);
			metametadata.addContent(0, newIdentifier);

			Element catalog = new Element("catalog", Namespace.NO_NAMESPACE);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry", Namespace.NO_NAMESPACE);
			ident = ident.replace("/", ".");
			ident = ident.replace(":", ".");
			entry.setText(ident);
			newIdentifier.addContent(entry);
			gLOMID = ident;
		}
		return record;

	}

	@Override
	public String getGlobalLOIdentifier() {
		// TODO Auto-generated method stub
		return gLOID;
	}

	@Override
	public String getGlobalLOMIdentifier() {
		// TODO Auto-generated method stub
		return gLOMID;
	}
}
