import * as React from 'react';

import RowContainer from './RowContainer';
import ColumnContainer from './ColumnContainer';
import Widget from './Widget';

export default class SelectorWidget extends Widget {

    constructor( props: any ) {
        super( props );
        this.state = { selection: "row", widget: new RowContainer( {}) };
        this.changeSelection = this.changeSelection.bind( this );
    }

    private changeSelection( event: any ): void {
        let selection: string = event.target.value;
        let widget: Widget;
        switch ( selection ) {
            case "row":
                widget = new RowContainer( {});
                break;
            case "col":
                widget = new ColumnContainer( {});
                break;
            default:
                widget = null;
        }
        this.setState( { selection: selection, widget: widget });
    }

    render(): any {
        let widget: any = this.state.widget != null ? this.state.widget.render() : <div></div>;
        return (
            <div>
                <div className="col-md-12" style={{ border: "1pt solid black" }}>
                    <select className="form-control" id="area" name="area" required={true} value={this.state.selection} onChange={this.changeSelection}>
                        <option value="row">Row Container</option>
                        <option value="col">Column Container</option>
                    </select>
                </div>
                <div className="col-md-12">
                    {widget}
                </div>
            </div>
        );
    }

}
