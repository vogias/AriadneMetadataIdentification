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
			String reposIdentifier) throws IllegalStateException, JDOMException;

	public abstract Record addGlobalLOIdentifier(Record record,
			String reposIdentifier) throws IllegalStateException, JDOMException;

}
