import * as React from 'react';
import { browserHistory } from 'react-router';

import ApplicationComponent from '../ApplicationComponent';

declare var $: any;

export default class AbstractEntriesView<P, S> extends ApplicationComponent<P, S> {

    constructor( props: any ) {
        super( props );
    }

    protected findEntries( entries: any, month: number, day: number, hour: number ): any[] {
        let foundEntries: any[] = [];
        if ( entries.months ) {
            let candidates: any[] = entries.months[month][day];
            if ( candidates ) {
                for ( let candidate of candidates ) {
                    if ( candidate.time.hour === hour ) {
                        foundEntries.push( candidate );
                    }
                }
            }
        }
        return foundEntries;
    }

    protected isNow( year: number, month: number, day: number, hour: number ): boolean {
        let now: any = new Date();
        if ( now.getYear() + 1900 != year ) {
            return false;
        }
        if ( now.getMonth() + 1 != month ) {
            return false;
        }
        if ( now.getDate() != day ) {
            return false;
        }
        if ( now.getHours() != hour ) {
            return false;
        }
        return true;
    }

    protected createTableRowEntry( entries: any[], year: number, month: number, day: number, hour: number ): any {
        let content: any = {};
        let isNow: boolean = this.isNow( year, month, day, hour );
        if ( entries.length == 0 ) {
            if ( !isNow ) {
                return <td></td>;
            } else {
                return <td className="alert-success"></td>;
            }
        }
        if ( isNow ) {
            return <td className="alert-danger">{this.renderEntries( entries )}</td>;
        } else {
            return <td className="alert-warning">{this.renderEntries( entries )}</td>;
        }
    }

    protected createEntry( year: number, month: number, day: number, hour: number ) {
        let yearString: string = String( year );
        let monthString: string = month < 10 ? '0' + month : String( month );
        let dayString: string = day < 10 ? '0' + day : String( day );
        let hourStringFrom: string = hour < 10 ? '0' + hour : String( hour );
        let hourStringTo: string = hour + 1 < 10 ? '0' + ( hour + 1 ) : String( hour + 1 );
        browserHistory.push( '/dialog/calendar/create-entry/' + yearString + '-' + monthString + '-' + dayString + '/' + hourStringFrom + ':00:00' + '/' + hourStringTo + ':00:00' );
    }

    /**
     * This method renders an array of elements representing the entries of the calendar.
     */
    protected renderEntries( entries: any[] ): any {
        let renderedEntries: any[] = [];
        let key: number = 0;
        for ( let entry of entries ) {
            let tooltip: string = "<b>" + entry.title + "</b><br/><p>" + entry.description + "</p>";
            renderedEntries.push( <span key={key} data-toggle="tooltip" data-placement="bottom" data-html="true" title={tooltip} style={{ border: "solid black 1pt" }}>{entry.title}</span > )
            key++;
        }
        return renderedEntries;
    }

}