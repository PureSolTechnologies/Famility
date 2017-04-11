export default class CalendarDay {

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

    year;
    month;
    dayOfMonth;

}
