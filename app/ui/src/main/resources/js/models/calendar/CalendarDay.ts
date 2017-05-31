import CalendarEvent from './CalendarEvent';

export default class CalendarDay {

    static fromEvent( event: CalendarEvent ): CalendarDay {
        var day = new CalendarDay;
        day.year = event.beginDate.year;
        day.month = event.beginDate.month;
        day.dayOfMonth = event.beginDate.dayOfMonth;
        return day;
    }

    /**
     * String: yyyy-mm-dd
     */
    static fromString( s: string ): CalendarDay {
        var date = new Date( s );
        return CalendarDay.fromDate( date );
    }

    static fromDate( date: any ): CalendarDay {
        var calendarDay = new CalendarDay;
        calendarDay.year = date.getYear() + 1900;
        calendarDay.month = date.getMonth() + 1;
        calendarDay.dayOfMonth = date.getDate();
        return calendarDay;
    }

    toString(): string {
        let day: string = String( this.dayOfMonth );
        if ( this.dayOfMonth < 10 ) {
            day = "0" + day;
        }
        let month: string = String( this.month );
        if ( this.month < 10 ) {
            month = "0" + month;
        }
        return day + "." + month + "." + String( this.year );
    }

    constructor(
        public year: number = null,
        public month: number = null,
        public dayOfMonth: number = null
    ) { }

}
