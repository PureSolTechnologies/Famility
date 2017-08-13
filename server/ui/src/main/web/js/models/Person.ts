import CalendarDay from './calendar/CalendarDay';

export default class Person {

    constructor(
        public id: number = -1,
        name: string = '',
        birthday: CalendarDay
    ) { };

}
