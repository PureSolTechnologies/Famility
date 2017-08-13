import React from 'react';

import restController from '../controller/RESTController';

export default class MetricsDashboard extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { test: 1 };
    }

    componentDidMount() {
        var component = this;
        var request = new XMLHttpRequest();
        var url = "http://localhost:8080/metrics";
        request.open( "GET", url );
        request.addEventListener( 'load',
            function( event ) {
                component.setState( event.target );
            }
        );
        request.addEventListener( 'error',
            function( event ) {
                component.setState( { message: "ERROR" });
            }
        );
        request.send();
    }

    render() {
        return <div className="row">
            <div className="col-md-12">
                <h1>Metrics Dashboard</h1>
                <p>
                    {JSON.stringify( this.state )}
                </p>
            </div>
        </div >;
    }
}
