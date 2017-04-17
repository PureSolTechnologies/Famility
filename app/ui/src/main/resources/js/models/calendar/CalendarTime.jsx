export default class CalendarTime {

    /**
     * String: hh-MM-ss
     */
    static fromString( s ) {
        var parts = s.split(':');
        var calendarTime = new CalendarTime;
        calendarTime.hour = parts[0];
        if ( parts[1] ) {
            calendarTime.minute = parts[1];            
        } else {
            calendarTime.minute = 0;                        
        }
        if (parts[2]) {
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

    hour;
    minute;
    second;

}
