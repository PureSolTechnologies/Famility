export default class CalendarTime {

    static fromEntry( entry ) {
        var time = new CalendarTime;
        time.hour = entry.time.hour;
        time.minute = entry.time.minute;
        time.second = entry.time.second;
        return time;
    }

    /**
     * String: hh-MM-ss
     */
    static fromString( s ) {
        var parts = s.split( ':' );
        var calendarTime = new CalendarTime;
        calendarTime.hour = parts[0];
        if ( parts[1] ) {
            calendarTime.minute = parts[1];
        } else {
            calendarTime.minute = 0;
        }
        if ( parts[2] ) {
            calendarTime.second = parts[2];
        } else {
            calendarTime.second = 0;
        }
        return calendarTime;
    }

    static fromDate( date ) {
        var calendarTime = new CalendarTime;
        calendarTime.hour = date.getHours();
        calendarTime.minute = date.getMinutes();
        calendarTime.second = date.getSeconds();
        return calendarTime;
    }

    toString( showSecond = true ) {
        var minute = this.minute;
        if ( minute < 10 ) {
            minute = "0" + minute;
        }
        var second = this.second;
        if ( second < 10 ) {
            second = "0" + second;
        }
        if ( showSecond ) {
            return this.hour + ":" + minute + ":" + second;
        } else {
            return this.hour + ":" + minute;
        }
    }

    hour;
    minute;
    second;

}
