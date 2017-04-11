export default class CalendarTime {

    /**
     * String: hh-MM-ss
     */
    static fromString( s ) {
        var date = new Date( "1900-01-01T" + s );
        return CalendarTime.fromDate( date );
    }

    static fromDate( date ) {
        var calendarTime = new CalendarTime;
        calendarTime.hour = date.getHours();
        calendarTime.minute = date.getMinutes();
        calendarTime.second = date.getSeconds();
        return calendarTime;
    }

    hour;
    minute;
    second;

}
