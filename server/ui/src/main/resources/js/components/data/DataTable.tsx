import * as React from 'react';

import TableData from './TableData';

export default class DataTable extends React.Component<any, undefined> {


    static propTypes = {
        id: React.PropTypes.string.isRequired,
        data: React.PropTypes.object.isRequired
    }

    private data: TableData;

    constructor( props: any ) {
        super( props );
        this.data = this.props.data;
    }

    render() {
        return <table id={this.props.id}>
        </table>;
    }

}
