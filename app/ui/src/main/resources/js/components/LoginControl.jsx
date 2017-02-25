import React from 'react';
import { Link } from 'react-router';
import { SignInIcon, SignOutIcon } from 'react-octicons';

import { logout } from '../flux/LoginActions';
import store from '../flux/Store';

export default class LoginControl extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            login: store.getState().login
        }
    }

    componentDidMount() {
        store.subscribe(() => this.update() );
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
                <span onClick={this.logout}><SignOutIcon /></span>
            );
        } else {
            return (
                <Link className="nav-link" to="/login"><SignInIcon /></Link>
            );
        }
    }
}