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

	public HarvesterUtils() {
		try {
			mmIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:metaMetadata/lom:identifier/lom:catalog/text()=\"oai\"");
			mmIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);

			gIdOaiCatalog = XPath
					.newInstance("//lom:lom/lom:general/lom:identifier/lom:catalog/text()=\"oai\"");
			gIdOaiCatalog.addNamespace(OaiUtils.LOMLOMNS);
		} catch (JDOMException e) {
			// NOOP
		}
	}

	public Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String ctlg) throws IllegalStateException,
			JDOMException {

		String ident = ctlg + ":" + reposIdentifier + ":";

		String loIdent = "";

		// try {

		Element metametadata = JDomUtils.getXpathNode(
				"//lom:lom/lom:metaMetadata", OaiUtils.LOMLOMNS,
				record.getMetadata());

		if (metametadata != null) {
			Element mmIdentifier = metametadata.getChild("identifier",
					OaiUtils.LOMNS);
			if (mmIdentifier != null) {
				if (!(Boolean) mmIdOaiCatalog.selectSingleNode(record
						.getMetadata())) {

					loIdent = mmIdentifier.getChildText("entry",
							mmIdentifier.getNamespace());

					if (loIdent != null) {
						ident = ident.concat(loIdent);

						Element newIdentifier = new Element("identifier",
								OaiUtils.LOMNS);
						metametadata.addContent(0, newIdentifier);

						Element catalog = new Element("catalog", OaiUtils.LOMNS);
						catalog.setText(ctlg);
						newIdentifier.addContent(catalog);

						Element entry = new Element("entry", OaiUtils.LOMNS);
						entry.setText(ident);
						newIdentifier.addContent(entry);
					} else {

						System.err.println("Missing LO Identifier");

					}

				}
			} else {
				// throw new
				// IllegalStateException("The LO has no metaMetadata.identifier set");
			}
		} else {
			// throw new
			// IllegalStateException("The LO has no metaMetadata.identifier set");
		}
		return record;

	}

	public Record addGlobalLOIdentifier(Record record, String reposIdentifier,
			String ctlg) throws IllegalStateException, JDOMException {

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
				Element technical = JDomUtils.getXpathNode(
						"//lom:lom/lom:technical", OaiUtils.LOMLOMNS,
						record.getMetadata());

				if (technical != null) {
					Element location = technical.getChild("location",
							OaiUtils.LOMNS);

					if (location != null) {
						loIdent = location.getText();

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

					}
				}
			}
		} else {

		}

		return record;
	}

}
