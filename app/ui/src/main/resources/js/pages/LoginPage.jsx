import React from 'react';
import { browserHistory } from 'react-router';

import LoginController from '../controller/LoginController';
import store from '../flux/Store';
import { login, logout } from '../flux/LoginActions';

export default class LoginPage extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            email: '',
            password: ''
        }
        // This binding is necessary to make `this` work in the callback
        this.handleEmailChange = this.handleEmailChange.bind( this );
        this.handlePasswordChange = this.handlePasswordChange.bind( this );
        this.login = this.login.bind( this );
    }

    handleEmailChange( event ) {
        this.setState( {
            email: event.target.value
        });
    }

    handlePasswordChange( event ) {
        this.setState( {
            password: event.target.value
        });
    }

    login() {
        var controller = this;
        LoginController.login( this.state.email, this.state.password,
            function( response ) {
                if ( response.status == 200 ) {
                    var authId = response.getResponseHeader( "Auth-Id" );
                    var authToken = response.getResponseHeader( "Auth-Token" );
                    store.dispatch( login( { name: controller.state.email, authId: authId, authToken: authToken }) );
                    const destination = controller.props.params.r ? controller.props.params.r : "";
                    browserHistory.replace( destination );
                }
            },
            function( response ) { });
    }

    render() {
        return <div className="row">
            <div className="col-md-12">
                <h1>Login</h1>
                <div className="form-group">
                    <label htmlFor="email">Email address</label>
                    <input type="email" className="form-control" id="email" aria-describedby="emailHelp" placeholder="Enter email" value={this.state.email} onChange={this.handleEmailChange} />
                    <small id="emailHelp" className="form-text text-muted">We'll never share your email with anyone else.</small>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input type="password" className="form-control" id="email" placeholder="Enter password" value={this.state.password} onChange={this.handlePasswordChange} />
                </div>
                <button className="btn btn-primary" onClick={this.login}>Login...</button>
            </div>
        </div>;
    }
}
