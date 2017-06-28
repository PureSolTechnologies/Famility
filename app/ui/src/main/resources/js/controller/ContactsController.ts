import restController from './RESTController';
import TypeDefinition from '../models/contact/TypeDefinition';

export default class ContactsController {

    static getEmailAddressTypes( successfulCallback: ( types: TypeDefinition[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/email-address-types',
            null,
            function( response ) {
                var types: TypeDefinition[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getPostalAddressTypes( successfulCallback: ( types: TypeDefinition[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/postal-address-types',
            null,
            function( response ) {
                var types: TypeDefinition[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getPhoneNumberTypes( successfulCallback: ( types: TypeDefinition[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/phone-number-types',
            null,
            function( response ) {
                var types: TypeDefinition[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getBankAccountTypes( successfulCallback: ( types: TypeDefinition[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/bank-account-types',
            null,
            function( response ) {
                var types: TypeDefinition[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getOtherContactTypes( successfulCallback: ( types: TypeDefinition[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/contacts/other-contact-types',
            null,
            function( response ) {
                var types: TypeDefinition[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

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

    static getContact( contactId: number, successCallback: ( contact: any ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.GET( '/contacts/' + contactId, null,
            function( response ) {
                var contact: any = JSON.parse( response.response );
                successCallback( contact );
            },
            errorCallback
        );
    }

    static deleteContact( contactId: number, successCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        //{"name":"Rick-Rainer Ludwig","birthday":{"year":1978,"month":5,"dayOfMonth":16,"dayOfWeek":2,"weekOfYear":20,"quarterOfYear":2}}
        restController.DELETE( '/contacts/' + contactId, null,
            successCallback,
            errorCallback
        );
    }

}
