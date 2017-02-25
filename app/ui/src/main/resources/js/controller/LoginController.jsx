import React from 'react';

import store from '../flux/Store';
import logout from '../flux/LoginActions';

export default class LoginController extends React.Component {

    static isLoggedIn() {
        return store.getState().login;
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