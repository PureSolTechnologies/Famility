import React from 'react';
import { browserHistory } from 'react-router';

import store from '../flux/Store';
import { login, logout } from '../flux/LoginActions';

export default class LoginPage extends React.Component {

    constructor( props ) {
        super( props );

        // This binding is necessary to make `this` work in the callback
        this.login = this.login.bind( this );
    }

    login() {
        store.dispatch( login( 'user' ) );
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
