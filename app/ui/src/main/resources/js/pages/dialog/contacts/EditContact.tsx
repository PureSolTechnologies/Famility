import * as React from 'react';

import ContactsController from '../../../controller/ContactsController';
import Dialog from '../../../components/dialog/Dialog';
import Loading from '../../../components/Loading';
import CalendarDay from '../../../models/calendar/CalendarDay';
import Collapse from '../../../components/Collapse';

export default class EditContact extends React.Component<any, any> {

    private id: number;

    constructor( props: any ) {
        super( props );
        this.id = this.props.params.id;
        this.state = { contact: null };
        this.close = this.close.bind( this );
    }

    componentDidMount() {
        const component = this;
        ContactsController.getContact( this.id,
            function( contact: any ) {
                component.setState( { contact: contact });
            },
            function( response: XMLHttpRequest ) { }
        );
    }
    close() {
        this.props.router.push( '/contacts' );
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
                <hr />
                <h3>Emails</h3>
                <Collapse id="add-email-address" name="Add Email Address..." >
                    <div className="row">
                        <div className="form-group col-md-6">
                            <label htmlFor="newEmailAddress">New Email Address</label>
                            <input type="text" className="form-control" id="newEmailAddress" placeholder="Enter name email address" value={this.state.newEmailAddress} />
                        </div>
                        <div className="form-group col-md-6">
                            <label htmlFor="newEmailAddressType">New Email Type</label>
                            <select id="newEmailAddressType" className="form-control" value={this.state.newEmailAddressType}>
                                <option>test</option>
                            </select>
                        </div>
                    </div>
                    <button className="btn btn-primary">Add New Email Address</button>
                </Collapse>
                <hr />
                <h3>Postal Addresses</h3>
                <Collapse id="add-postal-address" name="Add Postal Address..." >
                </Collapse>
                <hr />
                <h3>Phones</h3>
                <Collapse id="add-phone-number" name="Add Phone Number..." >
                </Collapse>
                <hr />
                <h3>Other Channels</h3>
                <hr />
                <h3>Bank Accounts</h3>
                <Collapse id="add-bank-account" name="Add Bank Account..." >
                </Collapse>
                <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
            </Dialog>
        );
    }
}


