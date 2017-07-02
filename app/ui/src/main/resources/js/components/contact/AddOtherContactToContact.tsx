import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import Collapse from '../Collapse';
import OtherContactTypeSelector from './OtherContactTypeSelector';

export default class AddOtherContactToContact extends React.Component<any, any> {

    static propTypes = {
        contactId: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { account: '', typeId: 0 };
        this.changeAccount = this.changeAccount.bind( this );
        this.changeType = this.changeType.bind( this );
        this.add = this.add.bind( this );
    }

    changeAccount( event: any ): void {
        let account = event.target.value;
        this.setState( { account: account });
    }

    changeType( typeId: number ) {
        this.setState( { typeId: typeId });
    }

    add() {

    }


    render() {
        return (
            <Collapse id="add-other-contact-type" name="Add..." >
                <div className="row">
                    <div className="form-group col-md-2">
                        <label htmlFor="account">Account</label>
                    </div>
                    <div className="form-group col-md-3">
                        <input type="email" className="form-control" id="account" placeholder="Enter account" value={this.state.account} onChange={this.changeAccount} />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="accountType">Account Type</label>
                    </div>
                    <div className="form-group col-md-3">
                        <OtherContactTypeSelector id="accountType" typeId={this.state.typeId} onChange={this.changeType} />
                    </div>
                    <div className="form-group col-md-2">
                        <button className="btn btn-primary" onClick={this.add}>Add...</button>
                    </div>
                </div>
            </Collapse >
        );
    }

}