package org.ariadne.oai.utils;

import org.jdom.Element;

public class Record {

	protected Element metadata;
	protected String oaiIdentifier;

	public Element getMetadata() {
		return metadata;
	}

	public void setMetadata(Element metadata) {
		this.metadata = metadata;
	}

}
