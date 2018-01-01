import TableDefinition from './TableDefinition';
import TableRow from './TableRow';

export default class DataTable {

    constructor( private definition: TableDefinition, private rows: TableRow[] ) { }

    public getDefinition(): TableDefinition {
        return this.definition;
    }

    public getRows(): TableRow[] {
        return this.rows;
    }
}
