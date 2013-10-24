/**
 * 
 */
package org.ariadne.oai.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * @author vogias
 * 
 */
public class HashID extends Identification {

	public XPath mmIdOaiCatalog;
	public XPath gIdOaiCatalog;
	String xmlString;

	public HashID() {

		try {

			xmlString = "";
			mmIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog/text()=\"oai\"");
			mmIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);

			gIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:general/lom:identifier/lom:catalog/text()=\"oai\"");
			gIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);
		} catch (JDOMException e) {
			// NOOP
			e.printStackTrace();
		}
	}

	@Override
	public Record addGlobalLOIdentifier(Record record, String reposIdentifier,
			String ctlg) throws IllegalStateException, JDOMException {
		// TODO Auto-generated method stub

		// String ident = ctlg + ":" + reposIdentifier + ":";
		//
		// Element general = JDomUtils.getXpathNode("//lom:lom/lom:general",
		// OaiUtils.LOMLOMNS, record.getMetadata());
		//
		// if (general != null) {
		//
		// if (xmlString.equals(""))
		// xmlString = JDomUtils.parseXml2string(record.getMetadata()
		// .getDocument(), null);
		//
		// ident = ident.concat(createMD5(xmlString));
		//
		// Element newIdentifier = new Element("identifier", OaiUtils.LOMNS);
		// general.addContent(0, newIdentifier);
		//
		// Element catalog = new Element("catalog", OaiUtils.LOMNS);
		// catalog.setText(ctlg);
		// newIdentifier.addContent(catalog);
		//
		// Element entry = new Element("entry", OaiUtils.LOMNS);
		// entry.setText(ident);
		// newIdentifier.addContent(entry);
		//
		// }
		//
		// return record;
		String ident = ctlg + ":" + reposIdentifier + ":";

		String loIdent = "";

		// try {
		Element general = JDomUtils.getXpathNode("//lom:lom/lom:general",
				OaiUtils.LOMLOMNS, record.getMetadata());
		// /lom:lom/lom:general
		if (general != null) {
			Element generalIdentifier = general.getChild("identifier",
					OaiUtils.LOMNS);

			if (generalIdentifier != null) {
				if (!(Boolean) gIdOaiCatalog.selectSingleNode(record
						.getMetadata())) {
					loIdent = generalIdentifier.getChildText("entry",
							generalIdentifier.getNamespace());

					if (loIdent != null) {
						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								OaiUtils.LOMNS);
						general.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMNS);
						entry.setText(ident);
						newIdentifier.addContent(entry);
					} else {
						System.err.println("Missing LOM Identifier");
					}

				}
			} else {

			}
		} else {

		}

		return record;
	}

	private String createMD5(String input) {

		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");

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
		}

	}

	@Override
	public Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String ctlg) throws IllegalStateException,
			JDOMException {
		// TODO Auto-generated method stub
		String ident = ctlg + ":" + reposIdentifier + ":";

		Element metametadata = JDomUtils.getXpathNode(
				"//lom:lom/lom:metaMetadata", OaiUtils.LOMLOMNS,
				record.getMetadata());

		if (metametadata != null) {

			if (xmlString.equals(""))
				xmlString = JDomUtils.parseXml2string(record.getMetadata()
						.getDocument(), null);

			ident = ident.concat(createMD5(xmlString));

			Element newIdentifier = new Element("identifier", OaiUtils.LOMNS);
			metametadata.addContent(0, newIdentifier);

			Element catalog = new Element("catalog", OaiUtils.LOMNS);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry", OaiUtils.LOMNS);
			entry.setText(ident);
			newIdentifier.addContent(entry);

		}
		return record;

	}
}
