import ServerConfiguration from '../config/ServerConfiguration';

export class RESTController {

    baseURL = 'http://localhost:8080/rest';

    server: ServerConfiguration;

    constructor() {
        var component = this;
        var request = new XMLHttpRequest();
        request.open( 'GET', '/conf/server.json', false );
        request.addEventListener( 'load',
            function( event: any ) {
                component.server = JSON.parse( event.target.response );
            }
        );
        request.addEventListener( 'error',
            function( event: any ) {
                component.server = new ServerConfiguration();
            }
        );
        request.send();
    }

    createRequest( type: string, path: string, headers: any, successCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): XMLHttpRequest {
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

    GET( path: string, headers: any, successCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): void {
        var request = this.createRequest( 'GET', path, headers, successCallback, errorCallback );
        request.send();
    }

    PUT( path: string, headers: any, entity: any, successCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): void {
        var request = this.createRequest( 'PUT', path, headers, successCallback, errorCallback );
        request.send( JSON.stringify( entity ) );
    }

    POST( path: string, headers: any, entity: any, successCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): void {
        var request = this.createRequest( 'POST', path, headers, successCallback, errorCallback );
        request.send( JSON.stringify( entity ) );
    }

    DELETE( path: string, headers: any, successCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): void {
        var request = this.createRequest( 'DELETE', path, headers, successCallback, errorCallback );
        request.send();
    }

}

const restController = new RESTController();
export default restController;