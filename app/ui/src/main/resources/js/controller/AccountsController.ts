import restController from './RESTController';

export default class AccountsController {

    static createAccount( email: string, password: string, successfulCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.POST( '/accounts/' + email,
            { 'password': password },
            null,
            successfulCallback,
            errorCallback
        );
    }

    static activateAccount( email: string, key: string, successfulCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.POST( '/accounts/' + email + '/activate/' + key,
            null,
            null,
            successfulCallback,
            errorCallback
        );
    }
}

