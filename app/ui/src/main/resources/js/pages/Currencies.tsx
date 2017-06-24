import * as React from 'react';

import FinanceController from '../controller/FinanceController';

export default class Plugins extends React.Component<any, any> { 

    constructor( props: any ) {
        super( props );
        this.state = {
            currencies: []
        }
    }

    componentDidMount() {
        var component = this;
        FinanceController.getCurrencies(
            function( currencies ) {
                component.setState( { currencies: currencies });
            },
            function( response ) { }
        );
    }

    render() {
        var rows: any[] = [];
        if ( this.state.currencies ) {
            let currencies: any[] = this.state.currencies;
            currencies.sort( function( a, b ) { return a.code.localeCompare( b.code ); })
            for ( var i = 0; i < currencies.length; i++ ) {
                var currency = currencies[i];
                rows.push(
                    <tr key={i}>
                        <th>{currency.code}</th>
                        <td>{currency.name}</td>
                    </tr>
                );
            }
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Currencies</h1>
                    <table className="table table-hover">
                        <thead className="thead-inverse">
                            <tr>
                                <th>Code</th>
                                <th>Name</th>
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