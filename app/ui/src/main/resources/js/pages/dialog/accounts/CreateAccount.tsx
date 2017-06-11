import * as React from 'react';

import AccountsController from '../../../controller/AccountsController';
import Dialog from '../../../components/dialog/Dialog';

export default class CreateAccount extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = { email: '', password: '', password2: '' };
        this.handleEmailChange = this.handleEmailChange.bind( this );
        this.handlePasswordChange = this.handlePasswordChange.bind( this );
        this.createAccount = this.createAccount.bind( this );
        this.cancel = this.cancel.bind( this );
    }

    handleEmailChange( event: any ): void {
        this.setState( {
            email: event.target.value
        });
    }

    handlePasswordChange( event: any ) {
        var target = event.target;
        if ( target.id === "password" ) {
            this.setState( {
                password: target.value
            });
        } else if ( target.id === "password2" ) {
            this.setState( {
                password2: target.value
            });
        }
    }

    createAccount() {
        var component = this;
        AccountsController.createAccount( this.state.email, this.state.password,
            function( response: XMLHttpRequest ) {
                AccountsController.activateAccount( this.state.email, response.response,
                    function( response: XMLHttpRequest ) {
                        component.props.router.push( '/admin/account' );
                    },
                    function( response: XMLHttpRequest ) { });
            },
            function( response: XMLHttpRequest ) { }
        );
    }

    cancel() {
        this.props.router.push( '/admin/accounts' );
    }

    render() {
        return (
            <Dialog title="Create Account">
                <form>
                    <div className="form-group">
                        <label htmlFor="email">Email</label>
                        <input type="email" className="form-control" id="email" placeholder="Enter email address" value={this.state.email} onChange={this.handleEmailChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input type="password" className="form-control" id="password" placeholder="Enter password" value={this.state.password} onChange={this.handlePasswordChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password2">Password Confirmation</label>
                        <input type="password" className="form-control" id="password2" placeholder="Re-enter password" value={this.state.password2} onChange={this.handlePasswordChange} />
                    </div>
                    <button type="button" className="btn btn-primary" onClick={this.createAccount}>Create</button>
                    <button type="button" className="btn btn-secondary" onClick={this.cancel}>Cancel</button>
                </form>
            </Dialog>
        );
    }
}


