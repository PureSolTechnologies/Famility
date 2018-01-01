import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import Collapse from '../Collapse';
import PostalAddressTypeSelector from './PostalAddressTypeSelector';

export default class AddPostalAddressToContact extends React.Component<any, any> {

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
            <Collapse id="add-postal-address-type" name="Add..." >
                <div className="row">
                    <div className="form-group col-md-2">
                        <label htmlFor="address">Email Address</label>
                    </div>
                    <div className="form-group col-md-3">
                        <input type="email" className="form-control" id="address" placeholder="Enter name email address" value={this.state.address} onChange={this.changeAddress} />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="addressType">Email Type</label>
                    </div>
                    <div className="form-group col-md-3">
                        <PostalAddressTypeSelector id="addressType" typeId={this.state.typeId} onChange={this.changeType} />
                    </div>
                    <div className="form-group col-md-2">
                        <button className="btn btn-primary" onClick={this.add}>Add...</button>
                    </div>
                </div>
            </Collapse >
        );
    }

}