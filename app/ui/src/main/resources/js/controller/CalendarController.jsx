import restController from './RESTController';

export default class CalendarController {

    static namesOfMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    static getNameOfMonth( month ) {
        return CalendarController.namesOfMonths[month - 1];
    }

    static getYear( year, successfulCallback, errorCallback ) {
        restController.GET( '/calendar/year/' + year,
            null,
            function( response ) {
                var calendar = JSON.parse( response.response );
                calendar.weeks = {};
                for ( var month in calendar.months ) {
                    if ( calendar.months.hasOwnProperty( month ) ) {
                        var monthObject = calendar.months[month];
                        for ( var day in monthObject.days ) {
                            if ( monthObject.days.hasOwnProperty( day ) ) {
                                var dayObject = monthObject.days[day];
                                if ( !calendar.weeks[dayObject.weekOfYear] ) {
                                    calendar.weeks[dayObject.weekOfYear] = {};
                                }
                                calendar.weeks[dayObject.weekOfYear][dayObject.dayOfWeek] = dayObject;
                            }
                        }
                    }
                }
                successfulCallback( calendar );
            },
            errorCallback
        );
    }

}
