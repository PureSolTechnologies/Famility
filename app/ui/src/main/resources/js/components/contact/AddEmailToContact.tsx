import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import Collapse from '../Collapse';
import EmailAddressSelector from './EmailAddressTypeSelector';

export default class AddEmailToContact extends React.Component<any, any> {

    static propTypes = {
        contactId: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { address: '', typeId: 0 };
        this.changeAddress = this.changeAddress.bind( this );
        this.changeType = this.changeType.bind( this );
        this.add = this.add.bind( this );
    }

    changeAddress( event: any ): void {
        let address = event.target.value;
        this.setState( { address: address });
    }

    changeType( typeId: number ) {
        this.setState( { typeId: typeId });
    }

    add() {
        
    }


    render() {
        return (
            <Collapse id="add-email-address" name="Add Email Address..." >
                <div className="row">
                    <div className="form-group col-md-6">
                        <label htmlFor="address">New Email Address</label>
                        <input type="email" className="form-control" id="address" placeholder="Enter name email address" value={this.state.address} onChange={this.changeAddress} />
                    </div>
                    <div className="form-group col-md-6">
                        <label htmlFor="addressType">New Email Type</label>
                        <EmailAddressSelector id="addressType" typeId={this.state.typeId} onChange={this.changeType} />
                    </div>
                </div>
                <button className="btn btn-primary" onClick={this.add}>Add...</button>
            </Collapse>
        );
    }

}