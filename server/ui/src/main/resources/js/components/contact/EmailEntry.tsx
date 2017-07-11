import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';

const {TrashIcon } = require( 'react-octicons' );


export default class AddEmailToContact extends React.Component<any, any> {

    static propTypes = {
        contactId: React.PropTypes.number.isRequired,
        emailId: React.PropTypes.number.isRequired,
        email: React.PropTypes.number.isRequired,
        type: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
    }

    render() {
        return (
            <div className="row">
                <div className="form-group col-md-6">
                    {this.props.email}
                </div>
                <div className="form-group col-md-5">
                    {this.props.type}
                </div>
                <div className="form-group col-md-1">
                    <button className="btn btn-secondary"><TrashIcon /></button>
                </div>
            </div>
        );
    }

}