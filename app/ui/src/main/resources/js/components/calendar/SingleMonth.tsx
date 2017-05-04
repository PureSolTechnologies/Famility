import * as React from 'react';
import { Link } from 'react-router';

export default class SingleMonth extends React.Component<any, any> {

    static propTypes = {
        month: React.PropTypes.number.isRequired,
        data: React.PropTypes.object.isRequired,
        entries: React.PropTypes.array,
    };

    constructor( props: any ) {
        super( props );
        this.state = { month: props.month, data: props.data, entries: props.entries };
    }

    componentWillReceiveProps( nextProps: any ) {
        var newState: any = {};
        if ( this.state.month != nextProps.month ) {
            newState.month = nextProps.month;
        }
        if ( this.state.data != nextProps.data ) {
            newState.data = nextProps.data;
        }
        if ( this.state.entries != nextProps.entries ) {
            newState.entries = nextProps.entries;
        }
        this.setState( newState );
    }

    render() {
        var weeks: any[] = [];
        var days = this.state.data.days;
        var startWeek = days["1"]["weekOfYear"];
        var endWeek = days[Object.keys( days ).length]["weekOfYear"];
        if ( startWeek > endWeek ) {
            startWeek = 0;
        }
        var now: any = new Date();
        var nowYear: number = now.getYear() + 1900;
        var nowMonth: number = now.getMonth() + 1;
        var nowDay: number = now.getDate();
        var dayId = 1;
        var day = days[dayId];
        for ( var week = startWeek; week <= endWeek; week++ ) {
            var daysRow: any[] = [];
            for ( var dow = 1; dow <= 7; dow++ ) {
                if ( ( day ) && ( dow < day["dayOfWeek"] ) ) {
                    daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                } else {
                    if ( day ) {
                        var isToday: boolean = false;
                        var style: any = {};
                        var className: string = "";
                        if ( ( this.state.data.year === nowYear ) && ( this.state.data.month === nowMonth ) && ( dayId === nowDay ) ) {
                            style.border = "solid red 2pt";
                            isToday = true;
                        }
                        if ( ( this.state.entries ) && ( this.state.entries[dayId] ) ) {
                            style.fontWeight = "900";
                            if ( isToday ) {
                                className = "alert-danger";
                            } else {
                                className = "alert-warning";
                            }
                        }
                        daysRow.push( <td key={daysRow.length} className={className} style={style}><Link to={'/calendar/day/' + this.state.data.year + '/' + this.state.data.month + '/' + dayId}>{dayId}</Link></td> );
                    } else {
                        daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                    }
                    dayId++;
                    day = days[dayId];
                }
            }
            weeks.push(
                <tr key={week}>
                    <th style={{ borderRight: '1pt solid #000000' }}><Link to={'/calendar/week/' + this.state.data.year + '/' + week}>{week > 0 ? week : ''}</Link></th>
                    {daysRow}
                </tr>
            );
        }
        return <table width="100%" style={{ margin: "0pt", spacing: "0pt", border: "1pt solid  gray" }}>
            <thead style={{ border: "1pt solid  gray" }}>
                <tr>
                    <th></th>
                    <th>Mo</th>
                    <th>Tu</th>
                    <th>We</th>
                    <th>Th</th>
                    <th>Fr</th>
                    <th>Sa</th>
                    <th>Su</th>
                </tr>
            </thead>
            <tbody>
                {weeks}
            </tbody>
        </table>;
    }
}