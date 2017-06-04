import * as React from 'react';
import { Link } from 'react-router';
const { SignInIcon, SignOutIcon } = require( 'react-octicons' );

import { logout } from '../flux/LoginActions';
import store from '../flux/Store';

export default class LoginControl extends React.Component<any, any> {

    unsubscribeStore: any = null;

    constructor( props: any ) {
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
        if ( ( !this.state.login ) || ( this.state.login.name != loginState.name ) ) {
            this.setState( { login: loginState });
        }
    }

    logout() {
        store.dispatch( logout() );
    }

    render() {
        if ( this.state.login ) {
            return (
                <span onClick={this.logout}>Hi, {this.state.login.name} !<SignOutIcon /></span>
            );
        } else {
            return (
                <Link className="nav-link" to="/login"><SignInIcon /></Link>
            );
        }
    }
}