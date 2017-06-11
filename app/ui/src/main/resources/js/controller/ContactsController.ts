import restController from './RESTController';

export default class ContactsController {

    static getContacts( successfulCallback: ( contacts: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts',
            null,
            function( response ) {
                var contacts: any = JSON.parse( response.response );
                successfulCallback( contacts );
            },
            errorCallback
        );
    }

    static getBirthdays( successfulCallback: ( birtdays: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/birthdays',
            null,
            function( response ) {
                var birthdays = JSON.parse( response.response );
                successfulCallback( birthdays );
            },
            errorCallback
        );
    }

    static createContact( userName: string, birthday: string, successCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        var d: any = new Date( birthday );
        restController.PUT( '/contacts', { "Content-Type": "application/json; charset=utf-8" },
            { name: userName, birthday: { year: d.getYear() + 1900, month: d.getMonth() + 1, dayOfMonth: d.getDate() } },
            successCallback,
            errorCallback
        );
    }

    static deleteContact( userId: number, successCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.DELETE( '/contacts/' + userId, null,
            successCallback,
            errorCallback
        );
    }

}
