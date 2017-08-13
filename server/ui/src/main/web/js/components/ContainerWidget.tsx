import * as React from 'react';

import Widget from './Widget';

export default class ContainerWidget extends Widget {

    protected widgets: Widget[] = [];

    constructor( props: any ) {
        super( props );
    }

    protected getWidgets(): Widget[] {
        return this.widgets;
    }

    protected getWidgetNum(): number {
        return this.widgets.length;
    }

    protected appendWidget( widget: any ): void {
        this.widgets.push( widget );
    }

    protected removeWidget(): void {
        this.widgets.pop();
    }

    render(): any {
        return (
            <div></div>
        );
    }

}
