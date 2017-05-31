import CalendarEvent from './CalendarEvent';

export default class CalendarTime {

    static fromEvent( entry: CalendarEvent ): CalendarTime {
        var time = new CalendarTime;
        time.hour = entry.beginTime.hour;
        time.minute = entry.beginTime.minute;
        time.second = entry.beginTime.second;
        return time;
    }

    /**
     * String: hh-MM-ss
     */
    static fromString( s: string ): CalendarTime {
        var parts = s.split( ':' );
        var calendarTime = new CalendarTime;
        calendarTime.hour = Number( parts[0] );
        if ( parts[1] ) {
            calendarTime.minute = Number( parts[1] );
        } else {
            calendarTime.minute = 0;
        }
        if ( parts[2] ) {
            calendarTime.second = Number( parts[2] );
        } else {
            calendarTime.second = 0;
        }
        return calendarTime;
    }

    static fromDate( date: any ): CalendarTime {
        var calendarTime = new CalendarTime;
        calendarTime.hour = date.getHours();
        calendarTime.minute = date.getMinutes();
        calendarTime.second = date.getSeconds();
        return calendarTime;
    }

    toString( showSecond: boolean = true ): string {
        var minute = String( this.minute );
        if ( this.minute < 10 ) {
            minute = "0" + minute;
        }
        var second: string = String( this.second );
        if ( this.second < 10 ) {
            second = "0" + second;
        }
        if ( showSecond ) {
            return this.hour + ":" + minute + ":" + second;
        } else {
            return this.hour + ":" + minute;
        }
    }

    constructor(
        public hour: number = null,
        public minute: number = null,
        public second: number = null
    ) { }

}
