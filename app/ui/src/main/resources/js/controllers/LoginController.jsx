import React from 'react';

import { browserHistory } from 'react-router';

var loggedIn = false;

export default class LoginController extends React.Component {

    static isLoggedIn() {
        return loggedIn;
    }
    
    static requireAuth( nextState, replaceState ) {
        if ( !loggedIn ) {
            const redirect = escape(nextState.location.pathname);
            replaceState( `/login/${encodeURIComponent(redirect)}` );
        }
    }

    constructor(props) {
        super(props);
        
        // This binding is necessary to make `this` work in the callback
        this.login = this.login.bind(this);
    }
    
    login() {
        loggedIn = true;
        const destination = this.props.params.redirect ? this.props.params.redirect : "";
        browserHistory.replace( destination );
    }

    render() {
        return <div className="row">
            <div className="col-md-12">
                <h1>Login</h1>
                <button onClick={this.login}>Login...</button>
            </div>
        </div>;
    }
}
