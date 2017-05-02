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
                        <h3 className="card-header">Weather</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <LocalWeather />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Today's and next Birthdays</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <Birthdays />
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card">
                        <h3 className="card-header">Today's Appointments</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesToday type="appointment" />
                        </div>
                    </div>
                    <div className="card">
                        <h3 className="card-header">Tomorrow's Appointments</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesTomorrow type="appointment" />
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card">
                        <h3 className="card-header">Today's Anniversaries</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesToday type="anniversary" />
                        </div>
                    </div>
                    <div className="card">
                        <h3 className="card-header">Tomorrow's Anniversaries</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesTomorrow type="anniversary" />
                        </div>
                    </div>
                </div>
                <div className="col-md-4">
                    <div className="card">
                        <h3 className="card-header">Today's TODOs</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesToday type="todo" />
                        </div>
                    </div>
                    <div className="card">
                        <h3 className="card-header">Tomorrow's TODOs</h3>
                        <div className="card-block" style={{ padding: "0pt" }}>
                            <EntriesTomorrow type="todo" />
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}
