export default class CalendarDay {

    static fromEntry( entry ) {
        var day = new CalendarDay;
        day.year = entry.date.year;
        day.month = entry.date.month;
        day.dayOfMonth = entry.date.dayOfMonth;
        return day;
    }

    /**
     * String: yyyy-mm-dd
     */
    static fromString( s ) {
        var date = new Date( s );
        return CalendarDay.fromDate( date );
    }

    static fromDate( date ) {
        var calendarDay = new CalendarDay;
        calendarDay.year = date.getYear() + 1900;
        calendarDay.month = date.getMonth() + 1;
        calendarDay.dayOfMonth = date.getDate();
        return calendarDay;
    }

    toString() {
        var day = this.dayOfMonth;
        if ( day < 10 ) {
            day = "0" + day;
        }
        var month = this.month;
        if ( month < 10 ) {
            month = "0" + month;
        }
        return day + "." + month + "." + this.year;
    }

    year;
    month;
    dayOfMonth;

}
