import React from 'react';

import DateSelector from './components/calendar/DateSelector';

export default class CalendarLayout extends React.Component {

    constructor( props ) {
        super( props );
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-2">
                    <DateSelector />
                </div>
                <div className="col-md-10">
                    {this.props.children}
                </div>
            </div>
        );
    }
}
