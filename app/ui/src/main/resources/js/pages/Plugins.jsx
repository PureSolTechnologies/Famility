import React from 'react';

import restController from '../controller/RESTController';

export default class Plugins extends React.Controller {

    constructor( props ) {
        super( props );
        this.state = { plugins: [] };
    }

    componentDidMount() {
        restController.get('/plugins', null, 
            function(response) {
                event
            }, 
            function(response){}
        );
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Plugins</h1>
                </div>
            </div >
        );
    }
}
