package com.puresoltechnologies.lifeassist.app.rest.server.auth;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthElement implements Serializable {

    private static final long serialVersionUID = -2226392567855828037L;

    public static final String AUTH_ID_HEADER = "auth-id";
    public static final String AUTH_TOKEN_HEADER = "auth-token";

    private final String authId;
    private final String authToken;

    @JsonCreator
    public AuthElement( //
	    @JsonProperty("authId") String authId, //
	    @JsonProperty("authToken") String authToken //
    ) {
	this.authId = authId;
	this.authToken = authToken;
    }

    public String getAuthId() {
	return authId;
    }

    public String getAuthToken() {
	return authToken;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((authId == null) ? 0 : authId.hashCode());
	result = prime * result + ((authToken == null) ? 0 : authToken.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	AuthElement other = (AuthElement) obj;
	if (authId == null) {
	    if (other.authId != null)
		return false;
	} else if (!authId.equals(other.authId))
	    return false;
	if (authToken == null) {
	    if (other.authToken != null)
		return false;
	} else if (!authToken.equals(other.authToken))
	    return false;
	return true;
    }

}