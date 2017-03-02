import React from 'react';

import CalendarController from '../../controller/CalendarController';

class SingleMonth extends React.Component {

    constructor( props ) {
        super( props );
    }

    render() {
        var weeks = [];
        var days = this.props.data.days;
        var startWeek = days["1"]["weekOfYear"];
        var endWeek = days[Object.keys( days ).length]["weekOfYear"];
        if ( startWeek > endWeek ) {
            startWeek = 0;
        }
        for ( var week = startWeek; week <= endWeek; week++ ) {
            var days = [];
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            days.push(<td>&nbsp;</td>);
            weeks.push(
                <tr>
                    <td>{week > 0 ? week : ""}</td>
                    {days}
                </tr>
            );
        }
        return <div className="card">
            <div className="card-header" style={{ margin: "0pt", padding: "5pt" }}>
                {this.props.month}
            </div>
            <div className="card-block" style={{ margin: "0pt", padding: "0pt" }}>
                <table width="100%" style={{ margin: "0pt", spacing: "0pt", border: "1pt solid  gray" }}>
                    <thead style={{ border: "1pt solid  gray" }}>
                        <tr>
                            <th></th>
                            <th>M</th>
                            <th>T</th>
                            <th>W</th>
                            <th>T</th>
                            <th>F</th>
                            <th>S</th>
                            <th>S</th>
                        </tr>
                    </thead>
                    <tbody>
                        {weeks}
                    </tbody>
                </table>
            </div>
        </div>;
    }
}

export default class YearView extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { year: null };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getYear( 2017,
            function( response ) {
                component.setState( { year: JSON.parse( response.response ) });
            },
            function( response ) {
            }
        );
    }

    render() {
        var rows = [];
        if ( this.state.year ) {
            rows.push(
                <div className="row" key="1">
                    <div className="col-md-4">
                        <SingleMonth month="January" data={this.state.year.months["1"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="February" data={this.state.year.months["2"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="March" data={this.state.year.months["3"]} />
                    </div>
                </div>
            );
            rows.push(
                <div className="row" key="2">
                    <div className="col-md-4">
                        <SingleMonth month="April" data={this.state.year.months["4"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="May" data={this.state.year.months["5"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="June" data={this.state.year.months["6"]} />
                    </div>
                </div>
            );
            rows.push(
                <div className="row" key="3">
                    <div className="col-md-4">
                        <SingleMonth month="July" data={this.state.year.months["7"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="August" data={this.state.year.months["8"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="September" data={this.state.year.months["9"]} />
                    </div>
                </div>
            );
            rows.push(
                <div className="row" key="4">
                    <div className="col-md-4">
                        <SingleMonth month="October" data={this.state.year.months["10"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="November" data={this.state.year.months["11"]} />
                    </div>
                    <div className="col-md-4">
                        <SingleMonth month="December" data={this.state.year.months["12"]} />
                    </div>
                </div>
            );
        }
        return <div>
            <h1>2017</h1>
            {rows}
        </div>;
    }
}