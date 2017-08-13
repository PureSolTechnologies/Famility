import * as React from 'react';

import ApplicationComponent from '../ApplicationComponent';

import CalendarDay from '../../models/calendar/CalendarDay';
import CalendarTime from '../../models/calendar/CalendarTime';
import EventIcon from './EventIcon';

export default class EventView extends ApplicationComponent<any, undefined> {

    constructor( props: any ) {
        super( props );
    }

    public render() {
        let event: any = this.props.event;
        return (
            <div>
                <h2> <EventIcon type={event.type} /> {event.title}</h2>
                <p>
                    <i>{CalendarDay.fromEvent( event ).toString()} {CalendarTime.fromEvent( event ).toString()} ({event.timezone} )</i><br />
                    <i>for {event.durationAmount} {event.durationUnit}</i>
                </p>
                <p>
                    {event.description}
                </p>
                <p>
                    {JSON.stringify( event.participants )}
                </p>
            </div>
        );
    }
}