import Reminder from './Reminder';
import CalendarDay from './CalendarDay';
import CalendarTime from './CalendarTime';

export default class CalendarEntry {

    constructor(
        public id: number = -1,
        public type: string = 'appointment',
        public title: string = '',
        public description: string = '',
        public participants: string[] = [],
        public reminding: boolean = true,
        public reminder: Reminder = new Reminder( 15, 'MINUTES' ),
        public durationAmount: number = 1,
        public durationUnit: string = 'HOURS',
        public date: CalendarDay = new CalendarDay,
        public time: CalendarTime = new CalendarTime,
        public timezone: string = 'Europe/Berlin',
        public occupancy: string = 'OCCUPIED'
    ) { }
}
