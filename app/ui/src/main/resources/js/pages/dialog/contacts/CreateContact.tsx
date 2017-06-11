import * as React from 'react';

import ContactsController from '../../../controller/ContactsController';
import Dialog from '../../../components/dialog/Dialog';

export default class CreateContact extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = { contactName: '', birthday: '' };
        this.handleNameChange = this.handleNameChange.bind( this );
        this.handleBirthdayChange = this.handleBirthdayChange.bind( this );
        this.createContact = this.createContact.bind( this );
        this.cancel = this.cancel.bind( this );
    }

    handleNameChange( event: any ) {
        this.setState( {
            contactName: event.target.value
        });
    }

    handleBirthdayChange( event: any ) {
        this.setState( {
            birthday: event.target.value
        });
    }

    createContact() {
        var component = this;
        ContactsController.createContact( this.state.contactName, this.state.birthday,
            function( response ) { component.props.router.push( '/contacts' ); },
            function( response ) { }
        );
    }

    cancel() {
        this.props.router.push( '/contacts' );
    }

    render() {
        return (
            <Dialog title="Create Contact">
                <form>
                    <div className="form-group">
                        <label htmlFor="ContactName">User name</label>
                        <input type="text" className="form-control" id="ContactName" placeholder="Enter display name" value={this.state.contactName} onChange={this.handleNameChange} />
                    </div>
                    <div className="form-group">
                        <label htmlFor="Birthday">Birthday</label>
                        <input type="date" className="form-control" id="Birthday" placeholder="Enter birthday" value={this.state.birthday} onChange={this.handleBirthdayChange} />
                    </div>
                    <button type="button" className="btn btn-primary" onClick={this.createContact}>Create</button>
                    <button type="button" className="btn btn-secondary" onClick={this.cancel}>Cancel</button>
                </form>
            </Dialog>
        );
    }
}


