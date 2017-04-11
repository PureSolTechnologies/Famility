import React from 'react';

import CalendarController from '../../controller/CalendarController';

export default class TimeZoneSelector extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            timezones: [],
            timezone: "UTC"
        }
    }

    componentDidMount() {
        var component = this;
        CalendarController.getTimezones(
            function( timezones ) { component.setState( { timezones: timezones }) },
            function( response ) { }
        );
    }

    update() {
        const dayState = store.getState().calendar.day;
        if ( this.state.day != dayState ) {
            this.setState( { day: dayState });
        }
    }

    render() {
        var options = [];
        for ( var timezone of this.state.timezones ) {
            options.push(
                <option key={timezone.id} value={timezone.id}>{timezone.id} - {timezone.name}</option>
            );
        }
        return (
            <div className="form-group">
                <label htmlFor="timezone">Timezone</label>
                <select className="form-control" id="timezone" name="timezone" required="true" size="5">
                    {options}
                </select>
            </div>
        );
    }

}
