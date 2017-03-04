import React from 'react';

import CalendarComponent from '../components/CalendarComponent';

export default function Calendar() {
    return (
        <div className="row">
            <div className="col-md-12">
                <CalendarComponent year="2016"/>
            </div>
        </div >
    );
}