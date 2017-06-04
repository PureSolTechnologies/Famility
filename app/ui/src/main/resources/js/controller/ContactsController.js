import restController from './RESTController';

export default class ContactsController {

    static getContacts( successfulCallback, errorCallback ) {
        restController.GET( '/contacts',
            null,
            function( response ) {
                var contacts = JSON.parse( response.response );
                successfulCallback( contacts );
            },
            errorCallback
        );
    }

    static getBirthdays( successfulCallback, errorCallback ) {
        restController.GET( '/contacts/birthdays',
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
        var d = new Date( birthday );
        restController.PUT( '/contacts', { "Content-Type": "application/json; charset=utf-8" },
            { name: userName, birthday: { year: d.getYear() + 1900, month: d.getMonth() + 1 , dayOfMonth: d.getDate() } },
            successCallback,
            errorCallback
        );
    }

    static deleteUser( userId, successCallback, errorCallback ) {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.DELETE( '/contacts/' + userId, null,
            successCallback,
            errorCallback
        );
    }

}
