import ColumnDefinition from './ColumnDefinition';

export default class TableDefinition {

    constructor( private name: string, private columnDefinitions: ColumnDefinition[] ) { }

    public getName(): string {
        return this.name;
    }

    public getColumnDefinitions(): ColumnDefinition[] {
        return this.columnDefinitions;
    }

}
