import React from 'react';

export default class WeekView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { calendar: props.calendar };
    }

    render() {
        return <div>
            WeekView...
        </div>;
    }
}