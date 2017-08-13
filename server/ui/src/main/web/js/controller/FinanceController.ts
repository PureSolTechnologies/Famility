import restController from './RESTController';

export default class FinanceController {

    static getCurrencies( successfulCallback: ( currencies: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/finance/currencies',
            null,
            function( response ) {
                let currencies: any[] = JSON.parse( response.response );
                successfulCallback( currencies );
            },
            errorCallback
        );
    }

}

