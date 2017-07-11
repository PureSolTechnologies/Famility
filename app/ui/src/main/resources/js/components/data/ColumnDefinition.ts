import ColumnType from './ColumnType';

export default class ColumnDefinition {

    constructor( public name: string, public columnType: ColumnType ) { }

    public getName(): string {
        return this.name;
    }

    public getType(): any {
        return this.columnType;
    }
}
