import Person from '../Person';
import CalendarDay from './CalendarDay';
import CalendarTime from './CalendarTime';
import Reminder from './Reminder';

export default class Series {
    constructor(
        public id: number,
        public type: string,
        public title: string,
        public description: string,
        public participants: Person[] = [],
        public reminding: boolean,
        public reminder: Reminder,
        public firstOccurence: CalendarDay,
        public lastOccurence: CalendarDay,
        public timezone: string,
        public startTime: CalendarTime,
        public durationAmount: number,
        public durationUnit: string,
        public occupancy: string,
        public turnus: string,
        public skipping: number
    ) { }

}
