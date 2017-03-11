import React from 'react';

export class RESTController extends React.Component {

    baseURL = 'http://localhost:8080/rest';

    constructor(props) {
        super(props);
    }

    createRequest(type, path, headers, successCallback, errorCallback) {
        var request = new XMLHttpRequest();
        var url = this.baseURL + path;
        request.open( type, url );
        for ( var key in headers ) {
            if ( headers.hasOwnProperty( key ) ) {
                request.setRequestHeader( key, headers[key] );
            }
        }
        request.addEventListener( 'load', 
            function( event ) { 
                successCallback( event.target ); 
            }
        );
        request.addEventListener( 'error', 
            function( event ) { 
                errorCallback( event.target );
            }
        );
        return request;
    }

    GET( path, headers, successCallback, errorCallback ) {
        var request = this.createRequest('GET', path, headers, successCallback, errorCallback);
        request.send();
    }

    PUT( path, headers, entity, successCallback, errorCallback ) {
        var request = this.createRequest('PUT', path, headers, successCallback, errorCallback);
        request.send(JSON.stringify(entity));
    }

    POST( path, headers, entity, successCallback, errorCallback ) {
        var request = this.createRequest('POST', path, headers, successCallback, errorCallback);
        request.send(JSON.stringify(entity));
    }

    DELETE( path, headers, successCallback, errorCallback ) {
        var request = this.createRequest('DELETE', path, headers, successCallback, errorCallback);
        request.send();
    }

}

const restController = new RESTController();
export default restController;