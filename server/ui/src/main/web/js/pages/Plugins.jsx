import React from 'react';

import restController from '../controller/RESTController';

export default class Plugins extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            pluginDescriptions: ''
        }
    }

    componentDidMount() {
        var component = this;
        restController.GET( '/plugins', null,
            function( response ) {
                component.setState( { pluginDescriptions: response.response });
            },
            function( response ) { }
        );
    }

    render() {
        var rows = [];
        if ( this.state.pluginDescriptions ) {
            var pluginDescriptions = JSON.parse( this.state.pluginDescriptions );
            for ( var i = 0; i < pluginDescriptions.length; i++ ) {
                var description = pluginDescriptions[i];
                rows.push(
                    <tr key={i}>
                        <th>{description.name}</th>
                        <td>{description.description}</td>
                    </tr>
                );
            }
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Plugins</h1>
                    <table className="table table-hover">
                        <thead className="thead-inverse">
                            <tr>
                                <th>Plugin</th>
                                <th>Description</th>
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