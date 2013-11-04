/**
 * 
 */
package org.ariadne.oai.utils;

import org.jdom.JDOMException;

/**
 * @author vogias
 * 
 */
public abstract class Identification {

	public abstract Record addGlobalMetadataIdentifier(Record record,
			String reposIdentifier, String catalog)
			throws IllegalStateException, JDOMException;

	public abstract Record addGlobalLOIdentifier(Record record,
			String reposIdentifier, String catalog)
			throws IllegalStateException, JDOMException;

	public abstract String getGlobalLOIdentifier();

	public abstract String getGlobalLOMIdentifier();

}
