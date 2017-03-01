import React from 'react';
import Tab from './Tab';
import TabComponent from './TabComponent';

import DayView from './calendar/DayView';
import WeekView from './calendar/WeekView';
import MonthView from './calendar/MonthView';
import YearView from './calendar/YearView';

export default class CalendarComponent extends React.Component {
    
    constructor( props ) {
        super( props );
    }

    render() {
        return <div>
            <TabComponent>
                <Tab heading="Year"><YearView /></Tab>
                <Tab heading="Month"><MonthView /></Tab>
                <Tab heading="Week"><WeekView /></Tab>
                <Tab heading="Day"><DayView /></Tab>
            </TabComponent>
        </div>;
    }
}