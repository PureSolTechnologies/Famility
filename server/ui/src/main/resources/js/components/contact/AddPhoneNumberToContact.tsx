import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import Collapse from '../Collapse';
import PhoneNumberTypeSelector from './PhoneNumberTypeSelector';

export default class AddPhoneNumberToContact extends React.Component<any, any> {

    static propTypes = {
        contactId: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { countryCode: '', identificationCode: '', subscriberNumber: '', typeId: 0 };
        this.changeCountryCode = this.changeCountryCode.bind( this );
        this.changeIdentificationCode = this.changeIdentificationCode.bind( this );
        this.changeSubscriberNumber = this.changeSubscriberNumber.bind( this );
        this.changeType = this.changeType.bind( this );
        this.add = this.add.bind( this );
    }

    changeCountryCode( event: any ): void {
        let countryCode = event.target.value;
        this.setState( { countryCode: countryCode });
    }

    changeIdentificationCode( event: any ): void {
        let identificationCode = event.target.value;
        this.setState( { identificationCode: identificationCode });
    }

    changeSubscriberNumber( event: any ): void {
        let subscriberNumber = event.target.value;
        this.setState( { subscriberNumber: subscriberNumber });
    }

    changeType( typeId: number ) {
        this.setState( { typeId: typeId });
    }

    add() {

    }

    render() {
        return (
            <Collapse id="add-phone-number-type" name="Add..." >
                <div className="row">
                    <div className="form-group col-md-2">
                        <label htmlFor="countryCode">Country Code</label>
                        <input type="text" className="form-control" id="countryCode" placeholder="Enter country code" value={this.state.countryCode} onChange={this.changeCountryCode} />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="identificationCode">Identification Code (City / Network)</label>
                        <input type="text" className="form-control" id="identificationCode" placeholder="Enter identification code" value={this.state.identificationCode} onChange={this.changeIdentificationCode} />
                    </div>
                    <div className="form-group col-md-4">
                        <label htmlFor="subscriberNumber">Subscriber Number</label>
                        <input type="text" className="form-control" id="subscriberNumber" placeholder="Enter subscriber number" value={this.state.subscriberNumber} onChange={this.changeSubscriberNumber} />
                    </div>
                    <div className="form-group col-md-2">
                        <label htmlFor="addressType">Phone Number Type</label>
                        <PhoneNumberTypeSelector id="addressType" typeId={this.state.typeId} onChange={this.changeType} />
                    </div>
                    <div className="form-group col-md-2">
                        <button className="btn btn-primary" onClick={this.add}>Add...</button>
                    </div>
                </div>
            </Collapse >
        );
    }

}