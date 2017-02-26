import React from 'react';

import store from '../flux/Store';
import logout from '../flux/LoginActions';
import restController from './RESTController';

export default class LoginController extends React.Component {

    static isLoggedIn() {
        return store.getState().login;
    }

    static login( email, password, successfulCallback, errorCallback ) {
        restController.put( '/login/authenticate',
            {
                email: email,
                password: password
            },
            null,
            successfulCallback, errorCallback
        );
    }

    static logout() {
        store.dispatch( logout() );
    }

    static requireAuth( nextState, replaceState ) {
        if ( !store.getState().login ) {
            const redirect = escape( nextState.location.pathname );
            replaceState( `/login/${encodeURIComponent( redirect )}` );
        }
    }


}