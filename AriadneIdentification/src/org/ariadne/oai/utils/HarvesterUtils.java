/*******************************************************************************
 * Copyright (c) 2008 Ariadne Foundation.
 * 
 * This file is part of Ariadne Harvester.
 * 
 * Ariadne Harvester is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Ariadne Harvester is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with Ariadne Harvester.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package org.ariadne.oai.utils;

import org.ariadne.util.JDomUtils;
import org.ariadne.util.OaiUtils;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

public class HarvesterUtils extends Identification {

	public XPath mmIdOaiCatalog;
	public XPath gIdOaiCatalog;

	String gLOID, gLOMID;
	String xmlString;

	public HarvesterUtils() {
		try {
			xmlString = "";
			mmIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog/text()=\"oai\"");
			mmIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);

			gIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:general/lom:identifier/lom:catalog/text()=\"oai\"");
			gIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);
			gLOID = "";
			gLOMID = "";
		} catch (JDOMException e) {
			// NOOP
		}
	}

	public Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String ctlg, String oaiID)
			throws IllegalStateException, JDOMException {

		String ident = ctlg + "_" + reposIdentifier + "_";

		String loIdent = "";

		// try {

		Element metametadata = JDomUtils.getXpathNode("//lom:lom/lom:metaMetadata",
				OaiUtils.LOMLOMNS, record.getMetadata());

		if (metametadata != null) {
			Element mmIdentifier = metametadata.getChild("identifier",
					OaiUtils.LOMLOMNS);
			// List children = metametadata.getChildren("identifier",
			// OaiUtils.LOMLOMNS);

			// Element mmIdentifier = (Element) children.get(0);

			if (mmIdentifier != null) {
				// if (!(Boolean) mmIdOaiCatalog.selectSingleNode(record
				// .getMetadata())) {

				loIdent = mmIdentifier.getChildText("entry",
						mmIdentifier.getNamespace());

				if (loIdent != null) {

					if (!loIdent.equals("")) {
						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								OaiUtils.LOMLOMNS);
						metametadata.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMLOMNS);
						// ident = ident.replace("/", ".");
						// ident = ident.replace(":", ".");
						entry.setText(ident);
						newIdentifier.addContent(entry);
						gLOMID = ident;
					} else {
						mmIdentifier = new Element("identifier",OaiUtils.LOMLOMNS);
						metametadata.addContent(0, mmIdentifier);

						ident += "_" + oaiID;

						Element catalog = new Element("catalog",OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						mmIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMLOMNS);
						// ident = ident.replace("/", ".");
						// ident = ident.replace(":", ".");
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
				// ident = ident.replace("/", ".");
				// ident = ident.replace(":", ".");
				entry.setText(ident);
				mmIdentifier.addContent(entry);
				gLOMID = ident;

			}
		} else {

			ident += "_" + oaiID;

			Element lom = JDomUtils.getXpathNode("//lom:lom", OaiUtils.LOMLOMNS,
					record.getMetadata());

			// Element lifecycle = JDomUtils.getXpathNode(
			// "//lom/lifeCycle", OaiUtils.LOMLOMNS,
			// record.getMetadata());

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

			// if (lifecycle != null) {
			// try {
			// lom.addContent(4, metametadata);
			// } catch (IndexOutOfBoundsException ex) {
			// lom.addContent(2, metametadata);
			// }
			//
			// } else {
			// lom.addContent(2, metametadata);
			//
			// }

			Element newIdentifier = new Element("identifier", OaiUtils.LOMLOMNS);
			metametadata.addContent(0, newIdentifier);

			Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
			catalog.setText(ctlg);
			newIdentifier.addContent(catalog);

			Element entry = new Element("entry",OaiUtils.LOMLOMNS);
			// ident = ident.replace("/", ".");
			// ident = ident.replace(":", ".");
			entry.setText(ident);
			newIdentifier.addContent(entry);
			gLOMID = ident;
		}
		return record;

	}

	public Record addGlobalLOIdentifier(Record record, String reposIdentifier,
			String ctlg) throws IllegalStateException, JDOMException {

		String ident = ctlg + "_" + reposIdentifier + "_";

		String loIdent = "";

		// try {
		Element general = JDomUtils.getXpathNode("//lom:lom/lom:general",
				OaiUtils.LOMLOMNS, record.getMetadata());

		// /lom/general
		if (general != null) {
			Element generalIdentifier = general.getChild("identifier",
					OaiUtils.LOMLOMNS);

			if (generalIdentifier != null) {

				// if (!(Boolean) gIdOaiCatalog.selectSingleNode(record
				// .getMetadata())) {
				loIdent = generalIdentifier.getChildText("entry",
						generalIdentifier.getNamespace());

				if (loIdent != null) {

					if (!loIdent.equals("")) {

						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								OaiUtils.LOMLOMNS);
						general.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMLOMNS);
						// ident = ident.replace("/", ".");
						// ident = ident.replace(":", ".");
						entry.setText(ident);
						newIdentifier.addContent(entry);
						gLOID = ident;
					} else {
						Element technical = JDomUtils.getXpathNode(
								"//lom:lom/lom:technical", OaiUtils.LOMLOMNS,
								record.getMetadata());

						if (technical != null) {
							Element location = technical.getChild("location",
									OaiUtils.LOMLOMNS);

							if (location != null) {
								loIdent = location.getText();

								ident = ident.concat(loIdent);

								Element newIdentifier = new Element(
										"identifier", OaiUtils.LOMLOMNS);
								general.addContent(0, newIdentifier);

								Element catalog = new Element("catalog",
										OaiUtils.LOMLOMNS);
								catalog.setText(ctlg);
								newIdentifier.addContent(catalog);

								Element entry = new Element("entry",
										OaiUtils.LOMLOMNS);
								// ident = ident.replace("/", ".");
								// ident = ident.replace(":", ".");
								entry.setText(ident);
								newIdentifier.addContent(entry);
								gLOID = loIdent;

							}
						}
					}
				} else {

					System.err.println("Missing LO Identifier");
				}

				// }
			} else {
				Element technical = JDomUtils.getXpathNode("//lom:lom/lom:technical",
						OaiUtils.LOMLOMNS, record.getMetadata());

				if (technical != null) {
					Element location = technical.getChild("location",
							OaiUtils.LOMLOMNS);

					if (location != null) {
						loIdent = location.getText();

						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								OaiUtils.LOMLOMNS);
						general.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMLOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry",OaiUtils.LOMLOMNS);
						// ident = ident.replace("/", ".");
						// ident = ident.replace(":", ".");
						entry.setText(ident);
						newIdentifier.addContent(entry);
						gLOID = loIdent;

					}
				}
			}
		} else {

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
