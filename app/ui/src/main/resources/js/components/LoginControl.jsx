import React from 'react';
import { Link } from 'react-router';
import { SignInIcon, SignOutIcon } from 'react-octicons';

import { logout } from '../flux/LoginActions';
import store from '../flux/Store';

export default class LoginControl extends React.Component {

    unsubscribeStore = null;
    
    constructor( props ) {
        super( props );
        this.state = {
            login: store.getState().login
        }
    }

    componentDidMount() {
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }
    
    update() {
        const loginState = store.getState().login;
        if ( this.state.login != loginState ) {
            this.setState( { login: loginState });
        }
    }

    logout() {
        store.dispatch( logout() );
    }

    render() {
        if ( this.state.login ) {
            return (
                <span onClick={this.logout}>Hi, {this.state.login}!<SignOutIcon /></span>
            );
        } else {
            return (
                <Link className="nav-link" to="/login"><SignInIcon /></Link>
            );
        }
    }
}