import React from 'react';

import LocalWeather from '../components/LocalWeather';
import PeopleController from '../controller/PeopleController';
import CalendarController from '../controller/CalendarController';
import EntriesToday from '../components/calendar/EntriesToday';
import EntriesTomorrow from '../components/calendar/EntriesTomorrow';
import Birthdays from '../components/calendar/Birthdays';

export default class MainDashboard extends React.Component {

    constructor( props ) {
        super( props );
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Today</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesToday />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Weather</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <LocalWeather />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Tomorrow</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesTomorrow />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Birthdays</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <Birthdays />
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}
