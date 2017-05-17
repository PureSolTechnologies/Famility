import * as React from 'react';

import ApplicationComponent from '../ApplicationComponent';
import CalendarEntryLabel from './CalendarEntryLabel';

import CalendarDay from '../../models/calendar/CalendarDay';
import CalendarTime from '../../models/calendar/CalendarTime';
import EntryIcon from './EntryIcon';

export default class EntryView extends ApplicationComponent<any, undefined> {

    constructor( props: any ) {
        super( props );
    }

    public render() {
        let entry: any = this.props.entry;
        return (
            <div>
                <h2> <EntryIcon type={entry.type} /> {entry.title}</h2>
                <p>
                    <i>{CalendarDay.fromEntry( entry ).toString()} {CalendarTime.fromEntry( entry ).toString()} ({entry.timezone} )</i><br />
                    <i>for {entry.durationAmount} {entry.durationUnit}</i>
                </p>
                <p>
                    {entry.description}
                </p>
                <p>
                    {JSON.stringify( entry.participants )}
                </p>
            </div>
        );
    }
}