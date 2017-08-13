import * as  React from 'react';

import ContactsController from '../../controller/ContactsController';
import TypeDefinition from '../../models/contact/TypeDefinition';

export default class EmailAddressTypeSelector extends React.Component<any, any> {

    static propTypes = {
        id: React.PropTypes.string,
        typeId: React.PropTypes.number.isRequired,
        onChange: React.PropTypes.func
    };

    constructor( props: any ) {
        super( props );
        this.state = { typeId: this.props.typeId, types: [] };
        this.onChange = this.onChange.bind( this );
    }

    componentDidMount(): void {
        let component = this;
        ContactsController.getEmailAddressTypes(
            function( types: TypeDefinition[] ) {
                let typeId = types[0] ? types[0].id : '';
                component.setState( { types: types, value: typeId });
            },
            function( resonse ) { }
        );
    }

    onChange( event: any ): void {
        var typeId: number = Number( event.target.value );
        this.setState( {
            typeId: typeId
        });
        if ( this.props.onChange ) {
            this.props.onChange( typeId );
        }
    }

    render(): any {
        var selection: any[] = [];
        for ( var type of this.state.types ) {
            selection.push( <option key={type.id} value={type.id}>{type.name}</option> );
        }
        return (
            <select id={this.props.id} className="form-control" value={this.state.typeId} onChange={this.onChange} >
                {selection}
            </select>
        );
    }
}