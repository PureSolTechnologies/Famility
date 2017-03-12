import restController from './RESTController';

export default class PeopleController {

    static getPeople( successfulCallback, errorCallback ) {
        restController.GET( '/people',
            null,
            function( response ) {
                var people = JSON.parse( response.response );
                successfulCallback( people );
            },
            errorCallback
        );
    }

    static getBirthdays( successfulCallback, errorCallback ) {
        restController.GET( '/people/birthdays',
            null,
            function( response ) {
                var birthdays = JSON.parse( response.response );
                successfulCallback( birthdays );
            },
            errorCallback
        );
    }

    static addUser( userName, birthday, successCallback, errorCallback ) {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.PUT( '/people', { "Content-Type": "application/json; charset=utf-8" },
            { name: userName, birthday: birthday },
            successCallback,
            errorCallback
        );
    }

    static deleteUser( userId, successCallback, errorCallback ) {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.DELETE( '/people/' + userId, null,
            successCallback,
            errorCallback
        );
    }

}
