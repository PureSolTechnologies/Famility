import React from 'react';

export default function MainDashboard() {
    return (
        <div className="row">
            <div className="col-md-12">
                <h1>Main Dashboard</h1>
            </div>
            <div className="col-md-6">
                <div className="card">
                    <h4 className="card-header">Weather</h4>
                    <div className="card-block">
                        <p className="card-text">Simple weather forecast. </p>
                    </div>
                </div>
            </div>
            <div className="col-md-6">
                <div className="card">
                    <h4 className="card-header">Today's and next Birthdays</h4>
                    <div className="card-block">
                        <p className="card-text">...</p>
                    </div>
                </div>
            </div>
            <div className="col-md-6">
                <div className="card">
                    <h4 className="card-header">Today's appointments</h4>
                    <div className="card-block">
                        <p className="card-text">...</p>
                    </div>
                </div>
            </div>
            <div className="col-md-6">
                <div className="card">
                    <h4 className="card-header">To be done today.</h4>
                    <div className="card-block">
                        <p className="card-text">...</p>
                    </div>
                </div>
            </div>
        </div >
    );
}
