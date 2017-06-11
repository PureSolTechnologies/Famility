import React from 'react';
import { Link } from 'react-router';
import { SettingsIcon, TrashcanIcon } from 'react-octicons';

import ContactsController from '../controller/ContactsController';

export default class Contacts extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { contacts: [] };
        this.deleteContact = this.deleteContact.bind( this );
    }

    componentDidMount() {
        var component = this;
        ContactsController.getContacts(
            function( contacts ) {
                component.setState( { contacts: contacts });
            },
            function( response ) { }
        );
    }

    deleteContact( contact ) {
        var component = this;
        ContactsController.deleteContact( contact.id,
            function( response ) { component.componentDidMount(); },
            function( response ) { }
        );
    }
    render() {
        var rows = [];
        var contacts = this.state.contacts;
        for ( var i = 0; i < contacts.length; i++ ) {
            const contact = contacts[i];
            if ( contact.birthday != null ) {
                rows.push(
                    <tr key={contact.id}>
                        <td>{contact.name}</td>
                        <td>{contact.birthday.year + "-" + contact.birthday.month + "-" + contact.birthday.dayOfMonth}</td>
                        <td><button className="btn btn-secondary"><SettingsIcon />&nbsp;Edit...</button>&nbsp;<button className="btn btn-secondary" onClick={() => this.deleteContact( contact )} ><TrashcanIcon />&nbsp;Delete</button></td>
                    </tr>
                );
            } else {
                rows.push(
                    <tr key={contact.id}>
                        <td>{contact.name}</td>
                        <td></td>
                        <td><button className="btn btn-secondary"><SettingsIcon />&nbsp;Edit...</button>&nbsp;<button className="btn btn-secondary" onClick={() => this.deleteContact( contact )} ><TrashcanIcon />&nbsp;Delete</button></td>
                    </tr>
                );
            }
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Contacts</h1>
                    <Link className="btn btn-primary" to="/dialog/contacts/create" role="button">Create Contact...</Link>
                    <table className="table table-hover">
                        <thead className="thead-default">
                            <tr>
                                <th>Name</th>
                                <th>Birthday</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {rows}
                        </tbody>
                    </table>
                </div>
            </div >
        );
    }
}
