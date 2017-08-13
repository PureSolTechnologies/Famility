import * as React from 'react';

import FinanceController from '../controller/FinanceController';
import DataTable from '../components/data/DataTable';

import ColumnDefinition from '../components/data/ColumnDefinition';
import RowContainer from '../components/RowContainer';

export default class Finance extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
    }

    componentDidMount() {
        var component = this;
    }

    render() {
        return (
            <div className="col-md-12">
                <h1>Finance</h1>
                <RowContainer />
                <DataTable />
            </div>
        );
    }
}