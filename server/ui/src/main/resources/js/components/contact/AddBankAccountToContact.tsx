import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import Collapse from '../Collapse';
import BankAccountTypeSelector from './BankAccountTypeSelector';

export default class AddBankAccountToContact extends React.Component<any, any> {

    static propTypes = {
        contactId: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { iban: '', bic: '', accountNumber: '', blz: '', typeId: 0 };
        this.changeIBAN = this.changeIBAN.bind( this );
        this.changeBIC = this.changeBIC.bind( this );
        this.changeAccountNumber = this.changeAccountNumber.bind( this );
        this.changeBLZ = this.changeBLZ.bind( this );
        this.changeType = this.changeType.bind( this );
        this.add = this.add.bind( this );
    }

    changeIBAN( event: any ): void {
        let iban = event.target.value;
        this.setState( { iban: iban });
    }

    changeBIC( event: any ): void {
        let bic = event.target.value;
        this.setState( { bic: bic });
    }

    changeAccountNumber( event: any ): void {
        let accountNumber = event.target.value;
        this.setState( { accountNumber: accountNumber });
    }

    changeBLZ( event: any ): void {
        let blz = event.target.value;
        this.setState( { blz: blz });
    }

    changeType( typeId: number ) {
        this.setState( { typeId: typeId });
    }

    add() {

    }

    render() {
        return (
            <Collapse id="add-bank-account-type" name="Add..." >
                <div className="row">
                    <div className="form-group col-md-6">
                        <label htmlFor="iban">IBAN</label>
                        <input type="text" className="form-control" id="iban" placeholder="IBAN (Internation Banking Account Number)" value={this.state.iban} onChange={this.changeIBAN} />
                    </div>
                    <div className="form-group col-md-6">
                        <label htmlFor="bic">BIC</label>
                        <input type="text" className="form-control" id="bic" placeholder="BIC (Bank Identification Code)" value={this.state.bic} onChange={this.changeBIC} />
                    </div>
                    <div className="form-group col-md-6">
                        <label htmlFor="accountNumber">Account Number</label>
                        <input type="text" className="form-control" id="accountNumber" placeholder="Account Number (before Swift)" value={this.state.accountNumber} onChange={this.changeAccountNumber} />
                    </div>
                    <div className="form-group col-md-6">
                        <label htmlFor="blz">BLZ</label>
                        <input type="text" className="form-control" id="blz" placeholder="BLZ (Bankleitzahl / before Swift)" value={this.state.blz} onChange={this.changeBLZ} />
                    </div>
                    <div className="form-group col-md-6">
                        <label htmlFor="accountType">Bank Account Type</label>
                        <BankAccountTypeSelector id="accountType" typeId={this.state.typeId} onChange={this.changeType} />
                    </div>
                    <div className="form-group col-md-6">
                        <button className="btn btn-primary" onClick={this.add}>Add...</button>
                    </div>
                </div>
            </Collapse >
        );
    }

}