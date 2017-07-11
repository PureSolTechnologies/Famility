import * as React from 'react';

import ContainerWidget from './ContainerWidget';
import Icon from './Icon';
import SelectorWidget from './SelectorWidget';

export default class ColumnContainer extends ContainerWidget {

    constructor( props: any ) {
        super( props );
        this.add = this.add.bind( this );
        this.delete = this.delete.bind( this );
    }

    private add(): void {
        let widget: SelectorWidget = new SelectorWidget( {});
        super.appendWidget( widget );
        this.setState( {});
    }

    private delete(): void {
        super.removeWidget();
        this.setState( {});
    }

    render(): any {
        let cols: any[] = [];
        for ( let widget of this.getWidgets() ) {
            cols.push(
                <div className="col-md-2">{widget.render()}</div>
            );
        }
        return (
            <div>
                <div className="col-md-12" style={{ "background-color": "#c0c0c0", border: "1pt solid black" }}>
                    <button type="button" className="btn btn-secondary" disabled={this.getWidgetNum() >= 12} onClick={this.add}><Icon name="application_add" /></button>
                    <button type="button" className="btn btn-secondary" disabled={this.getWidgetNum() <= 0} onClick={this.delete}><Icon name="application_delete" /></button>
                </div>
                {cols}
            </div>
        );
    }

}