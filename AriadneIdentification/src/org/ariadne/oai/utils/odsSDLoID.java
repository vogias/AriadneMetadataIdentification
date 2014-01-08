/**
 * 
 */
package org.ariadne.oai.utils;

import org.ariadne.util.JDomUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

/**
 * @author vogias
 * 
 */
public class odsSDLoID extends Identification {

	String gLOID, gLOMID;
	String xmlString;

	private static Namespace odsSD = Namespace
			.getNamespace("ods","http://www.opendiscoveryspace.eu/socialdata/v1/ods");

	public odsSDLoID() {
		xmlString = "";

		gLOID = "";

	}

	@Override
	public Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String catalog, String oaiID)
			throws IllegalStateException, JDOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Record addGlobalLOIdentifier(Record record, String reposIdentifier,
			String catalog) throws IllegalStateException, JDOMException {
		// TODO Auto-generated method stub
		String ident = catalog + "_" + reposIdentifier + "_";

		String loIdent = "";

		// try {

		// Element sdLOIdentifier = (Element) XPath.selectSingleNode(
		// record.getMetadata(), "//tag");

		Element sdLOIdentifier = JDomUtils.getXpathNode("ods:loIdentifier", odsSD,
				record.getMetadata());

		if (sdLOIdentifier != null) {
			loIdent = sdLOIdentifier.getText();
			if (!loIdent.equals("")) {
				ident = ident.concat(loIdent);

				sdLOIdentifier.setText(ident);

				gLOID = ident;
			} else
				System.err.println("no sd lo identifier text");
		}

		else {
			System.err.println("no sd lo identifier");
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
		return null;
	}

}
