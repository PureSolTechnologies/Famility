import restController from './RESTController';

export default class CalendarController {

    static getYear( year, successfulCallback, errorCallback ) {
        restController.GET( '/calendar/year/' + year,
            null,
            successfulCallback, errorCallback
        );
    }

}
