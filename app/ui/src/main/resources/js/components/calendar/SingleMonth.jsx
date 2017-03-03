import React from 'react';

export default class SingleMonth extends React.Component {

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
        var dayId = 1;
        var day = days[dayId];
        for ( var week = startWeek; week <= endWeek; week++ ) {
            var daysRow = [];
            for ( var dow = 1; dow <= 7; dow++ ) {
                if ( ( day ) && ( dow < day["dayOfWeek"] ) ) {
                    daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                } else {
                    if ( day ) {
                        daysRow.push( <td key={daysRow.length}>{dayId}</td> );
                    } else {
                        daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                    }
                    dayId++;
                    day = days[dayId];
                }
            }
            weeks.push(
                <tr key={week}>
                    <th>{week > 0 ? week : ""}</th>
                    {daysRow}
                </tr>
            );
        }
        return <table width="100%" style={{ margin: "0pt", spacing: "0pt", border: "1pt solid  gray" }}>
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
        </table>;
    }
}