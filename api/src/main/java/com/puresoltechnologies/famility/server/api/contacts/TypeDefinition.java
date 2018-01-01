package com.puresoltechnologies.famility.server.api.contacts;

/**
 * This class is a single type definition.
 * 
 * @author Rick-Rainer Ludwig
 */
public class TypeDefinition {

    private final long id;
    private final String name;

    public TypeDefinition(long id, String name) {
	super();
	this.id = id;
	this.name = name;
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

}
