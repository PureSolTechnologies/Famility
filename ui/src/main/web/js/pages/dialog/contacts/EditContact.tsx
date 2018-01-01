import * as React from 'react';

import ContactsController from '../../../controller/ContactsController';
import Dialog from '../../../components/dialog/Dialog';
import Loading from '../../../components/Loading';
import CalendarDay from '../../../models/calendar/CalendarDay';
import Collapse from '../../../components/Collapse';
import AddEmailToContact from '../../../components/contact/AddEmailToContact';
import AddPhoneNumberToContact from '../../../components/contact/AddPhoneNumberToContact';
import AddOtherContactToContact from '../../../components/contact/AddOtherContactToContact';
import AddPostalAddressToContact from '../../../components/contact/AddPostalAddressToContact';
import AddBankAccountToContact from '../../../components/contact/AddBankAccountToContact';

export default class EditContact extends React.Component<any, any> {

    private id: number;

    constructor( props: any ) {
        super( props );
        this.id = Number( this.props.params.id );
        this.state = { contact: null };
        this.close = this.close.bind( this );
    }

    componentDidMount() {
        const component = this;
        ContactsController.getContact( this.id,
            function( contact: any ) {
                component.setState( { contact: contact } );
            },
            function( response: XMLHttpRequest ) { }
        );
    }
    close() {
        this.props.router.push( '/contacts' );
    }

    // Component method
    handleFileUpload( e: any ) {
        alert( e );
    }

    render() {
        if ( !this.state.contact ) {
            return (
                < Dialog title="Edit Contact" >
                    <Loading />
                </Dialog >
            );
        }
        const contact = this.state.contact;
        let birthday: string;
        if ( contact.birthday ) {
            birthday = new CalendarDay( contact.birthday.year, contact.birthday.month, contact.birthday.dayOfMonth ).toISOString();
        } else {
            birthday = "";
        }
        return (
            <Dialog title={'Edit Contact: ' + contact.name}>
                <div className="form-group">
                    <label htmlFor="ContactName">User name</label>
                    <input type="text" className="form-control" id="ContactName" placeholder="Enter display name" value={contact.name} />
                </div>
                <div className="form-group">
                    <label htmlFor="Birthday">Birthday</label>
                    <input type="date" className="form-control" id="Birthday" placeholder="Enter birthday" value={birthday} />
                </div>
                <div className="form-group">
                    <label htmlFor="Avatar">Avatar</label>
                    <input type="file" className="form-control" id="Avatar" placeholder="Enter avatar" onChange={this.handleFileUpload} />
                </div>
                <hr />
                <h3>Emails</h3>
                <AddEmailToContact contactId={this.id} />
                <hr />
                <h3>Postal Addresses</h3>
                <AddPostalAddressToContact contactId={this.id} />
                <hr />
                <h3>Phones</h3>
                <AddPhoneNumberToContact contactId={this.id} />
                <hr />
                <h3>Other Channels</h3>
                <AddOtherContactToContact contactId={this.id} />
                <hr />
                <h3>Bank Accounts</h3>
                <AddBankAccountToContact contactId={this.id} />
                <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
            </Dialog>
        );
    }
}


