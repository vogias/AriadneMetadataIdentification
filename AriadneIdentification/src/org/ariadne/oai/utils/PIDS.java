package org.ariadne.oai.utils;

import java.util.Properties;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.grnet.pids.Handle;
import org.jdom.Element;
import org.jdom.JDOMException;

import constants.Constants;

public class PIDS extends Identification {

	private static Handle pid;
	private static Properties props;

	String gLOID, gLOMID;
	String xmlString;

	public PIDS() {

		props = LomGlobalID.props;
		pid = setupHandleConnection();
		xmlString = "";

		gLOID = "";
		gLOMID = "";

	}

	private Handle setupHandleConnection() {
		Handle pidInstance = new Handle();
		pidInstance.setUsername(props.getProperty(Constants.pidUsername));
		pidInstance.setPassword(props.getProperty(Constants.pidPassword));
		pidInstance.setContentType("application/json");

		return pidInstance;
	}

	public Record addGlobalMetadataIdentifier(Record record, String reposIdentifier, String ctlg, String oaiID)
			throws IllegalStateException, JDOMException {

		String ident = ctlg + "_" + reposIdentifier + "_";

		String loIdent = "";

		// try {

		Element metametadata = JDomUtils.getXpathNode("//lom:lom/lom:metaMetadata", OaiUtils.LOMLOMNS,
				record.getMetadata());

		if (metametadata != null) {
			Element mmIdentifier = metametadata.getChild("identifier", OaiUtils.LOMLOMNS);

			if (mmIdentifier != null) {

				loIdent = mmIdentifier.getChildText("entry", mmIdentifier.getNamespace());

				if (loIdent != null) {

					if (!loIdent.equals("")) {
						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
						metametadata.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMLOMNS);

						entry.setText(ident);
						newIdentifier.addContent(entry);
						gLOMID = ident;
					} else {
						mmIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
						metametadata.addContent(0, mmIdentifier);

						ident += "_" + oaiID;

						Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						mmIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMLOMNS);

						entry.setText(ident);
						mmIdentifier.addContent(entry);
						gLOMID = ident;
					}
				} else {

					System.err.println("Missing LOM Identifier");

				}

				// }
			} else {

				mmIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
				metametadata.addContent(0, mmIdentifier);

				ident += "_" + oaiID;

				Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
				catalog.setText(ctlg);
				mmIdentifier.addContent(catalog);

				Element entry = new Element("entry", OaiUtils.LOMLOMNS);

				entry.setText(ident);
				mmIdentifier.addContent(entry);
				gLOMID = ident;

			}
		} else {

			ident += "_" + oaiID;

			Element lom = JDomUtils.getXpathNode("//lom:lom", OaiUtils.LOMLOMNS, record.getMetadata());

			metametadata = new Element("metaMetadata", OaiUtils.LOMLOMNS);

			try {
				lom.addContent(metametadata);
			} catch (IndexOutOfBoundsException ex) {
				System.out.println("Fuck");
				System.out.println("Index out of bounds");
			} catch (NullPointerException e) {

				// TODO: handle exception
				System.out.println("Fuck");
				System.out.println("NullPointer exception");
			}

			Element newIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
			metametadata.addContent(0, newIdentifier);

			Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry", OaiUtils.LOMLOMNS);

			entry.setText(ident);
			newIdentifier.addContent(entry);
			gLOMID = ident;
		}
		return record;

	}

	public Record addGlobalLOIdentifier(Record record, String reposIdentifier, String ctlg)
			throws IllegalStateException, JDOMException {

		String ident = ctlg + "_" + reposIdentifier + "_";

		String loIdent = "";

		Element general = JDomUtils.getXpathNode("//lom:lom/lom:general", OaiUtils.LOMLOMNS, record.getMetadata());

		Element teclocation = JDomUtils.getXpathNode("//lom:lom/lom:technical/lom:location", OaiUtils.LOMLOMNS,
				record.getMetadata());

		if (general != null && teclocation != null) {

			loIdent = teclocation.getText();
			loIdent = loIdent.replace("/", ".");
			loIdent = String.valueOf(loIdent.replace(":", ".").hashCode());

			ident = ident.concat(loIdent);

			//pid.deletePID(props.getProperty(Constants.pidapiURL), ident);

			pid.createPIDManual(props.getProperty(Constants.pidapiURL),
					"[{\"type\":\"URL\",\"parsed_data\":\"" + teclocation.getText() + "\"}]", ident);

			
			teclocation.setText(props.getProperty(Constants.pidResolverURL) + ident);
			
			Element newIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
			general.addContent(0, newIdentifier);

			Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry", OaiUtils.LOMLOMNS);

			ident = props.getProperty(Constants.pidResolverURL) + ident;
			entry.setText(ident);
			newIdentifier.addContent(entry);

			gLOID = loIdent;

			

		} else
			System.err.println("Missing LO Location or general section element");

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
