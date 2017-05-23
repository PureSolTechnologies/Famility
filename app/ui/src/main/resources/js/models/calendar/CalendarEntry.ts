import Person from '../Person';
import Reminder from './Reminder';
import CalendarDay from './CalendarDay';
import CalendarTime from './CalendarTime';

export default class CalendarEntry {

    constructor(
        public id: number = -1,
        public type: string = 'appointment',
        public title: string = '',
        public description: string = '',
        public participants: Person[] = [],
        public reminding: boolean = true,
        public reminder: Reminder = new Reminder( 15, 'MINUTES' ),
        public beginDate: CalendarDay = null,
        public beginTime: CalendarTime = null,
        public beginTimezone: string = 'Europe/Berlin',
        public endDate: CalendarDay = null,
        public endTime: CalendarTime = null,
        public endTimezone: string = 'Europe/Berlin',
        public occupancy: string = 'OCCUPIED'
    ) { }
}
